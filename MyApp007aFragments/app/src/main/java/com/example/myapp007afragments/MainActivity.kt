package com.example.myapp007afragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapp007afragments.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // po spuštění ukážu rovnou fragment A, ať není prázdno
        if (savedInstanceState == null) {
            ukazFragment(FragmentA())
        }

        binding.btnFragmentA.setOnClickListener { ukazFragment(FragmentA()) }
        binding.btnFragmentB.setOnClickListener { ukazFragment(FragmentB()) }
    }

    // vymění to, co je v kontejneru, za nový fragment
    private fun ukazFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}