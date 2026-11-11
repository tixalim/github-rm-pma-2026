package com.example.myapp008bdatastore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapp008bdatastore.databinding.FragmentDashboardBinding
import kotlinx.coroutines.launch

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

        // Načítání přes Flow: collect se spustí znovu pokaždé, když se data změní
        viewLifecycleOwner.lifecycleScope.launch {
            requireContext().dataStore.data.collect { data ->
                val jmeno = data[JMENO_KEY] ?: ""
                val hardcore = data[HARDCORE_KEY] ?: false

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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}