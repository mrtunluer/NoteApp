package com.yks.noteapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note(
    val title: String,
    val description: String,
    val time: Long,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
): Serializable