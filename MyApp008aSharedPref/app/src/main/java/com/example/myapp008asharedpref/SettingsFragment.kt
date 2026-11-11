package com.example.myapp008asharedpref

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapp008asharedpref.databinding.FragmentSettingsBinding

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

        // 1. Otevření SharedPreferences se jménem "HracskyProfil"
        val sharedPref = requireActivity().getSharedPreferences("HracskyProfil", Context.MODE_PRIVATE)

        // 2. Načtení uložených hodnot do formuláře
        binding.etJmeno.setText(sharedPref.getString("JMENO", ""))
        binding.switchHardcore.isChecked = sharedPref.getBoolean("HARDCORE", false)

        binding.tvPocetKliku.text = getString(R.string.pocet_kliku, pocetKliku)

        // 3. Uložení
        binding.btnUlozit.setOnClickListener {
            val jmeno = binding.etJmeno.text.toString()
            val hardcore = binding.switchHardcore.isChecked

            // Simulace těžké práce: hlavní vlákno se na 3 vteřiny úplně zastaví
            Thread.sleep(3000)

            // Synchronní zápis (commit čeká, dokud se data nezapíšou)
            val editor = sharedPref.edit()
            editor.putString("JMENO", jmeno)
            editor.putBoolean("HARDCORE", hardcore)
            editor.commit()

            Toast.makeText(requireContext(), getString(R.string.ulozeno_toast), Toast.LENGTH_SHORT).show()
        }

        // 4. Navazující úkol: tlačítko, které s ukládáním nesouvisí
        binding.btnKlikac.setOnClickListener {
            pocetKliku++
            binding.tvPocetKliku.text = getString(R.string.pocet_kliku, pocetKliku)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}