package com.zabi.travelmemories.utils

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.zabi.travelmemories.models.Memory

@Dao
interface MemoryDAO {
    @Insert
    suspend fun insert(memory: Memory)

    @Update
    suspend fun update(memory: Memory)

    @Query("SELECT * from memory_table ORDER BY id DESC")
    fun getAllMemories(): LiveData<List<Memory>>

    @Query("DELETE FROM memory_table")
    suspend fun clear()
}