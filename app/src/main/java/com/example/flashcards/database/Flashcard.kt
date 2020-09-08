package com.example.flashcards.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Flashcard(
    @PrimaryKey(autoGenerate = true)
    var flashcardId: Long = 0L,
    var flashcardGroupId: Long = 0L,
    var front: String = "",
    var back: String = ""
)