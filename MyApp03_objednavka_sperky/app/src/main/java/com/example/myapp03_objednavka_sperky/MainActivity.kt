package com.example.myapp03_objednavka_sperky

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp03_objednavka_sperky.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        //binding - nafouknutí (inflate) layoutu do binding instance
        binding = ActivityMainBinding.inflate(layoutInflater)

        //nastavení kořenového pohledu (root) do okna aktivity
        setContentView(binding.root)


        // Ošetření systémových lišt – použije se přímo binding.main nebo binding.root
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnOrder.setOnClickListener {
            val sperk = when (binding.rgSperk.checkedRadioButtonId) {
                binding.rbSperk1.id -> binding.rbSperk1
                binding.rbSperk2.id -> binding.rbSperk2
                binding.rbSperk3.id -> binding.rbSperk3

                else -> binding.rbSperk1 //záložní možnost
            }

            val Srdce = binding.cbBTN1.isChecked
            val nekonecno = binding.cbBTN2.isChecked
            val strom = binding.cbBTN3.isChecked

            val orderText = "Souhrn objednavky: " + "${sperk.text}" +
                    (if(Srdce)) "; text a" else "") +
                    (if(nekonecno)) "; text b" else "") +
                    (if(strom)) "; text c" else "")

            binding.tvOrder.text = orderText

            binding.rbSperk1.setOnClickListener {
                binding.ivSperk.setImageResource(R.drawable.sperk_1)

            }
            binding.rbSperk2.setOnClickListener {
                binding.ivSperk.setImageResource(R.drawable.sperk_2)

            }
            binding.rbSperk3.setOnClickListener {
                binding.ivSperk.setImageResource(R.drawable.sperk_3)

            }

        }


    }
}