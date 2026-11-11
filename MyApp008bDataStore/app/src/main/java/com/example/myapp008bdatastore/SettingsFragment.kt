package com.example.myapp008bdatastore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapp008bdatastore.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    // Počítadlo pro navazující úkol
    private var pocetKliku = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvPocetKliku.text = getString(R.string.pocet_kliku, pocetKliku)

        // 1. Načtení uložených hodnot do formuláře (asynchronně)
        viewLifecycleOwner.lifecycleScope.launch {
            val data = requireContext().dataStore.data.first()
            binding.etJmeno.setText(data[JMENO_KEY] ?: "")
            binding.switchHardcore.isChecked = data[HARDCORE_KEY] ?: false
        }

        // 2. Uložení
        binding.btnUlozit.setOnClickListener {
            val jmeno = binding.etJmeno.text.toString()
            val hardcore = binding.switchHardcore.isChecked

            viewLifecycleOwner.lifecycleScope.launch {
                // Tlačítko vypneme, aby se nedalo kliknout dvakrát
                binding.btnUlozit.isEnabled = false

                // Zapamatujeme si staré hodnoty kvůli tlačítku "Zpět"
                val stareData = requireContext().dataStore.data.first()
                val stareJmeno = stareData[JMENO_KEY] ?: ""
                val staryHardcore = stareData[HARDCORE_KEY] ?: false

                // Simulace těžké práce, ale hlavní vlákno NENÍ zablokované
                delay(3000)

                // Asynchronní zápis do DataStore
                requireContext().dataStore.edit { prefs ->
                    prefs[JMENO_KEY] = jmeno
                    prefs[HARDCORE_KEY] = hardcore
                }

                binding.btnUlozit.isEnabled = true

                // Snackbar s tlačítkem Zpět
                Snackbar.make(binding.root, R.string.ulozeno_snackbar, Snackbar.LENGTH_LONG)
                    .setAction(R.string.zpet) {
                        vratitZpet(stareJmeno, staryHardcore)
                    }
                    .show()
            }
        }

        // 3. Navazující úkol: tlačítko, které s ukládáním nesouvisí
        binding.btnKlikac.setOnClickListener {
            pocetKliku++
            binding.tvPocetKliku.text = getString(R.string.pocet_kliku, pocetKliku)
        }
    }

    // Vrátí do DataStore i do formuláře hodnoty, které tam byly před uložením
    private fun vratitZpet(jmeno: String, hardcore: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            requireContext().dataStore.edit { prefs ->
                prefs[JMENO_KEY] = jmeno
                prefs[HARDCORE_KEY] = hardcore
            }
            binding.etJmeno.setText(jmeno)
            binding.switchHardcore.isChecked = hardcore
            Toast.makeText(requireContext(), getString(R.string.vraceno_zpet), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}