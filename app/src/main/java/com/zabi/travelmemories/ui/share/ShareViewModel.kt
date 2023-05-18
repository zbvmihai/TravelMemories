package com.zabi.travelmemories.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Share Fragment"
    }
    val text: LiveData<String> = _text
}