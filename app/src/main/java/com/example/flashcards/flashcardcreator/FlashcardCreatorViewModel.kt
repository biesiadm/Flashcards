package com.example.flashcards.flashcardcreator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.database.Flashcard
import com.example.flashcards.database.FlashcardsDatabaseDao
import kotlinx.coroutines.launch

class FlashcardCreatorViewModel(
    private val flashcardsGroupKey: Long = 0L,
    private val dataSource: FlashcardsDatabaseDao
) : ViewModel() {

    val database = dataSource

    val packageTitle = database.getFlashcardsPackageTitle(flashcardsGroupKey)

    private val _createNewFlashcardEvent = MutableLiveData<Boolean?>()
    val createNewFlashcardEvent: LiveData<Boolean?>
        get() = _createNewFlashcardEvent

    fun onCreateFlashcard() {
        _createNewFlashcardEvent.value = true
    }

    fun onCreateFlashcardDone() {
        _createNewFlashcardEvent.value = null
    }

    fun addNewFlashcard(front: String, back: String) {
        viewModelScope.launch {
            addFlashcard(Flashcard(front = front, back = back))
        }
    }

    private suspend fun addFlashcard(flashcard: Flashcard) {
        database.insertFlashcard(flashcardsGroupKey, flashcard)
    }
}