package com.example.nenmott.screens.main.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.nenmott.viewmodels.UserProfileViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun UserProfileScreen(
    navController: NavHostController,
    viewModel: UserProfileViewModel = viewModel()
) {
    val user = viewModel.user.collectAsState().value
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.30f)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "User Photo",
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            user?.let {
                Text(
                    text = "${it.name} ${it.surname}",
                    fontSize = 24.sp,
                    fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(it.nickname, fontSize = 16.sp, color = Color.Gray)

                val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                Text(
                    "Дата регистрации: ${dateFormatter.format(it.registrationDate)}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    thickness = 1.dp, color = Color.Gray
                )

                Text(
                    "Статистика",
                    fontSize = 20.sp,
                    fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatisticCard("Монеты", it.coins.toString())
                    StatisticCard("Рейтинг", it.rank.toString())
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Достижения",
                    fontSize = 20.sp,
                    fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Карта достижений
                AchievementsCard()

                Spacer(modifier = Modifier.height(25.dp))
                // Кнопка выхода из профиля
                Button(
                    onClick = {
                        viewModel.signOut()
                        navController.navigate("login") {
                            popUpTo("mainScreen") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.5f)
                ) {
                    Text("Выйти", fontSize = 22.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun StatisticCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Cyan
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(title, fontSize = 16.sp, fontWeight = MaterialTheme.typography.headlineMedium.fontWeight, color = MaterialTheme.colorScheme.primary)
            Text(value, fontSize = 24.sp, fontWeight = MaterialTheme.typography.headlineMedium.fontWeight, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun AchievementsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Cyan,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Star, contentDescription = "Achievement 1", tint = Color.Yellow)
                Icon(Icons.Filled.Star, contentDescription = "Achievement 2", tint = Color.Yellow)
                Icon(Icons.Filled.Star, contentDescription = "Achievement 3", tint = Color.Yellow)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
