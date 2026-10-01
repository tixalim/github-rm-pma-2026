package com.example.myapp02_dicerollcompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme{
                DiceApp()
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun DiceApp() {
    val diceSymbols = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")
    var diceValue by remember { mutableStateOf(value = 1) }
    var isRolling by remember { mutableStateOf(value = false) }
    val scope = rememberCoroutineScope ()

    val backgroundColor = Color(0xFFF5F3FF)
    val primaryColor = Color(0xFF352060)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .safeDrawingPadding()
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text= "Hod kostkou",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )
        Text(
            text= diceSymbols[diceValue - 1],
            fontSize = 126.sp,
            color = primaryColor,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        Button(
            enabled = !isRolling,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = Color.White
            ),
            onClick = {
                isRolling = true

                scope.launch {
                    repeat(times = 10) {
                        diceValue = (1..6).random()
                        delay(timeMillis = 250)
                    }
                    diceValue = (1..6).random()
                    isRolling = false
                }
            }

        ) {
            Text(
                text = "Hodit",
                fontSize = 26.sp
            )

        }
    }
}
