package com.example.flashcards.groupcreator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.database.FlashcardsDatabaseDao
import com.example.flashcards.database.FlashcardsGroup
import kotlinx.coroutines.launch

class FlashcardsGroupCreatorViewModel(private val dataSource: FlashcardsDatabaseDao) : ViewModel() {

    val database = dataSource

    private val _createNewPackageEvent = MutableLiveData<Boolean?>()
    val createNewPackageEvent: LiveData<Boolean?>
        get() = _createNewPackageEvent

    fun onClick() {
        _createNewPackageEvent.value = true
    }

    fun onCreateNewPackageDone() {
        _createNewPackageEvent.value = null
    }

    private suspend fun add(packageTitle: String) {
        database.insertGroup(FlashcardsGroup(title = packageTitle))
    }

    fun addNewPackage(packageTitle: String) {
        viewModelScope.launch {
            add(packageTitle)
        }
    }
}