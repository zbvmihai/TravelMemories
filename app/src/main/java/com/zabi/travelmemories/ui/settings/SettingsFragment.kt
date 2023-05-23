package com.zabi.travelmemories.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.zabi.travelmemories.databinding.FragmentSettingsBinding

@Suppress("NAME_SHADOWING")
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", 0)

        setupDarkModeSwitch()
        setupSatelliteModeSwitch()
        setupMapScaleSlider()

        return root
    }

    private fun setupDarkModeSwitch() {
        binding.switchDarkMode.isChecked = sharedPrefs.getBoolean("isDarkModeEnabled", false)
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefs.edit().putBoolean("isDarkModeEnabled", true).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefs.edit().putBoolean("isDarkModeEnabled", false).apply()
            }
        }
    }

    private fun setupSatelliteModeSwitch() {
        binding.switchSatelliteMode.isChecked = sharedPrefs.getBoolean("isSatelliteModeEnabled", false)
        binding.switchSatelliteMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("isSatelliteModeEnabled", isChecked).apply()
        }
    }

    private fun setupMapScaleSlider() {
        val savedMapScale = sharedPrefs.getFloat("mapScale", 10.0f)
        binding.sliderZoomLevel.value = savedMapScale
        binding.sliderZoomLevel.addOnChangeListener { _, value, _ ->
            sharedPrefs.edit().putFloat("mapScale", value).apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

