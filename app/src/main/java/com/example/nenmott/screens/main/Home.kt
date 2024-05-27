package com.example.nenmott.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nenmott.R
import com.example.nenmott.data.User
import com.example.nenmott.viewmodels.UserProfileViewModel
import kotlin.random.Random

@Composable
fun HomeScreen(viewModel: UserProfileViewModel = viewModel()) {
    val user by viewModel.user.collectAsState()

    var showModuleSelection by remember { mutableStateOf(false) }
    var selectedModule by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TopBar(user, selectedModule != null) {
                if (selectedModule != null) {
                    selectedModule = null
                } else {
                    showModuleSelection = true
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (selectedModule == null) {
                    items((1..5).toList()) { moduleIndex ->
                        ModuleSection("Модуль $moduleIndex") {
                            selectedModule = "Модуль $moduleIndex"
                        }
                    }
                } else {
                    items((1..9).toList()) { sectionIndex ->
                        LessonItem(
                            lessonTitle = "Раздел $sectionIndex",
                            description = "Тест $sectionIndex",
                            offset = sectionIndex % 2 == 0
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showModuleSelection,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            ModuleSelectionCard(onDismiss = { showModuleSelection = false })
        }
    }
}

@Composable
fun TopBar(user: User?, showBackArrow: Boolean, onArrowClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (showBackArrow) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_keyboard_arrow_up_24),
                contentDescription = "Back",
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        onArrowClick()
                    }
            )
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
                contentDescription = "User Profile",
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        // Logic for user profile
                    }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            StatusItem(icon = R.drawable.ic_launcher_background, value = user?.xp.toString())
            Spacer(modifier = Modifier.width(8.dp))
            StatusItem(icon = R.drawable.ic_launcher_foreground, value = user?.coins.toString())
            Spacer(modifier = Modifier.width(8.dp))
            StatusItem(icon = R.drawable.ic_launcher_background, value = "∞")
        }

        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Module Selection",
            modifier = Modifier
                .size(48.dp)
                .clickable { onArrowClick() }
        )
    }
}

@Composable
fun StatusItem(icon: Int, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = value, fontSize = 16.sp, color = Color.Black)
    }
}

@Composable
fun ModuleSection(moduleTitle: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(text = moduleTitle, fontSize = 20.sp, color = Color.Green)
        Spacer(modifier = Modifier.height(8.dp))
        val testCount = remember { Random.nextInt(5, 9) }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            for (testIndex in 1..testCount) {
                LessonItem(
                    lessonTitle = "Раздел $testIndex",
                    description = "Тест $testIndex",
                    offset = testIndex % 2 == 0
                )
            }
        }
    }
}

@Composable
fun LessonItem(lessonTitle: String, description: String, offset: Boolean) {
    Box(
        modifier = Modifier
            .padding(start = if (offset) 32.dp else 0.dp, top = 8.dp)
            .size(80.dp)
            .background(Color.Green, shape = CircleShape)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Text(text = lessonTitle, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun ModuleSelectionCard(onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .clickable { onDismiss() },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Модуль 3: Дух странствий", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Раздел 2: Поговорите о языках", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(progress = 0.5f, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "5 из 10 заданий выполнено", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
    HomeScreen()
}
