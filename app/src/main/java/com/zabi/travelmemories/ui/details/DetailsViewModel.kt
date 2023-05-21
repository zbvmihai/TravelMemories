package com.zabi.travelmemories.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "There are no memories saved yet."
    }
    val text: LiveData<String> = _text
}