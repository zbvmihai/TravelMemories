package com.zabi.travelmemories.models

data class Memory(
    val name: String,
    val image: String,
    val date: String,
    val place: String,
    val location: Location,
    val type: String,
    val mood: String,
    val notes: String
)
