package com.zabi.travelmemories.ui.editmemory

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.datepicker.MaterialDatePicker
import com.zabi.travelmemories.R
import com.zabi.travelmemories.databinding.FragmentEditMemoryBinding
import com.zabi.travelmemories.models.Location
import com.zabi.travelmemories.models.Memory
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class EditMemoryFragment : Fragment() {

    private var _binding: FragmentEditMemoryBinding? = null
    private val binding get() = _binding!!

    private var selectedType: Int = 0
    private lateinit var memory: Memory
    private lateinit var location: Location

    private lateinit var editMemoryViewModel: EditMemoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        editMemoryViewModel = ViewModelProvider(this)[EditMemoryViewModel::class.java]

        _binding = FragmentEditMemoryBinding.inflate(inflater, container, false)

        val args: EditMemoryFragmentArgs by navArgs()
        memory = args.memory

        setupSpinner()
        setupDatePicker()
        setupPlacePicker()

        binding.etEditMemoryName.editText?.setText(memory.name)
        binding.ivEditMemoryPhoto.setImageURI(memory.image?.toUri())
        binding.etEditPlaceLocation.editText?.setText(memory.place)
        binding.etEditPlaceLocation.editText?.setText(memory.place)
        binding.etEditDate.editText?.setText(memory.date)
        binding.sliderEdit.value = if (memory.mood == null) {
            0f
        } else {
            memory.mood!!.toFloat()
        }
        binding.spinnerEdit.setSelection(memory.type)
        binding.etEditNotes.editText?.setText(memory.notes)


        binding.saveEditBtn.setOnClickListener {
            updateMemory()
        }

        return binding.root
    }

    private fun setupSpinner() {
        val spinner: Spinner = binding.spinnerEdit

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.moods_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                selectedType = pos
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedType = 0
            }
        }
    }

    private fun setupPlacePicker(){
        if (!Places.isInitialized()) {
            Places.initialize(activity, "AIzaSyDzIb8ooyNWX9UD09oOFjTMWDn3IwfzeKA");
        }

        binding.etEditPlaceLocation.editText?.setOnClickListener {

            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(activity)
            startActivityForResult(intent, 1)
        }
    }

    private fun setupDatePicker(){

        binding.etEditDate.editText?.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()

            datePicker.addOnPositiveButtonClickListener {

                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                binding.etEditDate.editText?.setText(date.format(dateFormatter))
            }

            datePicker.show(parentFragmentManager, "datePicker")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                binding.etEditPlaceLocation.editText?.setText(place.name)
                location = Location(place.latLng?.latitude ?: 0.0, place.latLng?.longitude ?: 0.0)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.i(ContentValues.TAG, status.statusMessage!!)
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private fun updateMemory() {
        val updatedName = binding.etEditMemoryName.editText?.text.toString()
        val updatedPlace = binding.etEditPlaceLocation.editText?.text.toString()
        val updatedDate = binding.etEditDate.editText?.text.toString()
        val updatedMood = binding.sliderEdit.value.toDouble()
        val updatedType = selectedType
        val updatedNotes = binding.etEditNotes.editText?.text.toString()

        val updatedMemory = Memory(
            memory.id,
            updatedName,
            memory.image,
            updatedDate,
            updatedPlace,
            memory.location,
            updatedType,
            updatedMood,
            updatedNotes
        )
        viewLifecycleOwner.lifecycleScope.launch {
            editMemoryViewModel.update(updatedMemory)
            Toast.makeText(requireContext(), "Memory updated", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_editMemoryFragment_to_nav_home)
        }
    }
}