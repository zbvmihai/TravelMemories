package com.zabi.travelmemories.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.zabi.travelmemories.models.Memory
import com.zabi.travelmemories.utils.MemoryDAO
import com.zabi.travelmemories.utils.MemoryDatabase
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val memoryDAO: MemoryDAO = MemoryDatabase.getInstance(application).memoryDao
    val memoryList: LiveData<List<Memory>> = memoryDAO.getAllMemories()

    private val _text = MutableLiveData<String>().apply {
        value = "There are no memories saved yet."
    }
    val text: LiveData<String> = _text

    fun deleteMemory(memory: Memory) {
        viewModelScope.launch {
            memoryDAO.delete(memory)
        }
    }
}
