package com.zabi.travelmemories.ui.addmemory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.zabi.travelmemories.models.Memory
import com.zabi.travelmemories.utils.MemoryDatabase
import kotlinx.coroutines.launch

class AddMemoryViewModel(application: Application) : AndroidViewModel(application) {

    private val memoryDao = MemoryDatabase.getInstance(application).memoryDao

    suspend fun insert(memory: Memory) = viewModelScope.launch {
        memoryDao.insert(memory)
    }
}
