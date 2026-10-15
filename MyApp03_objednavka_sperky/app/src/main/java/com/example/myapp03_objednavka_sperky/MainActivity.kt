package com.example.myapp03_objednavka_sperky

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp03_objednavka_sperky.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Ceny v Kč
    private val cenaSrdce = 890
    private val cenaNekonecno = 990
    private val cenaStrom = 1190
    private val cenaGravirovani = 250
    private val cenaKrabicka = 100
    private val cenaRetizek = 150

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Změna náramku -> změna obrázku + přepočet ceny
        binding.rgSperk.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbSperk1.id -> binding.ivSperk.setImageResource(R.drawable.sperk_1)
                binding.rbSperk2.id -> binding.ivSperk.setImageResource(R.drawable.sperk_2)
                binding.rbSperk3.id -> binding.ivSperk.setImageResource(R.drawable.sperk_3)
            }
            aktualizujCenu()
        }

        // Změna doplňku -> přepočet ceny
        binding.cbBTN1.setOnCheckedChangeListener { _, _ -> aktualizujCenu() }
        binding.cbBTN2.setOnCheckedChangeListener { _, _ -> aktualizujCenu() }
        binding.cbBTN3.setOnCheckedChangeListener { _, _ -> aktualizujCenu() }

        // Cena se zobrazí hned po spuštění
        aktualizujCenu()

        // Tlačítko Objednat - vypíše souhrn objednávky
        binding.btnOrder.setOnClickListener {
            val sperk = when (binding.rgSperk.checkedRadioButtonId) {
                binding.rbSperk2.id -> binding.rbSperk2
                binding.rbSperk3.id -> binding.rbSperk3
                else -> binding.rbSperk1 // záložní možnost
            }

            val orderText = getString(R.string.order_summary_prefix, sperk.text) +
                    (if (binding.cbBTN1.isChecked) "; " + binding.cbBTN1.text else "") +
                    (if (binding.cbBTN2.isChecked) "; " + binding.cbBTN2.text else "") +
                    (if (binding.cbBTN3.isChecked) "; " + binding.cbBTN3.text else "")

            binding.tvOrder.text = orderText
        }
    }

    // Spočítá celkovou cenu a zobrazí ji v tvPrice
    private fun spocitejCenu(): Int {
        var cena = when (binding.rgSperk.checkedRadioButtonId) {
            binding.rbSperk2.id -> cenaNekonecno
            binding.rbSperk3.id -> cenaStrom
            else -> cenaSrdce
        }
        if (binding.cbBTN1.isChecked) cena += cenaGravirovani
        if (binding.cbBTN2.isChecked) cena += cenaKrabicka
        if (binding.cbBTN3.isChecked) cena += cenaRetizek
        return cena
    }

    private fun aktualizujCenu() {
        binding.tvPrice.text = getString(R.string.price_total, spocitejCenu())
    }
}