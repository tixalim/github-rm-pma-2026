package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapp007bfragmentsexample1.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // vytáhnu číslo šperku, které mi poslal katalog
        val cislo = arguments?.getInt("SPERK") ?: 1

        // výchozí hodnoty = srdce
        var obrazek = R.drawable.sperk_1
        var nazev = R.string.sperk_srdce
        var popis = R.string.popis_srdce
        var cena = 890

        if (cislo == 2) {
            obrazek = R.drawable.sperk_2
            nazev = R.string.sperk_nekonecno
            popis = R.string.popis_nekonecno
            cena = 990
        } else if (cislo == 3) {
            obrazek = R.drawable.sperk_3
            nazev = R.string.sperk_strom
            popis = R.string.popis_strom
            cena = 1190
        }

        binding.ivDetail.setImageResource(obrazek)
        binding.tvName.text = getString(nazev)
        binding.tvDescription.text = getString(popis)
        binding.tvPrice.text = getString(R.string.cena, cena)

        // zpět = vrátím se na předchozí fragment (katalog)
        binding.btnZpet.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}