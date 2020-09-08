package com.example.flashcards.groupcreator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flashcards.database.FlashcardsDatabaseDao

class FlashcardsGroupCreatorViewModelFactory(private val dataSource: FlashcardsDatabaseDao) :
    ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardsGroupCreatorViewModel::class.java)) {
            return FlashcardsGroupCreatorViewModel(dataSource) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}