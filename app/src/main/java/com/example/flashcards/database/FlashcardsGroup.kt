package com.example.flashcards.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FlashcardsGroup(
    @PrimaryKey(autoGenerate = true)
    var groupId: Long = 0L,
    var title: String = ""
)