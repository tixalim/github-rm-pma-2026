package com.example.myapp006toastsnackbar

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp006toastsnackbar.databinding.ActivityMainBinding
import com.example.myapp006toastsnackbar.databinding.CustomToastBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // kolik sušenek už jsem snědla
    private var susenky = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ukazPocet()

        // 1) obyčejný Toast
        binding.btnToast.setOnClickListener {
            Toast.makeText(this, getString(R.string.toast_text), Toast.LENGTH_SHORT).show()
        }

        // 2) Snackbar s tlačítkem VRÁTIT
        binding.btnSnackbar.setOnClickListener {
            susenky++
            ukazPocet()

            Snackbar.make(binding.root, getString(R.string.snackbar_text), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.snackbar_undo)) {
                    // když kliknu na VRÁTIT, sušenka se "vrátí"
                    susenky--
                    ukazPocet()
                }
                .show()
        }

        // 3) Toast s vlastní ikonkou (vlastní layout)
        binding.btnCustomToast.setOnClickListener {
            val toastBinding = CustomToastBinding.inflate(layoutInflater)
            toastBinding.tvToastText.text = getString(R.string.custom_toast_text)

            val toast = Toast(this)
            toast.duration = Toast.LENGTH_LONG
            toast.view = toastBinding.root
            toast.show()
        }
    }

    private fun ukazPocet() {
        binding.tvCounter.text = getString(R.string.counter, susenky)
    }
}