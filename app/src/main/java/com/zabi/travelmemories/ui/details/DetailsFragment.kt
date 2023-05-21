package com.zabi.travelmemories.ui.details

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.zabi.travelmemories.R
import com.zabi.travelmemories.databinding.FragmentDetailsBinding
import com.zabi.travelmemories.databinding.FragmentHomeBinding
import com.zabi.travelmemories.models.Memory
import com.zabi.travelmemories.ui.home.HomeViewModel


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var memory: Memory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailsViewModel =
            ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory
                    .getInstance(requireActivity().application))[DetailsViewModel::class.java]

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        memory = DetailsFragmentArgs.fromBundle(requireArguments()).memory
        populateMemoryDetails(memory)
        setupMap()

        return binding.root
    }

    private fun setupMap(){
        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager
            .beginTransaction()
            .replace(R.id.flMaps, mapFragment)
            .commit()

        mapFragment.getMapAsync { googleMap ->
            val location = LatLng(memory.location!!.lat, memory.location!!.long)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
        }
    }

    private fun populateMemoryDetails(memory: Memory) {

        val moodsArray = resources.getStringArray(R.array.moods_array)

        binding.ivDetailsPhoto.setImageURI(Uri.parse(memory.image))
        binding.tvDetailsMemoryName.text = memory.name
        binding.tvDetailsMemoryPlace.text = getString(R.string.place, memory.place)
        binding.tvDetailsMemoryDate.text = getString(R.string.travel_date, memory.date)
        binding.tvDetailsType.text = getString(R.string.type, moodsArray[memory.type])
        binding.tvDetailsMood.text = getString(R.string.travel_mood, memory.mood.toString())
        binding.tvDetailsNotes.text = memory.notes

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}