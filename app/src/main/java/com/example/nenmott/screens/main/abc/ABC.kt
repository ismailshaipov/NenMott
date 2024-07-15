package com.example.nenmott.screens.main.abc

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nenmott.R
import com.example.nenmott.viewmodels.UserProfileViewModel

val chechenAlphabet = mapOf(
    "А" to "/ə/ /ɑː/", "а" to "/ə/ /ɑː/", "Аь" to "/æ/", "аь" to "/æ/",
    "Б" to "/b/", "б" to "/b/", "В" to "/v/", "в" to "/v/",
    "Г" to "/ɡ/", "г" to "/ɡ/", "ГӀ" to "/ɣ/", "гӀ" to "/ɣ/",
    "Д" to "/d/", "д" to "/d/", "Е" to "/e/", "е" to "/e/",
    "Ё" to "/jo/", "ё" to "/jo/", "Ж" to "/ʒ/", "ж" to "/ʒ/",
    "З" to "/z/", "з" to "/z/", "И" to "/i/ /iː/", "и" to "/i/ /iː/",
    "Й" to "/j/", "й" to "/j/", "К" to "/k/", "к" to "/k/",
    "Кх" to "/q/", "кх" to "/q/", "Къ" to "/qʼ/", "къ" to "/qʼ/",
    "КӀ" to "/kʼ/", "кӀ" to "/kʼ/", "Л" to "/l/", "л" to "/l/",
    "М" to "/m/", "м" to "/m/", "Н" to "/n/", "н" to "/n/",
    "О" to "/o/ /ɔː/ /wo/ /ɯo/", "о" to "/o/ /ɔː/ /wo/ /ɯo/", "Оь" to "/ɵ/", "оь" to "/ɵ/",
    "П" to "/p/", "п" to "/p/", "ПӀ" to "/pʼ/", "пӀ" to "/pʼ/",
    "Р" to "/r/", "р" to "/r/", "С" to "/s/", "с" to "/s/",
    "Т" to "/t/", "т" to "/t/", "ТӀ" to "/tʼ/", "тӀ" to "/tʼ/",
    "У" to "/u/ /ɯz/", "у" to "/u/ /ɯz/", "Уь" to "/y/", "уь" to "/y/",
    "Ф" to "/f/", "ф" to "/f/", "Х" to "/x/", "х" to "/x/",
    "Хь" to "/xʼ/", "хь" to "/xʼ/", "ХӀ" to "/h/", "хӀ" to "/h/",
    "Ц" to "/ts/", "ц" to "/ts/", "ЦӀ" to "/tsʼ/", "цӀ" to "/tsʼ/",
    "Ч" to "/tʃ/", "ч" to "/tʃ/", "ЧӀ" to "/tʃʼ/", "чӀ" to "/tʃʼ/",
    "Ш" to "/ʃ/", "ш" to "/ʃ/", "Щ" to "/ɕtɕ/", "щ" to "/ɕtɕ/",
    "Ъ" to "ʡ", "ъ" to "ʡ", "Ы" to "/ɨ/", "ы" to "/ɨ/",
    "Ь" to "kleda", "ь" to "kleda", "Э" to "/e/", "э" to "/e/",
    "Ю" to "/ju/", "ю" to "/ju/", "Я" to "/ja/", "я" to "/ja/",
    "Ӏ" to "ʡ"
)
private val mediaPlayer = MediaPlayer()

@Composable
fun ABC(viewModel: UserProfileViewModel = viewModel()) {
    val chechenAlphabetPairs = chechenAlphabet.keys.chunked(2)
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    var showTeory by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Row(modifier = Modifier.align(CenterHorizontally), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Абат",
                fontSize = 28.sp
            )
            IconButton(onClick = { showTeory = true }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_info_24),
                    contentDescription = "Show Theory"
                )
            }
        }
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

    if (showTeory) {
        TheoryCard(onDismiss = { showTeory = false })
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
    var isCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(upperCase) {
        isCompleted = viewModel.isTaskCompleted("task_$upperCase")
    }

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
            horizontalAlignment = CenterHorizontally,
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
    val context = LocalContext.current

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
        Column(horizontalAlignment = CenterHorizontally) {
            Text(text = "Транскрипция для $letter", fontSize = 24.sp, modifier = Modifier.padding(bottom = 8.dp))
            Text(text = chechenAlphabet[letter] ?: "Неизвестно", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
            IconButton(onClick = {

                playLetterSound(context, letter)
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
fun playLetterSound(context: Context, letter: String) {
    val resId = when (letter) {
//            "а" -> R.raw.a
//            "б" -> R.raw.b
//            "в" -> R.raw.v
        // Add cases for all other letters
        else -> null
    }

    resId?.let {
        try {
            mediaPlayer.reset()
            val afd = context.resources.openRawResourceFd(it)
            mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } ?: run {
        println("No sound file found for letter: $letter")
    }
}
@Composable
fun TheoryCard(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xB3000000))
            .padding(10.dp)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Основная теория", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    item {
                        Text(text = "Чтение и письмо", style = MaterialTheme.typography.headlineMedium)
                        Text(text = "В чеченском языке между написанным и устным воспроизведением текста почти нет расхождения: что написано, то и читается. Ударение постоянное, всегда стоит на первом слоге.")

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Чеченская азбука", style = MaterialTheme.typography.headlineMedium)
                        Text(text = "Чеченский язык включает специфические согласные фонемы: пІ, тІ, кІ, цІ, чІ. Эти звуки произносятся с напряжением и имеют характерное энергичное звучание.")

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "О буквах и звуках", style = MaterialTheme.typography.headlineMedium)
                        Text(text = "Специфические звуки, такие как в, І, гІ, хь, отсутствуют в русском языке. Например, звук в произносится тверже, чем в русском, и иногда превращается в лабиализованный дифтонг.")

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Аффрикаты", style = MaterialTheme.typography.headlineMedium)
                        Text(text = "Чеченские аффрикаты - сложно-слитные согласные звуки, такие как дж, дз, кх, и ъ. Они произносятся твердо, не смягчаются и не озвончаются перед согласными.")
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ABCPREV() {
    ABC(viewModel = UserProfileViewModel())
}