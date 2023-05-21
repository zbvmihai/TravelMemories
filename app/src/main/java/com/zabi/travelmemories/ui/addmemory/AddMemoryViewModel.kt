package com.zabi.travelmemories.ui.addmemory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddMemoryViewModel: ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        value = "Please Add A Memory"
    }
    val text: LiveData<String> = _text
}