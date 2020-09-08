package com.example.flashcards.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FlashcardsGroup::class, Flashcard::class], version = 1, exportSchema = false)
abstract class FlashcardsDatabase : RoomDatabase() {

    abstract val flashcardsDatabaseDao: FlashcardsDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: FlashcardsDatabase? = null

        fun getInstance(context: Context): FlashcardsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FlashcardsDatabase::class.java,
                        "flashcards_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}