package com.example.myapp03_objednavka_sperky

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp03_objednavka_sperky.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding - nafouknutí (inflate) layoutu do binding instance
        binding = ActivityMainBinding.inflate(layoutInflater)

        // nastavení kořenového pohledu (root) do okna aktivity
        setContentView(binding.root)

        // Ošetření systémových lišt
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Změna obrázku hned při výběru náramku (MIMO tlačítko Objednat)
        binding.rgSperk.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbSperk1.id -> binding.ivSperk.setImageResource(R.drawable.sperk_1)
                binding.rbSperk2.id -> binding.ivSperk.setImageResource(R.drawable.sperk_2)
                binding.rbSperk3.id -> binding.ivSperk.setImageResource(R.drawable.sperk_3)
            }
        }

        // Tlačítko Objednat - vypíše souhrn objednávky
        binding.btnOrder.setOnClickListener {
            val sperk = when (binding.rgSperk.checkedRadioButtonId) {
                binding.rbSperk1.id -> binding.rbSperk1
                binding.rbSperk2.id -> binding.rbSperk2
                binding.rbSperk3.id -> binding.rbSperk3
                else -> binding.rbSperk1 // záložní možnost
            }

            // stav doplňků (checkboxů)
            val doplnek1 = binding.cbBTN1.isChecked
            val doplnek2 = binding.cbBTN2.isChecked
            val doplnek3 = binding.cbBTN3.isChecked

            val orderText = getString(R.string.order_summary_prefix, sperk.text) +
                    (if (doplnek1) "; " + binding.cbBTN1.text else "") +
                    (if (doplnek2) "; " + binding.cbBTN2.text else "") +
                    (if (doplnek3) "; " + binding.cbBTN3.text else "")

            binding.tvOrder.text = orderText
        }
    }
}