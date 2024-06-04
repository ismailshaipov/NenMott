package com.example.nenmott.screens.main.abc

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nenmott.R
import com.example.nenmott.viewmodels.UserProfileViewModel

@Composable
fun ABC(viewModel: UserProfileViewModel = viewModel()) {
    val chechenAlphabet = listOf(
        "А", "а", "Аь", "аь", "Б", "б", "В", "в", "Г", "г", "ГӀ", "гӀ",
        "Д", "д", "Е", "е", "Ё", "ё", "Ж", "ж", "З", "з", "И", "и", "Й", "й",
        "К", "к", "Кх", "кх", "Къ", "къ", "КӀ", "кӀ", "Л", "л", "М", "м", "Н", "н",
        "О", "о", "Оь", "оь", "П", "п", "ПӀ", "пӀ", "Р", "р", "С", "с", "Т", "т",
        "ТӀ", "тӀ", "У", "у", "Уь", "уь", "Ф", "ф", "Х", "х", "Хь", "хь", "ХӀ", "хӀ",
        "Ц", "ц", "ЦӀ", "цӀ", "Ч", "ч", "ЧӀ", "чӀ", "Ш", "ш", "Щ", "щ", "Ъ", "ъ",
        "Ы", "ы", "Ь", "ь", "Э", "э", "Ю", "ю", "Я", "я", "Ӏ", "ӏ"
    )
    val chechenAlphabetPairs = chechenAlphabet.chunked(2)
    var selectedLetter by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Text(text = "Абат", fontSize = 28.sp, modifier = Modifier.align(Alignment.CenterHorizontally))

        selectedLetter?.let {
            TranscriptionBox(letter = it, viewModel = viewModel) { selectedLetter = null }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(4)) {
            items(chechenAlphabetPairs) { pair ->
                AlphabetBox(
                    upperCase = pair[0],
                    lowerCase = pair.getOrElse(1) { "" },
                    onClick = { selectedLetter = pair[0] },
                    viewModel = viewModel
                )
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun AlphabetBox(
    upperCase: String,
    lowerCase: String,
    onClick: () -> Unit,
    viewModel: UserProfileViewModel,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    var isPressed by remember { mutableStateOf(false) }
    val elevation by animateDpAsState(if (isPressed) 4.dp else 0.dp, label = "")

    // Вспомогательное состояние для хранения результата проверки
    var isCompleted by remember { mutableStateOf(false) }


        isCompleted = viewModel.isTaskCompleted("task_$upperCase")


    val borderColor = if (isCompleted) Color.Green else Color.LightGray

    Box(
        modifier = Modifier
            .padding(5.dp)
            .size(80.dp)
            .border(2.dp, borderColor, shape)
            .shadow(elevation, shape)
            .background(Color.Cyan, shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    isPressed = true
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "$upperCase $lowerCase", fontSize = 24.sp)
        }
    }
}



@Composable
fun TranscriptionBox(letter: String, viewModel: UserProfileViewModel, onClose: () -> Unit) {
    var isCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(letter) {
        isCompleted = viewModel.isTaskCompleted("task_$letter")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Транскрипция для $letter", fontSize = 24.sp, modifier = Modifier.padding(bottom = 8.dp))
            IconButton(onClick = {
                viewModel.updateUserXPAndCoins(
                    xp = if (letter.length > 1) 2 else 1,
                    coin = if (letter.length > 1) 2 else 1,
                    taskId = "task_$letter"
                )
                isCompleted = true
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_volume_up_24),
                    contentDescription = "Play sound",
                    tint = Color.Green,
                    modifier = Modifier.size(48.dp)
                )
            }
            Button(onClick = onClose, modifier = Modifier.padding(top = 8.dp)) {
                Text("Закрыть")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ABCPREV() {
    ABC(viewModel = UserProfileViewModel())
}
