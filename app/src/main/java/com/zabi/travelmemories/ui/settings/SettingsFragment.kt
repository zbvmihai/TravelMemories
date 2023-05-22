package com.zabi.travelmemories.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import com.zabi.travelmemories.databinding.FragmentSettingsBinding
import com.zabi.travelmemories.R

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
        setupLanguageSpinner()
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

    private fun setupLanguageSpinner() {
        val languages = resources.getStringArray(R.array.language_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages)
        binding.spinnerEdit.adapter = adapter


        val selectedLanguage = sharedPrefs.getString("selectedLanguage", "")
        val currentIndex = languages.indexOf(selectedLanguage)

        binding.spinnerEdit.setSelection(currentIndex)

        binding.spinnerEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguage = parent?.getItemAtPosition(position).toString()

                sharedPrefs.edit().putString("selectedLanguage", selectedLanguage).apply()

                binding.spinnerEdit.setSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
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

