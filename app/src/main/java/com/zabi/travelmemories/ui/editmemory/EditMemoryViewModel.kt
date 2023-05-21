package com.zabi.travelmemories.ui.editmemory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zabi.travelmemories.models.Memory
import com.zabi.travelmemories.utils.MemoryDatabase
import kotlinx.coroutines.launch

class EditMemoryViewModel(application: Application): AndroidViewModel(application) {

    private val memoryDao = MemoryDatabase.getInstance(application).memoryDao

    suspend fun update(memory: Memory) = viewModelScope.launch {
        memoryDao.update(memory)
    }
}