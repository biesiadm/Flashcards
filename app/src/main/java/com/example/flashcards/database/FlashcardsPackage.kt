package com.example.flashcards.database

import androidx.room.Embedded
import androidx.room.Relation


data class FlashcardsPackage(
    @Embedded val group: FlashcardsGroup,

    @Relation(
        parentColumn = "groupId",
        entityColumn = "flashcardGroupId"
    )
    val flashcards: List<Flashcard>
)