package com.example.myapp02_dicerollcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp02_dicerollcompose.ui.theme.AppAccent
import com.example.myapp02_dicerollcompose.ui.theme.AppBackground
import com.example.myapp02_dicerollcompose.ui.theme.AppDisabledBg
import com.example.myapp02_dicerollcompose.ui.theme.AppDisabledText
import com.example.myapp02_dicerollcompose.ui.theme.AppPrimary
import com.example.myapp02_dicerollcompose.ui.theme.AppTextDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                // Vypnutí "ripple" animace (vlna po kliknutí) pro celou aplikaci
                CompositionLocalProvider(LocalRippleConfiguration provides null) {
                    DiceApp()
                }
            }
        }
    }
}

// Fáze hry – podle ní se rozhoduje, co je na obrazovce vidět
enum class Phase { START, BETTING, CONFIRMED, GAME_OVER }

const val START_POINTS = 200
val BET_OPTIONS = listOf(20, 50, 100, 200)

@Composable
fun DiceApp() {
    val diceSymbols = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")

    // Stav hry – rememberSaveable přežije otočení telefonu
    var phase by rememberSaveable { mutableStateOf(Phase.START) }
    var points by rememberSaveable { mutableStateOf(START_POINTS) }
    var diceValue by rememberSaveable { mutableStateOf(1) }
    var resultText by rememberSaveable { mutableStateOf("") }
    var betOnEven by rememberSaveable { mutableStateOf<Boolean?>(null) } // null = nevybráno
    var selectedBet by rememberSaveable { mutableStateOf(0) }            // 0 = nevybráno

    // Stav jen pro animaci kostky (při otočení se nemusí ukládat)
    var isRolling by remember { mutableStateOf(false) }
    var rollingFace by remember { mutableStateOf(1) }
    var pointsBeforeRoll by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    fun startNewGame() {
        points = START_POINTS
        resultText = ""
        betOnEven = null
        selectedBet = 0
        phase = Phase.BETTING
    }

    // Během animace se ještě ukazuje stav před hodem
    val shownPhase = if (isRolling) Phase.CONFIRMED else phase
    val shownPoints = if (isRolling) pointsBeforeRoll else points
    val shownDice = if (isRolling) rollingFace else diceValue
    val choicesEnabled = shownPhase == Phase.BETTING

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .safeDrawingPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(all = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hod kostkou",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = AppTextDark
            )

            // Body: zobrazí se až po stisknutí Hrát
            if (shownPhase != Phase.START) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(
                        text = "Body: $shownPoints",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppPrimary,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }
            }

            // Úvodní tlačítko
            if (shownPhase == Phase.START) {
                MainButton(
                    text = "Hrát",
                    color = AppPrimary,
                    textColor = Color.White,
                    onClick = { startNewGame() },
                    modifier = Modifier.padding(top = 32.dp)
                )
            }

            // Sázení
            if (shownPhase == Phase.BETTING || shownPhase == Phase.CONFIRMED) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)) {
                    Text(text = "Na co sázíš?", fontSize = 16.sp, color = AppTextDark)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OptionButton(
                            text = "Sudé",
                            selected = betOnEven == true,
                            enabled = choicesEnabled,
                            onClick = { betOnEven = true },
                            modifier = Modifier.weight(1f)
                        )
                        OptionButton(
                            text = "Liché",
                            selected = betOnEven == false,
                            enabled = choicesEnabled,
                            onClick = { betOnEven = false },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Text(
                        text = "Kolik vsadíš?",
                        fontSize = 16.sp,
                        color = AppTextDark,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (bet in BET_OPTIONS) {
                            // Sázky vyšší než aktuální body jsou vypnuté
                            OptionButton(
                                text = "$bet",
                                selected = selectedBet == bet,
                                enabled = choicesEnabled && bet <= shownPoints,
                                onClick = { selectedBet = bet },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    if (shownPhase == Phase.BETTING) {
                        MainButton(
                            text = "Potvrdit sázku",
                            color = AppPrimary,
                            textColor = Color.White,
                            enabled = betOnEven != null && selectedBet != 0,
                            onClick = { phase = Phase.CONFIRMED },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        )
                    }
                }
            }

            // Kostka: po potvrzení, nebo když už se házelo
            val showDice = shownPhase == Phase.CONFIRMED ||
                    shownPhase == Phase.GAME_OVER ||
                    (shownPhase == Phase.BETTING && resultText.isNotEmpty())
            if (showDice) {
                Text(
                    text = diceSymbols[shownDice - 1],
                    fontSize = 120.sp,
                    color = AppTextDark,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            if (shownPhase == Phase.CONFIRMED) {
                MainButton(
                    text = "Hodit",
                    color = AppAccent,
                    textColor = AppTextDark,
                    enabled = !isRolling,
                    onClick = {
                        // Výsledek určíme hned na začátku, animace je jen vizuální.
                        // Díky tomu nejde výsledek "zrušit" otočením telefonu během animace.
                        val bet = selectedBet
                        val rolled = (1..6).random()
                        val isEven = rolled % 2 == 0
                        val won = isEven == betOnEven

                        pointsBeforeRoll = points
                        isRolling = true

                        diceValue = rolled
                        points += if (won) bet else -bet
                        val parityText = if (isEven) "sudé" else "liché"
                        resultText = if (won) {
                            "Padlo $rolled ($parityText). Výhra +$bet bodů!"
                        } else {
                            "Padlo $rolled ($parityText). Prohra −$bet bodů."
                        }
                        // Pokud na vybranou sázku už nejsou body, výběr zrušíme
                        if (selectedBet > points) selectedBet = 0
                        phase = if (points <= 0) Phase.GAME_OVER else Phase.BETTING

                        scope.launch {
                            repeat(times = 10) {
                                rollingFace = (1..6).random()
                                delay(timeMillis = 250)
                            }
                            isRolling = false
                        }
                    }
                )
            }

            // Výsledek hodu
            if ((shownPhase == Phase.BETTING || shownPhase == Phase.GAME_OVER) && resultText.isNotEmpty()) {
                val text = if (shownPhase == Phase.GAME_OVER) {
                    "$resultText\nDošly ti body, konec hry."
                } else {
                    resultText
                }
                Text(
                    text = text,
                    fontSize = 18.sp,
                    color = AppTextDark,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // Restart po prohře
            if (shownPhase == Phase.GAME_OVER) {
                MainButton(
                    text = "Hrát znovu",
                    color = AppPrimary,
                    textColor = Color.White,
                    onClick = { startNewGame() },
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

// Hlavní tlačítko (Hrát, Potvrdit sázku, Hodit, Hrát znovu)
@Composable
fun MainButton(
    text: String,
    color: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = textColor,
            disabledContainerColor = AppDisabledBg,
            disabledContentColor = AppDisabledText
        ),
        contentPadding = PaddingValues(horizontal = 40.dp, vertical = 12.dp),
        modifier = modifier
    ) {
        Text(text = text, fontSize = 22.sp)
    }
}

// Volitelné tlačítko (Sudé/Liché, výše sázky)
@Composable
fun OptionButton(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) AppPrimary else Color.White,
            contentColor = if (selected) Color.White else AppPrimary,
            disabledContainerColor = AppDisabledBg,
            disabledContentColor = AppDisabledText
        ),
        border = BorderStroke(1.dp, if (enabled) AppPrimary else AppDisabledBg),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
        modifier = modifier
    ) {
        Text(text = text, fontSize = 16.sp)
    }
}