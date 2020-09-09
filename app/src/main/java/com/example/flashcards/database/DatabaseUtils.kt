package com.example.flashcards.database

fun createFlashcard(front: String, back: String): Flashcard {
    return Flashcard(front = front, back = back)
}

suspend fun prepopulateDb(database: FlashcardsDatabase) {
    val dao = database.flashcardsDatabaseDao

    val englishWordsGroup = FlashcardsGroup(title = "English -> Polish")
    val englishWords = listOf<Flashcard>(
        createFlashcard("Door", "Drzwi"),
        createFlashcard("Sea", "Morze"),
        createFlashcard("Tree", "Drzewo"),
    )

    dao.insertGroup(englishWordsGroup)
    var groupInDb = dao.getLatestGroup()
    dao.insertFlashcards(groupInDb.groupId, englishWords)

    val multiplicationsGroup = FlashcardsGroup(title = "Multiplication table")
    val simpleMultiplications = listOf<Flashcard>(
        createFlashcard("2 * 2", (2 * 2).toString()),
        createFlashcard("7 * 9", (7 * 9).toString()),
        createFlashcard("12 * 8", (12 * 8).toString()),
        createFlashcard("4 * 3", (4 * 3).toString())
    )

    dao.insertGroup(multiplicationsGroup)
    groupInDb = dao.getLatestGroup()
    dao.insertFlashcards(groupInDb.groupId, simpleMultiplications)
}