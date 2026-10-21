package com.example.myapp005objednavkav2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp005objednavkav2.databinding.ActivitySummaryBinding

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Příjem dat z intentu
        val sperkName = intent.getStringExtra("EXTRA_SPERK_NAME") ?: getString(R.string.bracelet_heart)
        val sperkImage = intent.getIntExtra("EXTRA_SPERK_IMAGE", R.drawable.sperk_1)
        val totalPrice = intent.getIntExtra("EXTRA_TOTAL_PRICE", 0)

        val hasEngraving = intent.getBooleanExtra("EXTRA_HAS_ENGRAVING", false)
        val hasGiftBox = intent.getBooleanExtra("EXTRA_HAS_GIFT_BOX", false)
        val hasLongChain = intent.getBooleanExtra("EXTRA_HAS_LONG_CHAIN", false)

        // Sestavení textu s doplňky
        var extrasText = getString(R.string.summary_extras_title) + "\n"
        var hasAnyExtra = false

        if (hasEngraving) {
            extrasText += "- " + getString(R.string.extra_engraving) + "\n"
            hasAnyExtra = true
        }
        if (hasGiftBox) {
            extrasText += "- " + getString(R.string.extra_gift_box) + "\n"
            hasAnyExtra = true
        }
        if (hasLongChain) {
            extrasText += "- " + getString(R.string.extra_long_chain) + "\n"
            hasAnyExtra = true
        }
        if (!hasAnyExtra) {
            extrasText += "- " + getString(R.string.summary_no_extras)
        }

        // Zobrazení dat
        binding.ivSummarySperk.setImageResource(sperkImage)
        binding.tvSelectedSperk.text = getString(R.string.summary_bracelet, sperkName)
        binding.tvExtras.text = extrasText.trimEnd()
        binding.tvFinalPrice.text = getString(R.string.price_total, totalPrice)

        // Zpět na úpravu objednávky
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}