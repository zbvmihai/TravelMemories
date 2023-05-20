package com.zabi.travelmemories.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "There are no memories saved yet."
    }
    val text: LiveData<String> = _text
}