package com.example.nenmott.screens.main.home

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.example.nenmott.data.Module
import com.example.nenmott.data.Test
import com.example.nenmott.data.User
import com.example.nenmott.viewmodels.TestViewModel
import com.example.nenmott.viewmodels.UserProfileViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(
    userVM: UserProfileViewModel = viewModel(),
    testVM: TestViewModel = viewModel()
) {
    val user by userVM.user.collectAsState()
    val selectedModule by userVM.selectedModule.collectAsState()
    val currentTest by testVM.currentTest.collectAsState()
    val currentQuestion by testVM.currentQuestion.collectAsState()
    var showModuleSelection by remember { mutableStateOf(false) }
    var showTest by remember { mutableStateOf(false) }
    val modules by testVM.modules.collectAsState()

    if (selectedModule == null && modules.isNotEmpty()) {
        userVM.selectModule(modules.firstOrNull { it.active } ?: modules.first())
    }

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
            TopBar(user, selectedModule?.title, onArrowClick = { showModuleSelection = true })

            Spacer(modifier = Modifier.height(16.dp))

            selectedModule?.let { module ->
                if (module.tests.isNotEmpty()) {
                    TestGrid(tests = module.tests, onTestSelected = {
                        testVM.startTest(it,module)
                        showTest = true
                    })
                } else {
                    Text(
                        text = "Нет доступных тестов",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = showModuleSelection,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            ModuleSelectionCard(modules, onModuleSelected = { module ->
                userVM.selectModule(module)
                showModuleSelection = false
            }, onDismiss = { showModuleSelection = false })
        }

        if (showTest) {
            currentTest?.let {
                currentQuestion?.let {
                    TestScreen(testVM, onCancel = {
                        showTest = false
                        testVM.resetTest() // Вызов функции сброса теста, если необходимо
                    })
                } ?: run {
                    // Показываем индикатор загрузки, если вопросы загружаются
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}









@Composable
fun TestGrid(tests: List<Test>, onTestSelected: (Test) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(tests) { test ->
            TestCard(test = test, onTestSelected = onTestSelected)
        }
    }
}


@Composable
fun TopBar(user: User?, selectedModuleTitle: String?, onArrowClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            StatusItem(icon = R.drawable.ic_launcher_background, value = user?.xp.toString())
            Spacer(modifier = Modifier.width(8.dp))
            StatusItem(icon = R.drawable.ic_launcher_foreground, value = user?.coins.toString())
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_keyboard_arrow_up_24),
                contentDescription = "Module Selection",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onArrowClick() }
            )
            Text(
                text = selectedModuleTitle ?: "Выберите модуль",
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
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
fun ModuleCard(module: Module, onModuleSelected: (Module) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(enabled = module.active) {
                onModuleSelected(module)
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (module.active) Color.Green else Color.Gray
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = module.title, fontSize = 20.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = module.description, fontSize = 14.sp, color = Color.White)
        }
    }
}

@Composable
fun ModuleSelectionCard(modules: List<Module>, onModuleSelected: (Module) -> Unit, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable { /* Do nothing */ },
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_close_24),
                        contentDescription = "Close Module Selection",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onDismiss() }
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(modules) { module ->
                        ModuleCard(module, onModuleSelected)
                    }
                }
            }
        }
    }
}

@Composable
fun TestCard(test: Test, onTestSelected: (Test) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(enabled = test.active) {
                onTestSelected(test)
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (test.active) Color.Green else Color.Gray
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = test.title, fontSize = 20.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(
        userVM = viewModel(),
        testVM = viewModel(),
    )
}