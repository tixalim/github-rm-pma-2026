package com.example.myapp008asharedpref

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapp008asharedpref.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Načtení uložených dat (synchronně, na hlavním vlákně)
        val sharedPref = requireActivity().getSharedPreferences("HracskyProfil", Context.MODE_PRIVATE)
        val jmeno = sharedPref.getString("JMENO", "") ?: ""
        val hardcore = sharedPref.getBoolean("HARDCORE", false)

        // Výpis do UI
        if (jmeno.isEmpty()) {
            binding.tvJmeno.text = getString(R.string.jmeno_vypis, getString(R.string.jmeno_nevyplneno))
        } else {
            binding.tvJmeno.text = getString(R.string.jmeno_vypis, jmeno)
        }

        if (hardcore) {
            binding.tvHardcore.text = getString(R.string.hardcore_zapnuty)
        } else {
            binding.tvHardcore.text = getString(R.string.hardcore_vypnuty)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}