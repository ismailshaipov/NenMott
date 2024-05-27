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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.compose.rememberNavController
import com.example.nenmott.data.User
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegScreen(
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var registrationError by remember { mutableStateOf<String?>(null) }
    var isFormValid by remember { mutableStateOf(false) }

    fun updateFormValidity() {
        isFormValid = validateEmail(email) && validatePassword(password) && name.isNotBlank() && surname.isNotBlank() && nickname.isNotBlank()
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
                        registrationError = null
                        updateFormValidity()
                    },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = emailError != null,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White
                    )
                )
                if (emailError != null) {
                    Text(text = emailError!!, color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                        updateFormValidity()
                    },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = surname,
                    onValueChange = {
                        surname = it
                        updateFormValidity()
                    },
                    label = { Text("Surname") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = nickname,
                    onValueChange = {
                        nickname = it
                        updateFormValidity()
                    },
                    label = { Text("Nickname") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = if (validatePassword(it)) null else "Пароль должен быть не менее 6 символов"
                        registrationError = null
                        updateFormValidity()
                    },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = passwordError != null,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White
                    )
                )
                if (passwordError != null) {
                    Text(text = passwordError!!, color = MaterialTheme.colorScheme.error)
                }
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                createUserWithEmailAndPassword(email, password, name, surname, nickname)
                                navController.navigate("login")
                                Log.d(ContentValues.TAG, "CreateUser:success")
                            } catch (e: FirebaseAuthWeakPasswordException) {
                                registrationError = "Пароль слишком слабый."
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                registrationError = "Неправильный формат email."
                            } catch (e: FirebaseAuthUserCollisionException) {
                                registrationError = "Этот email уже используется."
                            } catch (e: FirebaseNetworkException) {
                                registrationError = "Проблемы с сетью. Пожалуйста, попробуйте позже."
                            } catch (e: FirebaseTooManyRequestsException) {
                                registrationError = "Слишком много попыток. Пожалуйста, попробуйте позже."
                            } catch (e: Exception) {
                                registrationError = "Неизвестная ошибка: ${e.localizedMessage}"
                            }
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Register")
                }
                if (registrationError != null) {
                    Text(
                        text = registrationError!!,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

suspend fun createUserWithEmailAndPassword(email: String, password: String, name: String, surname: String, nickname: String): String {
    return suspendCoroutine { continuation ->
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User(
                        id = task.result?.user?.uid ?: "",
                        name = name,
                        surname = surname,
                        nickname = nickname,
                        registrationDate = Date(),
                        level = 1,
                        xp = 0,
                        coins = 0
                    )
                    saveUserToFirestore(user)
                    continuation.resume(user.id)
                } else {
                    continuation.resumeWithException(task.exception ?: Exception("Unknown error"))
                }
            }
    }
}

fun saveUserToFirestore(user: User) {
    val firestore = FirebaseFirestore.getInstance()
    val userDocument = firestore.collection("users").document(user.id)
    userDocument.set(user)
        .addOnSuccessListener {
            Log.d(ContentValues.TAG, "User successfully written!")
        }
        .addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error writing document", e)
        }
}

@Composable
@Preview
fun RegPreview() {
    val navController = rememberNavController()
    RegScreen(navController = navController)
}
