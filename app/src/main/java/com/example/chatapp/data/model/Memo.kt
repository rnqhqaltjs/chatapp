package com.example.chatapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memo(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val category: String,
    val description: String?,
    val deposit: Int,
    val withdraw: Int,
    val year: Int,
    val month: Int,
    val day: Int,
)