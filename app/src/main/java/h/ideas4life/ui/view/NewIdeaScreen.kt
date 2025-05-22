package h.ideas4life.ui.view

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import h.ideas4life.viewmodel.IdeaViewModel
import java.util.Locale

@Composable
fun NewIdeaScreen(viewModel: IdeaViewModel) {
    val context = LocalContext.current
    val maxRecordingDurationMillis = 10_000L
    val speechRecognizer = remember {
        SpeechRecognizer.createSpeechRecognizer(context)
    }
    var ideaText by remember { mutableStateOf("") }
    val aiResponse by viewModel.aiResponse.collectAsState()

    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
    }

    val isRecording = remember { mutableStateOf(false) }

    val recognitionListener = remember {
        object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                val message = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Error de audio"
                    SpeechRecognizer.ERROR_CLIENT -> "Error de cliente"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permiso insuficiente"
                    SpeechRecognizer.ERROR_NETWORK -> "Error de red"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No se encontró coincidencia"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconocedor ocupado"
                    SpeechRecognizer.ERROR_SERVER -> "Error del servidor"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Tiempo de espera agotado"
                    else -> "Error desconocido"
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ideaText = matches?.firstOrNull() ?: ""
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    val pulse by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val scale = if (isRecording.value) pulse else 1f

    LaunchedEffect(Unit) {
        speechRecognizer.setRecognitionListener(recognitionListener)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Escribe tu idea", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = ideaText,
            onValueChange = { ideaText = it },
            label = { Text("Idea original") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    viewModel.saveIdea(ideaText)
                    ideaText = ""
                },
                modifier = Modifier
            ) {
                Text("Guardar Idea")
            }

            Button(
                onClick = { viewModel.askAi(ideaText) },
                modifier = Modifier
            ) {
                Text("Enviar a IA")
            }
        }

        if (isRecording.value) {
            Text("Grabando...", color = Color.Red, fontSize = 14.sp)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isRecording.value = true
                                speechRecognizer.startListening(recognizerIntent)
                                tryAwaitRelease()
                                isRecording.value = false
                                speechRecognizer.stopListening()
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRecording.value) Icons.Default.Mic else Icons.Default.MicNone,
                    contentDescription = if (isRecording.value) "Grabando" else "Iniciar grabación",
                    tint = if (isRecording.value) Color.Red else Color.Black,
                    modifier = Modifier.size(64.dp).scale(scale)
                )
            }
        }

        if (aiResponse.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text("Respuesta IA:", fontSize = 16.sp)
            Text(aiResponse, fontSize = 14.sp)
        }

        Column {
            // Mostrar respuesta
            Text(
                text = aiResponse,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    val iconScale = remember { Animatable(1f) }

    LaunchedEffect(isRecording.value) {
        if (isRecording.value) {
            iconScale.snapTo(1f)
            iconScale.animateTo(
                targetValue = 2f, // Escalar hasta el doble de tamaño
                animationSpec = tween(durationMillis = maxRecordingDurationMillis.toInt())
            )
        } else {
            iconScale.snapTo(1f)
        }
    }
}