package com.zabi.travelmemories.utils

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Delete
    suspend fun delete(memory: Memory)

    @Query("DELETE FROM memory_table WHERE id = :memoryId")
    suspend fun delete(memoryId: Long)
}