package com.example.flashcards

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.example.flashcards.database.Flashcard
import com.example.flashcards.database.FlashcardsDatabase
import com.example.flashcards.database.FlashcardsDatabaseDao
import com.example.flashcards.database.FlashcardsGroup
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class FlashcardsDatabaseTest {

    private lateinit var flashcardsDao: FlashcardsDatabaseDao
    private lateinit var db: FlashcardsDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, FlashcardsDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        flashcardsDao = db.flashcardsDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertAndGetGroup() {
        val group = FlashcardsGroup()
        group.title = "test"
        flashcardsDao.insertGroup(group)
        val allGroups = flashcardsDao.getAllFlashcardsPackages().value
        assertEquals(allGroups?.get(0)?.group?.title, "test")
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertManyGroups() {
        val size = 10
        for (i in 1..size) {
            val group = FlashcardsGroup()
            group.title = i.toString()

            flashcardsDao.insertGroup(group)
        }

        val allGroups = flashcardsDao.getAllFlashcardsPackages().value
        assertEquals(allGroups?.size, size)

        val sorted = allGroups?.sortedBy { it.group.title.toInt() }

        if (sorted != null) {
            for ((i, group) in sorted.withIndex()) {
                assertEquals(group.flashcards.size, 0)
                assertEquals(group.group.title, (i + 1).toString())
            }
        }
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertAndPopulateGroup() {
        val title = "abc"
        val group = FlashcardsGroup()
        group.title = title

        flashcardsDao.insertGroup(group)

        for (i in 0..10) {
            flashcardsDao.insertFlashcards(group, listOf(Flashcard(front = i.toString())))
        }

        val groupDb = flashcardsDao.getFlashcards(0)

        assertEquals(groupDb.size, 11)
    }
}