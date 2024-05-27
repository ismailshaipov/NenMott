package com.example.nenmott.screens.login

fun validateEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validatePassword(password: String): Boolean {
    return password.length >= 6
}