@file:Suppress("DEPRECATION")

package com.zabi.travelmemories.ui.addmemory

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.datepicker.MaterialDatePicker
import com.zabi.travelmemories.R
import com.zabi.travelmemories.databinding.FragmentAddMemoryBinding
import com.zabi.travelmemories.models.Location
import com.zabi.travelmemories.models.Memory
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch


class AddMemoryFragment : Fragment() {

    private var _binding: FragmentAddMemoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageFile: File
    private lateinit var location: Location
    private var selectedType: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addMemoryViewModel =
            ViewModelProvider(this)[AddMemoryViewModel::class.java]

        _binding = FragmentAddMemoryBinding.inflate(inflater, container, false)

        val memory = Memory()
        setupSpinner()
        setupDatePicker()
        setupPlacePicker()
        setupImagePicker()

        binding.saveBtn.setOnClickListener {

            memory.image = imageFile.absolutePath
            memory.name = binding.etMemoryName.editText?.text.toString()
            memory.place = binding.etPlaceLocation.editText?.text.toString()
            memory.date = binding.etDate.editText?.text.toString()
            memory.mood = binding.slider.value.toDouble()
            memory.notes = binding.etNotes.editText?.text.toString()
            memory.type = selectedType
            memory.location = location
            // Save the memory object to the database
            viewLifecycleOwner.lifecycleScope.launch {
                addMemoryViewModel.insert(memory)
                Toast.makeText(requireContext(), "Memory saved", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addMemoryFragment_to_nav_home)
            }
        }
        return binding.root
    }

    private fun setupImagePicker(){
        binding.ivAddMemoryPhoto.setOnClickListener {

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_READ_STORAGE)
            } else {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun setupPlacePicker(){
        if (!Places.isInitialized()) {
            Places.initialize(activity, "AIzaSyDzIb8ooyNWX9UD09oOFjTMWDn3IwfzeKA");
        }

        binding.etPlaceLocation.editText?.setOnClickListener {

            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,Place.Field.LAT_LNG)

            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(activity)
            startActivityForResult(intent, 1)
        }
    }

    private fun setupDatePicker(){

        binding.etDate.editText?.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()

            datePicker.addOnPositiveButtonClickListener {

                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                binding.etDate.editText?.setText(date.format(dateFormatter))
            }

            datePicker.show(parentFragmentManager, "datePicker")
        }
    }

    private fun setupSpinner(){
        val spinner: Spinner = binding.spinner

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                binding.etPlaceLocation.editText?.setText(place.name)
                location = Location(place.latLng?.latitude ?: 0.0, place.latLng?.longitude ?: 0.0)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.i(TAG, status.statusMessage!!)
            } else if (resultCode == RESULT_CANCELED) {

            }
        }

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            var selectedImageUri = data?.data
            selectedImageUri?.let {
                val selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                val filename = "${System.currentTimeMillis()}.jpg" // Unique filename
                imageFile = saveBitmapToPrivateStorage(selectedImageBitmap, filename)
                binding.ivAddMemoryPhoto.setImageURI(selectedImageUri)
            }
        }
    }

    private fun saveBitmapToPrivateStorage(bitmap: Bitmap, filename: String): File {
        val file = File(requireActivity().filesDir, filename)
        val fileOutputStream = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)

        fileOutputStream.flush()
        fileOutputStream.close()

        return file
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(requireContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 100
        private const val PERMISSIONS_REQUEST_READ_STORAGE = 2
    }
}


