package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapp007bfragmentsexample1.databinding.FragmentCatalogBinding

class CatalogFragment : Fragment() {

    private lateinit var binding: FragmentCatalogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    // tady už je vzhled hotový, takže můžu nastavit kliknutí
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemSrdce.setOnClickListener { otevriDetail(1) }
        binding.itemNekonecno.setOnClickListener { otevriDetail(2) }
        binding.itemStrom.setOnClickListener { otevriDetail(3) }
    }

    private fun otevriDetail(cislo: Int) {
        // do detailu pošlu číslo šperku, aby věděl, co má ukázat
        val detail = DetailFragment()
        val balicek = Bundle()
        balicek.putInt("SPERK", cislo)
        detail.arguments = balicek

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, detail)
            .addToBackStack(null)   // díky tomu funguje tlačítko zpět
            .commit()
    }
}