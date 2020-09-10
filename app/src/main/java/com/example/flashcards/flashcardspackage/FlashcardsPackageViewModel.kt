package com.example.flashcards.flashcardspackage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.database.Flashcard
import com.example.flashcards.database.FlashcardsDatabaseDao
import kotlinx.coroutines.launch


enum class Side { FRONT, BACK }

class FlashcardsPackageViewModel(
    private val flashcardsGroupKey: Long = 0L,
    dataSource: FlashcardsDatabaseDao
) : ViewModel() {

    val database = dataSource

    val flashcards = database.getFlashcardsWithId(flashcardsGroupKey)

    val packageTitle = database.getFlashcardsPackageTitle(flashcardsGroupKey)

    lateinit var flashcardsList: MutableList<Flashcard>
    var flashcardsListInitialised = false
    fun getFlashcardsLeftSize() = flashcardsList.size + 1

    private val _currentFlashcard = MutableLiveData<Flashcard>()

    val currentFlashcard: LiveData<Flashcard>
        get() = _currentFlashcard

    /**
     * Changing flashcard side.
     */

    private val _changeFlashcardSide = MutableLiveData<Boolean?>()
    val changeFlashcardSide: LiveData<Boolean?>
        get() = _changeFlashcardSide

    fun doneChangingSide() {
        _changeFlashcardSide.value = null
    }

    fun onTap() {
        _changeFlashcardSide.value = true
    }

    /**
     * Handling current flashcard side.
     */

    private val _currentSide = MutableLiveData(Side.FRONT)
    val currentSide: LiveData<Side>
        get() = _currentSide

    fun changeSide() {
        when (_currentSide.value) {
            Side.FRONT -> _currentSide.value = Side.BACK
            Side.BACK -> _currentSide.value = Side.FRONT
        }
    }

    /**
     * Navigation to main screen.
     */

    private val _eventFlashcardsFinish = MutableLiveData<Boolean?>()
    val eventFlashcardsFinish: LiveData<Boolean?>
        get() = _eventFlashcardsFinish

    private fun onFlashcardsFinish() {
        _eventFlashcardsFinish.value = true
    }

    fun onFlashcardsFinishComplete() {
        _eventFlashcardsFinish.value = null
    }

    private fun resetList() {
        viewModelScope.launch {
            flashcardsList = database.getFlashcardsAtMomentWithId(flashcardsGroupKey)

            flashcardsList.shuffle()
            nextFlashcard()

            flashcardsListInitialised = true
        }
    }

    private fun nextFlashcard() {
        if (flashcardsList.isEmpty()) {
            onFlashcardsFinish()
        } else {
            _currentSide.value = Side.FRONT
            _currentFlashcard.value = flashcardsList.removeAt(0)
        }
    }

    init {
        resetList()
    }

    /**
     * Handle flashcard buttons.
     */

    fun onIncorrect() {
        currentFlashcard.value?.let {
            flashcardsList.add(it)
            nextFlashcard()
        }
    }

    fun onCorrect() {
        nextFlashcard()
    }
}