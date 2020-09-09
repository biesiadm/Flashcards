package com.example.flashcards.flashcardcreator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flashcards.database.FlashcardsDatabaseDao

class FlashcardCreatorViewModelFactory(
    private val flashcardsGroupKey: Long = 0L,
    private val dataSource: FlashcardsDatabaseDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardCreatorViewModel::class.java)) {
            return FlashcardCreatorViewModel(flashcardsGroupKey, dataSource) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}