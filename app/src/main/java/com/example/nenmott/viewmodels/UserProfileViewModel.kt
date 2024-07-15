package com.example.nenmott.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nenmott.data.Module
import com.example.nenmott.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _selectedModule = MutableStateFlow<Module?>(null)
    val selectedModule: StateFlow<Module?> get() = _selectedModule

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    init {
        fetchUserData()
    }

    fun fetchUserData() {
        viewModelScope.launch {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid
                firestore.collection("users").document(userId).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val fetchedUser = document.toObject(User::class.java)
                            _user.value = fetchedUser
                            _isLoggedIn.value = true
                            fetchUserRank()  // Вызов функции для получения рейтинга после загрузки данных пользователя
                        }
                    }
                    .addOnFailureListener {
                        // Обработка ошибок
                    }
            }
        }
    }

    fun updateUserXPAndCoins(xp: Int, coin: Int, taskId: String) {
        viewModelScope.launch {
            val currentUser = auth.currentUser
            currentUser?.let {
                val userId = it.uid
                val userRef = firestore.collection("users").document(userId)
                _user.value?.let { user ->
                    if (!user.completedTasks.contains(taskId)) {
                        val updatedTasks = user.completedTasks + taskId
                        val newUser = user.copy(
                            xp = user.xp + xp,
                            coins = user.coins + coin,
                            completedTasks = updatedTasks
                        )
                        userRef.set(newUser)
                            .addOnSuccessListener {
                                _user.value = newUser
                                fetchUserRank()  // Пересчитайте рейтинг после обновления XP и монет
                            }
                            .addOnFailureListener {
                                // Обработка ошибки
                            }
                    }
                }
            }
        }
    }

    private fun fetchUserRank() {
        viewModelScope.launch {
            val currentUser = _user.value
            if (currentUser != null) {
                firestore.collection("users")
                    .whereGreaterThan("xp", currentUser.xp)
                    .get()
                    .addOnSuccessListener { result ->
                        val higherXpUsersCount = result.size()
                        val rank = higherXpUsersCount + 1
                        _user.value = currentUser.copy(rank = rank)
                    }
                    .addOnFailureListener {
                        // Обработка ошибок
                    }
            }
        }
    }

    fun selectModule(module: Module) {
        _selectedModule.value = module
    }

    fun isTaskCompleted(taskId: String): Boolean {
        return _user.value?.completedTasks?.contains(taskId) ?: false
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
        _isLoggedIn.value = false
    }

    fun setLoginState(isLoggedIn: Boolean) {
        _isLoggedIn.value = isLoggedIn
    }
}
