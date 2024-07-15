package com.example.nenmott.screens.login

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nenmott.viewmodels.UserProfileViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Composable
fun LoginScreen(
    navController: NavController,
    userProfileViewModel: UserProfileViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var isFormValid by remember { mutableStateOf(false) }

    fun updateFormValidity() {
        isFormValid = validateEmail(email) && validatePassword(password)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            elevation = CardDefaults.cardElevation(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Cyan,
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = if (validateEmail(it)) null else "Неправильный формат email"
                        loginError = null
                        updateFormValidity()
                    },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = emailError != null,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                    )
                )
                if (emailError != null) {
                    Text(text = emailError!!, color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = if (validatePassword(it)) null else "Пароль должен быть не менее 6 символов"
                        loginError = null
                        updateFormValidity()
                    },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = passwordError != null,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                    )
                )
                if (passwordError != null) {
                    Text(text = passwordError!!, color = MaterialTheme.colorScheme.error)
                }
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                signInWithEmailAndPassword(email, password)
                                userProfileViewModel.setLoginState(true)
                                navController.navigate("mainScreen"){
                                    popUpTo("login") { inclusive = true }
                                }
                                Log.d(ContentValues.TAG, "SignIn:success")
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                loginError = "Неправильный email или пароль."
                            } catch (e: FirebaseNetworkException) {
                                loginError = "Проблемы с сетью. Пожалуйста, попробуйте позже."
                            } catch (e: FirebaseTooManyRequestsException) {
                                loginError = "Слишком много попыток. Пожалуйста, попробуйте позже."
                            } catch (e: Exception) {
                                loginError = "Неизвестная ошибка: ${e.localizedMessage}"
                            }
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Login")
                }
                if (loginError != null) {
                    Text(
                        text = loginError!!,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("register") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }
            }
        }
    }
}

suspend fun signInWithEmailAndPassword(email: String, password: String) {
    return suspendCoroutine { continuation ->
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(task.exception ?: Exception("Unknown error"))
                }
            }
    }
}

@Composable
fun LoginScreen2(

) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var isFormValid by remember { mutableStateOf(false) }

    fun updateFormValidity() {
        isFormValid = validateEmail(email) && validatePassword(password)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            elevation = CardDefaults.cardElevation(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Cyan,
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = if (validateEmail(it)) null else "Неправильный формат email"
                        loginError = null
                        updateFormValidity()
                    },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = emailError != null,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                    )
                )
                if (emailError != null) {
                    Text(text = emailError!!, color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = if (validatePassword(it)) null else "Пароль должен быть не менее 6 символов"
                        loginError = null
                        updateFormValidity()
                    },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = passwordError != null,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                    )
                )
                if (passwordError != null) {
                    Text(text = passwordError!!, color = MaterialTheme.colorScheme.error)
                }
                Button(
                    onClick = {

                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Login")
                }
                if (loginError != null) {
                    Text(
                        text = loginError!!,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview(){
    LoginScreen2()
}
