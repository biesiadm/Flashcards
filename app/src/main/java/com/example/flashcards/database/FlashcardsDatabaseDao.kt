package com.example.flashcards.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class FlashcardsDatabaseDao {

    suspend fun insertFlashcards(groupId: Long, flashcards: List<Flashcard>) {
        for (flashcard in flashcards) {
            flashcard.flashcardGroupId = groupId
        }

        _insertAll(flashcards)
    }

    suspend fun insertFlashcard(groupId: Long, flashcard: Flashcard) {
        flashcard.flashcardGroupId = groupId

        _insert(flashcard)
    }

    suspend fun deleteFlashcardsPackage(groupId: Long) {
        _deleteFlashcards(groupId)
        _deleteFlashcardsGroup(groupId)
    }

    @Insert
    abstract suspend fun _insert(flashcard: Flashcard)

    @Insert
    abstract suspend fun _insertAll(flashcards: List<Flashcard>)

    @Insert
    abstract suspend fun insertGroup(flashcardsGroup: FlashcardsGroup)

    @Query("SELECT * FROM Flashcard WHERE flashcardGroupId = :groupId")
    abstract suspend fun getFlashcards(groupId: Long): List<Flashcard>

    @Query("DELETE FROM Flashcard WHERE flashcardGroupId = :groupId")
    abstract suspend fun _deleteFlashcards(groupId: Long)

    @Query("DELETE FROM FlashcardsGroup WHERE groupId = :groupId")
    abstract suspend fun _deleteFlashcardsGroup(groupId: Long)

    @Query("SELECT * FROM FlashcardsGroup ORDER BY groupId DESC LIMIT 1")
    abstract suspend fun getLatestGroup(): FlashcardsGroup

    @Query("SELECT * FROM Flashcard WHERE flashcardGroupId = :groupId")
    abstract suspend fun getFlashcardsAtMomentWithId(groupId: Long): MutableList<Flashcard>

    @Transaction
    @Query("SELECT * FROM FlashcardsGroup ORDER BY groupId DESC")
    abstract fun getAllFlashcardsPackages(): LiveData<List<FlashcardsPackage>>

    @Query("SELECT * FROM Flashcard WHERE flashcardGroupId = :groupId")
    abstract fun getFlashcardsWithId(groupId: Long): LiveData<List<Flashcard>>

    @Query("SELECT title FROM FlashcardsGroup WHERE groupId = :groupId")
    abstract fun getFlashcardsPackageTitle(groupId: Long): LiveData<String>
}
