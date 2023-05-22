package com.zabi.travelmemories.ui.aboutus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutUsViewModel : ViewModel() {

    private val _title = MutableLiveData<String>().apply {
        value = "Travel Memories"
    }
    val title: LiveData<String> = _title

    private val _aboutus = MutableLiveData<String>().apply {
        value = "Welcome to Travel Memories, the app that helps you save and share your best travel moments! Pin memories to maps, add notes, weather, and cool photos. Capture the essence of each place, preserve details with notes, embrace the weather, and bring memories to life with photos. Share your adventures with friends and family. Download Travel Memories now and create a collection of unforgettable travel moments. Let the journey begin!"
    }
    val aboutus: LiveData<String> = _aboutus
}