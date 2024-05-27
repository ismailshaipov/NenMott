package com.example.nenmott.data

data class Module(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val progress: Float = 0f,
    val tests: List<Test> = listOf(),
    val active: Boolean = false
)


data class Test(
    val id: String = "",
    val title: String = "",
    val questions: List<Question> = listOf(),
    val active: Boolean = false
)

data class Question(
    val id: String = "",
    val text: String = "",
    val soundUrl: String = "",
    val imageUrl: String = "",
    val correctAnswer: String = "",
    val options: List<String> = listOf()
)