package com.example.myapp01_diceroll_xml

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Fáze hry – podle ní se rozhoduje, co je na obrazovce vidět
    private enum class Phase { START, BETTING, CONFIRMED, GAME_OVER }

    private val diceSymbols = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")
    private val startPoints = 200

    // Stav hry (ukládá se při otočení telefonu)
    private var phase = Phase.START
    private var points = startPoints
    private var diceValue = 1
    private var resultText = ""

    // Barvy
    private val colorPrimary = Color.parseColor("#848EBF")
    private val colorAccent = Color.parseColor("#8EBF84")
    private val colorDisabledBg = Color.parseColor("#C9CCDE")
    private val colorDisabledText = Color.parseColor("#9A9EB5")

    // Prvky z layoutu
    private lateinit var cardPoints: View
    private lateinit var tvPoints: TextView
    private lateinit var tvDice: TextView
    private lateinit var tvResult: TextView
    private lateinit var btnPlay: MaterialButton
    private lateinit var btnConfirm: MaterialButton
    private lateinit var btnRoll: MaterialButton
    private lateinit var btnRestart: MaterialButton
    private lateinit var llBetting: LinearLayout
    private lateinit var tgParity: MaterialButtonToggleGroup
    private lateinit var tgBet: MaterialButtonToggleGroup

    // Tlačítka sázek a jejich hodnoty
    private lateinit var betButtons: Map<Int, Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Insety dáváme na ScrollView, aby se nepřepsal padding 24dp u llMain
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.svRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cardPoints = findViewById(R.id.cardPoints)
        tvPoints = findViewById(R.id.tvPoints)
        tvDice = findViewById(R.id.tvDice)
        tvResult = findViewById(R.id.tvResult)
        btnPlay = findViewById(R.id.btnPlay)
        btnConfirm = findViewById(R.id.btnConfirm)
        btnRoll = findViewById(R.id.btnRoll)
        btnRestart = findViewById(R.id.btnRestart)
        llBetting = findViewById(R.id.llBetting)
        tgParity = findViewById(R.id.tgParity)
        tgBet = findViewById(R.id.tgBet)

        betButtons = mapOf(
            R.id.btnBet20 to 20,
            R.id.btnBet50 to 50,
            R.id.btnBet100 to 100,
            R.id.btnBet200 to 200
        )

        setupColors()

        // Obnovení stavu po otočení telefonu
        if (savedInstanceState != null) {
            phase = Phase.valueOf(savedInstanceState.getString(KEY_PHASE, Phase.START.name))
            points = savedInstanceState.getInt(KEY_POINTS, startPoints)
            diceValue = savedInstanceState.getInt(KEY_DICE, 1)
            resultText = savedInstanceState.getString(KEY_RESULT, "")
            val parityId = savedInstanceState.getInt(KEY_PARITY, View.NO_ID)
            val betId = savedInstanceState.getInt(KEY_BET, View.NO_ID)
            if (parityId != View.NO_ID) tgParity.check(parityId)
            if (betId != View.NO_ID) tgBet.check(betId)
        }

        // Při změně výběru zkontrolujeme, jestli jde potvrdit
        tgParity.addOnButtonCheckedListener { _, _, _ -> updateConfirmButton() }
        tgBet.addOnButtonCheckedListener { _, _, _ -> updateConfirmButton() }

        btnPlay.setOnClickListener { startNewGame() }
        btnRestart.setOnClickListener { startNewGame() }

        btnConfirm.setOnClickListener {
            phase = Phase.CONFIRMED
            render()
        }

        btnRoll.setOnClickListener {
            val bet = selectedBet()
            val betOnEven = tgParity.checkedButtonId == R.id.btnEven

            // Výsledek určíme hned na začátku, animace je jen vizuální.
            // Díky tomu nejde výsledek "zrušit" otočením telefonu během animace.
            val rolled = (1..6).random()
            val isEven = rolled % 2 == 0
            val won = isEven == betOnEven

            diceValue = rolled
            points += if (won) bet else -bet
            val parityText = if (isEven) "sudé" else "liché"
            resultText = if (won) {
                "Padlo $rolled ($parityText). Výhra +$bet bodů!"
            } else {
                "Padlo $rolled ($parityText). Prohra −$bet bodů."
            }
            phase = if (points <= 0) Phase.GAME_OVER else Phase.BETTING

            lifecycleScope.launch {
                btnRoll.isEnabled = false
                repeat(times = 10) {
                    tvDice.text = diceSymbols.random()
                    delay(timeMillis = 250)
                }
                render()
            }
        }

        render()
    }
    // Nová hra: body zpět na začátek, výběr se vymaže
    private fun startNewGame() {
        points = startPoints
        resultText = ""
        tgParity.clearChecked()
        tgBet.clearChecked()
        phase = Phase.BETTING
        render()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PHASE, phase.name)
        outState.putInt(KEY_POINTS, points)
        outState.putInt(KEY_DICE, diceValue)
        outState.putString(KEY_RESULT, resultText)
        outState.putInt(KEY_PARITY, tgParity.checkedButtonId)
        outState.putInt(KEY_BET, tgBet.checkedButtonId)
    }

    // Vykreslí obrazovku podle aktuální fáze hry
    private fun render() {
        tvPoints.text = "Body: $points"
        tvDice.text = diceSymbols[diceValue - 1]
        tvResult.text = resultText
        btnRoll.isEnabled = true
        btnRestart.visibility = View.GONE

        when (phase) {
            Phase.START -> {
                cardPoints.visibility = View.GONE
                btnPlay.visibility = View.VISIBLE
                llBetting.visibility = View.GONE
                tvDice.visibility = View.GONE
                btnRoll.visibility = View.GONE
                tvResult.visibility = View.GONE
            }

            Phase.BETTING -> {
                cardPoints.visibility = View.VISIBLE
                btnPlay.visibility = View.GONE
                llBetting.visibility = View.VISIBLE
                btnConfirm.visibility = View.VISIBLE
                setChoicesEnabled(true)
                // Kostku a výsledek ukážeme až po prvním hodu
                val hasRolled = resultText.isNotEmpty()
                tvDice.visibility = if (hasRolled) View.VISIBLE else View.GONE
                tvResult.visibility = if (hasRolled) View.VISIBLE else View.GONE
                btnRoll.visibility = View.GONE
            }

            Phase.CONFIRMED -> {
                cardPoints.visibility = View.VISIBLE
                btnPlay.visibility = View.GONE
                llBetting.visibility = View.VISIBLE
                btnConfirm.visibility = View.GONE
                setChoicesEnabled(false) // sázka je zamčená
                tvDice.visibility = View.VISIBLE
                btnRoll.visibility = View.VISIBLE
                tvResult.visibility = View.GONE
            }

            Phase.GAME_OVER -> {
                cardPoints.visibility = View.VISIBLE
                llBetting.visibility = View.GONE
                tvDice.visibility = View.VISIBLE
                btnRoll.visibility = View.GONE
                tvResult.text = "$resultText\nDošly ti body, konec hry."
                tvResult.visibility = View.VISIBLE
                btnPlay.visibility = View.GONE
                btnRestart.visibility = View.VISIBLE
            }
        }
        updateConfirmButton()
    }

    // Zapne/vypne výběr sázky. Sázky vyšší než aktuální body jsou vždy vypnuté.
    private fun setChoicesEnabled(enabled: Boolean) {
        for (i in 0 until tgParity.childCount) {
            tgParity.getChildAt(i).isEnabled = enabled
        }
        for ((id, value) in betButtons) {
            findViewById<View>(id).isEnabled = enabled && value <= points
        }
        // Pokud je vybraná sázka, na kterou už hráč nemá, výběr zrušíme
        if (selectedBet() > points) {
            tgBet.clearChecked()
        }
    }

    // Barvy, které se mění podle stavu tlačítka (vybráno / vypnuto).
    // V XML by na to byl potřeba samostatný soubor, proto se nastavují tady.
    private fun setupColors() {
        val disabled = intArrayOf(-android.R.attr.state_enabled)
        val checked = intArrayOf(android.R.attr.state_checked)
        val other = intArrayOf()

        val toggleBg = ColorStateList(
            arrayOf(disabled, checked, other),
            intArrayOf(colorDisabledBg, colorPrimary, Color.WHITE)
        )
        val toggleText = ColorStateList(
            arrayOf(disabled, checked, other),
            intArrayOf(colorDisabledText, Color.WHITE, colorPrimary)
        )
        val toggleButtons = (listOf(R.id.btnEven, R.id.btnOdd) + betButtons.keys)
            .map { findViewById<MaterialButton>(it) }
        for (button in toggleButtons) {
            button.backgroundTintList = toggleBg
            button.setTextColor(toggleText)
        }

        btnPlay.backgroundTintList = enabledStates(colorPrimary)
        btnConfirm.backgroundTintList = enabledStates(colorPrimary)
        btnRestart.backgroundTintList = enabledStates(colorPrimary)
        btnRoll.backgroundTintList = enabledStates(colorAccent)

        // Vypnutí "ripple" animace (vlna po kliknutí) u všech tlačítek
        val noRipple = ColorStateList.valueOf(Color.TRANSPARENT)
        for (button in toggleButtons + listOf(btnPlay, btnConfirm, btnRoll, btnRestart)) {
            button.rippleColor = noRipple
        }
    }

    private fun enabledStates(color: Int) = ColorStateList(
        arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf()),
        intArrayOf(colorDisabledBg, color)
    )

    private fun updateConfirmButton() {
        btnConfirm.isEnabled =
            tgParity.checkedButtonId != View.NO_ID && tgBet.checkedButtonId != View.NO_ID
    }

    private fun selectedBet(): Int = betButtons[tgBet.checkedButtonId] ?: 0

    companion object {
        private const val KEY_PHASE = "phase"
        private const val KEY_POINTS = "points"
        private const val KEY_DICE = "dice"
        private const val KEY_RESULT = "result"
        private const val KEY_PARITY = "parity"
        private const val KEY_BET = "bet"
    }
}