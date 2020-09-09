package com.example.flashcards.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.database.FlashcardsDatabaseDao
import kotlinx.coroutines.launch

class FlashcardsMenuViewModel(dataSource: FlashcardsDatabaseDao) : ViewModel() {

    val database = dataSource

    val flashcardsPackages = database.getAllFlashcardsPackages()

    /**
     * Navigation for FlashcardsPackage fragment.
     */

    private val _navigateToFlashcardsPackage = MutableLiveData<Long>()
    val navigateToFlashcardsPackage
        get() = _navigateToFlashcardsPackage


    fun onNavigateToFlashcardsPackage(id: Long) {
        _navigateToFlashcardsPackage.value = id
    }

    fun onFlashcardsPackageNavigated() {
        _navigateToFlashcardsPackage.value = null
    }

    private val _navigateToFlashcardsGroupCreator = MutableLiveData<Boolean?>()
    val navigateToFlashcardsGroupCreator: LiveData<Boolean?>
        get() = _navigateToFlashcardsGroupCreator

    fun onAddPackageButtonClicked() {
        _navigateToFlashcardsGroupCreator.value = true
    }

    fun onFlashcardsGroupCreatorNavigated() {
        _navigateToFlashcardsGroupCreator.value = null
    }

    private val _navigateToFlashcardCreator = MutableLiveData<Long>()
    val navigateToFlashcardCreator: LiveData<Long>
        get() = _navigateToFlashcardCreator

    fun onAddFlashcardButtonClicked(id: Long) {
        _navigateToFlashcardCreator.value = id
    }

    fun onFlashcardCreatorNavigated() {
        _navigateToFlashcardCreator.value = null
    }

    /**
     * Toast for empty packages.
     */

    private val _displayEmptyPackageToast = MutableLiveData<Boolean?>()
    val displayEmptyPackageToast: LiveData<Boolean?>
        get() = _displayEmptyPackageToast

    fun onDisplayEmptyPackageToast() {
        _displayEmptyPackageToast.value = true
    }

    fun onDisplayEmptyPackageToastDone() {
        _displayEmptyPackageToast.value = null
    }

    fun navigateToPackage(groupId: Long) {
        viewModelScope.launch {
            val flashcards = database.getFlashcards(groupId)

            if (flashcards.isEmpty()) {
                onDisplayEmptyPackageToast()
            } else {
                onNavigateToFlashcardsPackage(groupId)
            }
        }
    }

    /**
     * Executes when 'X' button is clicked.
     */

    fun onDelete(groupId: Long) {
        viewModelScope.launch {
            delete(groupId)
        }
    }

    /**
     * Database operations handling.
     */

    private suspend fun delete(groupId: Long) {
        database.deleteFlashcardsPackage(groupId)
    }
}