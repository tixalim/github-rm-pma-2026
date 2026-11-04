package com.example.myapp005objednavkav2

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp005objednavkav2.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

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
        binding.rgSperk.setOnCheckedChangeListener { _, _ ->
            binding.ivSperk.setImageResource(vybranyObrazek())
            aktualizujCenu()
        }

        // Změna doplňku -> přepočet ceny (+ Snackbar když se odškrtne)
        binding.cbBTN1.setOnCheckedChangeListener { _, zaskrtnuto -> zmenaDoplnku(binding.cbBTN1, zaskrtnuto) }
        binding.cbBTN2.setOnCheckedChangeListener { _, zaskrtnuto -> zmenaDoplnku(binding.cbBTN2, zaskrtnuto) }
        binding.cbBTN3.setOnCheckedChangeListener { _, zaskrtnuto -> zmenaDoplnku(binding.cbBTN3, zaskrtnuto) }

        aktualizujCenu()

        // Tlačítko Objednat -> Toast + otevře novou aktivitu s rekapitulací
        binding.btnOrder.setOnClickListener {
            Toast.makeText(this, getString(R.string.toast_order_sent, spocitejCenu()), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, SummaryActivity::class.java)

            intent.putExtra("EXTRA_SPERK_NAME", vybranyNaramek().text.toString())
            intent.putExtra("EXTRA_SPERK_IMAGE", vybranyObrazek())
            intent.putExtra("EXTRA_TOTAL_PRICE", spocitejCenu())

            intent.putExtra("EXTRA_HAS_ENGRAVING", binding.cbBTN1.isChecked)
            intent.putExtra("EXTRA_HAS_GIFT_BOX", binding.cbBTN2.isChecked)
            intent.putExtra("EXTRA_HAS_LONG_CHAIN", binding.cbBTN3.isChecked)

            startActivity(intent)
        }
    }

    // když se doplněk odškrtne, ukáže se Snackbar s tlačítkem ZPĚT
    private fun zmenaDoplnku(checkBox: CheckBox, zaskrtnuto: Boolean) {
        aktualizujCenu()

        if (!zaskrtnuto) {
            Snackbar.make(binding.root, getString(R.string.snackbar_removed, checkBox.text), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.snackbar_undo)) {
                    checkBox.isChecked = true   // vrátí zaškrtnutí zpátky
                }
                .show()
        }
    }

    private fun vybranyNaramek() = when (binding.rgSperk.checkedRadioButtonId) {
        binding.rbSperk2.id -> binding.rbSperk2
        binding.rbSperk3.id -> binding.rbSperk3
        else -> binding.rbSperk1
    }

    private fun vybranyObrazek() = when (binding.rgSperk.checkedRadioButtonId) {
        binding.rbSperk2.id -> R.drawable.sperk_2
        binding.rbSperk3.id -> R.drawable.sperk_3
        else -> R.drawable.sperk_1
    }

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