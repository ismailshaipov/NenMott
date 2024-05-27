package com.example.nenmott.data

import java.util.Date

data class User(
    val id: String = "",
    val name: String = "",
    val surname: String = "",
    val nickname: String = "",
    val registrationDate: Date = Date(),
    val level: Int = 1,
    val xp: Int = 0,
    val coins: Int = 0,
    val completedTasks: List<String> = emptyList(),
    val rank: Int? = null
)
