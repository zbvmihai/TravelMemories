package com.zabi.travelmemories.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memory_table")
data class Memory(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    val name: String,
    val image: String,
    val date: String,
    val place: String,
    val location: Location,
    val type: String,
    val mood: Double,
    val notes: String
)
