package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapp007bfragmentsexample1.databinding.ActivityMainBinding

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

        // po spuštění ukážu katalog
        if (savedInstanceState == null) {
            ukazFragment(CatalogFragment())
        }

        binding.btnKatalog.setOnClickListener { ukazFragment(CatalogFragment()) }
        binding.btnOMne.setOnClickListener { ukazFragment(AboutFragment()) }
    }

    private fun ukazFragment(fragment: Fragment) {
        // smažu historii (třeba otevřený detail), aby tlačítko zpět nedělalo blbosti
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}