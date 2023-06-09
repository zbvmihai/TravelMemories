package com.zabi.travelmemories.ui.details

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.zabi.travelmemories.R
import com.zabi.travelmemories.databinding.FragmentDetailsBinding
import com.zabi.travelmemories.models.Memory
import com.zabi.travelmemories.utils.WeatherApiClient
import com.zabi.travelmemories.utils.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var memory: Memory
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
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

            val satelliteModeEnabled = sharedPrefs.getBoolean("isSatelliteModeEnabled", false)
            googleMap.mapType = if (satelliteModeEnabled) {
                GoogleMap.MAP_TYPE_HYBRID
            } else {
                GoogleMap.MAP_TYPE_NORMAL
            }

            val location = LatLng(memory.location!!.lat, memory.location!!.long)
            val mapScale = sharedPrefs.getFloat("mapScale", 50.0f)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, mapScale))
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

        val weatherApiClient = WeatherApiClient()
        weatherApiClient.getWeatherData(memory.location!!.lat, memory.location!!.long, object :
            Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        val temperatureKelvin = it.main.temp
                        val temperatureCelsius = convertKelvinToCelsius(temperatureKelvin).toInt()
                        binding.tvDetailsWeather.text = getString(R.string.temperature, temperatureCelsius.toString())
                    }
                } else {
                    Log.e("WeatherAPI", "WeatherAPI Error")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("Network Error", "Network Error")
            }
        })
    }

    fun convertKelvinToCelsius(temperatureKelvin: Double): Double {
        return temperatureKelvin - 273.15
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}