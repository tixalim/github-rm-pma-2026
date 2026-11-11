package com.example.myapp008bdatastore

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapp008bdatastore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aby obsah nebyl schovaný pod horní lištou a spodní navigací
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Při prvním spuštění ukážeme Dashboard
        if (savedInstanceState == null) {
            zobrazFragment(DashboardFragment())
        }

        binding.btnDashboard.setOnClickListener {
            zobrazFragment(DashboardFragment())
        }

        binding.btnNastaveni.setOnClickListener {
            zobrazFragment(SettingsFragment())
        }
    }

    private fun zobrazFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}