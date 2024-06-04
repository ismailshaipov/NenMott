package com.example.nenmott.screens.main.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.nenmott.R
import com.example.nenmott.data.Question
import com.example.nenmott.data.QuestionType
import com.example.nenmott.viewmodels.TestViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TestScreen(viewModel: TestViewModel = viewModel(), onCancel: () -> Unit) {
    val currentQuestion by viewModel.currentQuestion.collectAsState(initial = null)
    val progress by viewModel.progress.collectAsState(initial = 0f)
    val totalXp by viewModel.totalXp.collectAsState(initial = 0)
    val totalCoins by viewModel.totalCoins.collectAsState(initial = 0)
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { onCancel() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel Test")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(progress = progress, modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(CircleShape))

            currentQuestion?.let { question ->
                when (question.type) {
                    QuestionType.SELECT_WORD -> SelectWordQuestion(question, selectedAnswer, viewModel)
                    QuestionType.SELECT_IMAGE -> SelectImageQuestion(question, selectedAnswer, viewModel)
                    QuestionType.COMPLETE_SENTENCE -> CompleteSentenceQuestion(question, selectedAnswer, viewModel)
                    QuestionType.FORM_SENTENCE -> FormSentenceQuestion(question, selectedAnswer, viewModel)
                    QuestionType.READ_PASSAGE -> ReadPassageQuestion(question, viewModel)
                }

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        viewModel.submitAnswer()
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Проверить")
                }
            } ?: run {
                CompletionScreen(totalXp, totalCoins)
            }
        }
    }
}

@Composable
fun SelectWordQuestion(question: Question, selectedAnswer: String?, viewModel: TestViewModel) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(R.drawable.ic_launcher_foreground), // Замените на реальный ресурс
            contentDescription = "Play word",
            modifier = Modifier
                .size(100.dp)
                .clickable {
                    viewModel.playSound(question.soundUrl, context)
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        question.options.forEach { option ->
            Button(
                onClick = { viewModel.selectAnswer(option) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(
                        if (selectedAnswer == option) Color.LightGray else Color.White,
                        RoundedCornerShape(8.dp)
                    )
            ) {
                Text(text = option, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun SelectImageQuestion(question: Question, selectedAnswer: String?, viewModel: TestViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = question.text, fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

        val painter = rememberAsyncImagePainter(model = question.imageUrl)
        Image(painter = painter, contentDescription = null, modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.height(16.dp))

        question.options.forEach { option ->
            Button(
                onClick = { viewModel.selectAnswer(option) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(
                        if (selectedAnswer == option) Color.Green else Color.White,
                        RoundedCornerShape(8.dp)
                    )
            ) {
                Text(text = option, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun CompleteSentenceQuestion(question: Question, selectedAnswer: String?, viewModel: TestViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = question.text, fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

        question.options.forEach { option ->
            Button(
                onClick = { viewModel.selectAnswer(option) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(
                        if (selectedAnswer == option) Color.LightGray else Color.Blue,
                        RoundedCornerShape(8.dp)
                    )
            ) {
                Text(text = option, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun FormSentenceQuestion(question: Question, selectedAnswer: String?, viewModel: TestViewModel) {
    // TODO: Implement drag and drop functionality for forming sentences
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = question.text, fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Display the selected words and allow dragging and dropping to form a sentence
    }
}

@Composable
fun ReadPassageQuestion(question: Question, viewModel: TestViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = question.text, fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.markAsRead(question.id) }) {
            Text("Прочтено")
        }
    }
}

@Composable
fun CompletionScreen(totalXp: Int, totalCoins: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Поздравляем! Вы завершили тест.")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Полученные награды:")
        Spacer(modifier = Modifier.height(8.dp))
        Text("XP: $totalXp")
        Text("Монеты: $totalCoins")
    }
}

@Composable
@Preview
fun pre() {
    TestScreen(onCancel = {})
}

