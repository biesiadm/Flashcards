package com.example.flashcards.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flashcards.database.FlashcardsDatabaseDao

class FlashcardsMenuViewModelFactory(
    private val dataSource: FlashcardsDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardsMenuViewModel::class.java)) {
            return FlashcardsMenuViewModel(dataSource) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}