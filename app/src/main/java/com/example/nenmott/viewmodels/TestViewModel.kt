package com.example.nenmott.viewmodels

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nenmott.data.Module
import com.example.nenmott.data.Question
import com.example.nenmott.data.Test
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TestViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val mediaPlayer = MediaPlayer()

    private val _modules = MutableStateFlow<List<Module>>(emptyList())
    val modules: StateFlow<List<Module>> get() = _modules

    private val _currentQuestion = MutableStateFlow<Question?>(null)
    val currentQuestion: StateFlow<Question?> get() = _currentQuestion

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> get() = _progress

    private val _totalXp = MutableStateFlow(0)
    val totalXp: StateFlow<Int> get() = _totalXp

    private val _totalCoins = MutableStateFlow(0)
    val totalCoins: StateFlow<Int> get() = _totalCoins

    private val _currentTest = MutableStateFlow<Test?>(null)
    val currentTest: StateFlow<Test?> get() = _currentTest

    private val _incorrectQuestions = MutableStateFlow<List<Question>>(emptyList())
    val incorrectQuestions: StateFlow<List<Question>> get() = _incorrectQuestions

    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer: StateFlow<String?> get() = _selectedAnswer

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private var allQuestions = listOf<Question>()
    private var incorrectQuestionsList = mutableListOf<Question>()
    private var reviewMode = false

    fun startTest(test: Test, module: Module) {
        _currentTest.value = test
        loadQuestions(test,module)
    }

    fun resetTest() {
        _currentQuestion.value = null
        _selectedAnswer.value = null
        _progress.value = 0f
        _currentTest.value = null
        incorrectQuestionsList.clear()
        reviewMode = false
    }
    private fun loadQuestions(test: Test, module: Module) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = firestore.collection("modules")
                    .document(test.moduleId)
                    .collection("tests")
                    .document(test.id)
                    .collection("questions")
                    .get()
                    .await()

                allQuestions = result.toObjects(Question::class.java)
                if (allQuestions.isNotEmpty()) {
                    _currentQuestion.value = allQuestions.first()
                    _progress.value = 0f
                    incorrectQuestionsList.clear()
                } else {
                    println("No questions found for test: ${test.id}")
                }
            } catch (e: Exception) {
                println("Error loading questions: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun submitAnswer() {
        val currentQuestion = _currentQuestion.value ?: return
        val answer = _selectedAnswer.value

        println("Submitting answer for question: ${currentQuestion.id}")
        println("Selected answer: $answer")
        println("Correct answer: ${currentQuestion.correctAnswer}")

        if (answer != currentQuestion.correctAnswer) {
            println("Answer is incorrect, adding to incorrectQuestionsList")
            incorrectQuestionsList.add(currentQuestion)
        }else{
            _progress.value = 1f/allQuestions.size
        }

        _selectedAnswer.value = null

        val nextQuestion = getNextQuestion()
        if (nextQuestion != null) {
            println("Next question: ${nextQuestion.id}")
            _currentQuestion.value = nextQuestion
        } else {
            if (!reviewMode && incorrectQuestionsList.isNotEmpty()) {
                println("Starting review of incorrect questions")
                allQuestions = incorrectQuestionsList.toList()
                incorrectQuestionsList.clear()
                _currentQuestion.value = allQuestions.first()
                reviewMode = true
            } else {
                println("Finishing test")
                finishTest()
            }
        }
    }

    private fun getNextQuestion(): Question? {
        val currentIndex = allQuestions.indexOf(_currentQuestion.value)
        println("Current question index: $currentIndex")
        return if (currentIndex + 1 < allQuestions.size) {
            allQuestions[currentIndex + 1]
        } else {
            null
        }
    }
    private fun updateUserXPAndCoins(xp: Int, coin: Int) {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                currentUser?.let {
                    val userId = it.uid
                    val userRef = firestore.collection("users").document(userId)

                    firestore.runTransaction { transaction ->
                        val userSnapshot = transaction.get(userRef)
                        val currentXp = userSnapshot.getLong("xp") ?: 0
                        val currentCoins = userSnapshot.getLong("coins") ?: 0

                        val newXp = currentXp + xp
                        val newCoins = currentCoins + coin

                        transaction.update(userRef, "xp", newXp)
                        transaction.update(userRef, "coins", newCoins)
                    }.await()
                }
            } catch (e: Exception) {
                println("Error updating user XP and coins: ${e.message}")
            }
        }
    }


    private fun finishTest() {
        _currentTest.value?.let {
            println("Test completed, awarding XP and coins")
            _totalXp.value += it.xp
            _totalCoins.value += it.coin
            updateUserXPAndCoins(it.xp, it.coin)
            markTestAsCompleted(it)
        }
        _currentQuestion.value = null
        println("Test finished")
    }

    private fun markTestAsCompleted(test: Test) {
        viewModelScope.launch {
            try {
                val moduleDoc = firestore.collection("modules").document(test.moduleId)
                val testDoc = moduleDoc.collection("tests").document(test.id)
                testDoc.update("completed", true).await()

                val result = moduleDoc.collection("tests").get().await()
                val allTests = result.toObjects(Test::class.java)
                if (allTests.all { it.completed }) {
                    moduleDoc.update("completed", true).await()
                    unlockNextModule(test.moduleId)
                }
            } catch (e: Exception) {
                println("Error marking test as completed: ${e.message}")
            }
        }
    }

    private fun unlockNextModule(currentModuleId: String) {
        viewModelScope.launch {
            try {
                val result = firestore.collection("modules").get().await()
                val allModules = result.toObjects(Module::class.java)
                val currentModuleIndex = allModules.indexOfFirst { it.id == currentModuleId }
                if (currentModuleIndex >= 0 && currentModuleIndex + 1 < allModules.size) {
                    val nextModule = allModules[currentModuleIndex + 1]
                    firestore.collection("modules").document(nextModule.id).update("active", true).await()
                }
            } catch (e: Exception) {
                println("Error unlocking next module: ${e.message}")
            }
        }
    }

    fun playSound(audioUrl: String?, context: Context) {
        if (audioUrl != null) {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(audioUrl)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }

    fun selectAnswer(answer: String) {
        _selectedAnswer.value = answer
    }

    fun markAsRead(questionId: String) {
        loadNextQuestion()
    }

    private fun loadNextQuestion() {
        val nextQuestion = getNextQuestion()
        if (nextQuestion != null) {
            _currentQuestion.value = nextQuestion
        } else {
            finishTest()
        }
    }

    init {
        loadAllData()
    }

    private fun loadAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val modules = getModules()
                val updatedModules = modules.toMutableList()

                val deferredList = modules.mapIndexed { index, module ->
                    async {
                        val tests = getTestsForModule(module.id)
                        updatedModules[index] = module.copy(tests = tests)
                    }
                }
                deferredList.awaitAll()

                _modules.value = updatedModules
                println("Modules loaded: ${_modules.value.size}")
                _modules.value.forEach {
                    println("Module: ${it.title}, Tests: ${it.tests.size}")
                }
                println("Все скачено")
            } catch (e: Exception) {
                println("Error loading all data: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun getModules(): List<Module> {
        return try {
            val snapshot = firestore.collection("modules").get().await()
            println("Modules snapshot size: ${snapshot.size()}")
            snapshot.documents.map { document ->
                val module = document.toObject(Module::class.java)!!.copy(id = document.id)
                println("Loaded module: ${module.title} with id ${module.id}")
                module
            }
        } catch (e: Exception) {
            println("Error getting modules: ${e.message}")
            emptyList()
        }
    }

    private suspend fun getTestsForModule(moduleId: String): List<Test> {
        return try {
            println("Fetching tests for module id: $moduleId")
            val moduleDocRef = firestore.collection("modules").document(moduleId)
            println("Module document path: ${moduleDocRef.path}")
            val snapshot = moduleDocRef.collection("tests").get().await()
            println("Tests snapshot size for module $moduleId: ${snapshot.size()}")
            snapshot.documents.map { document ->
                val test = document.toObject(Test::class.java)!!.copy(id = document.id)
                println("Loaded test: ${test.title} with id ${test.id}")
                test
            }
        } catch (e: Exception) {
            println("Error getting tests for module $moduleId: ${e.message}")
            emptyList()
        }
    }

}

