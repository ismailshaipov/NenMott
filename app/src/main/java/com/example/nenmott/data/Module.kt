package com.example.nenmott.data

data class Module(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val progress: Float = 0f,
    var tests: List<Test> = listOf(),
    val active: Boolean = false,
    val completed: Boolean = false
)
data class Test(
    val id: String = "",
    val title: String = "",
    val active: Boolean = false,
    val moduleId: String = "",
    val completed: Boolean = false,
    val xp: Int = 0,
    val coin: Int = 0,
    var questions: List<Question> = listOf()
)
data class Question(
    val id: String = "",
    val text: String = "",
    val soundUrl: String? = null,
    val imageUrl: String? = null,
    val correctAnswer: String = "",
    val options: List<String> = listOf(),
    val type: QuestionType = QuestionType.SELECT_IMAGE,
    val completed: Boolean = false
)
enum class QuestionType {
    SELECT_WORD,
    SELECT_IMAGE,
    COMPLETE_SENTENCE,
    FORM_SENTENCE,
    READ_PASSAGE
}
