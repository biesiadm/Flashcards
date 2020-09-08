package com.example.flashcards.flashcardspackage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flashcards.database.FlashcardsDatabaseDao

class FlashcardsPackageViewModelFactory(
    private val flashcardsGroupKey: Long,
    private val dataSource: FlashcardsDatabaseDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardsPackageViewModel::class.java)) {
            return FlashcardsPackageViewModel(flashcardsGroupKey, dataSource) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}