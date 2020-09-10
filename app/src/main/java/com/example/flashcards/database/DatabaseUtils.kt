package com.example.flashcards.database

fun createFlashcard(front: String, back: String): Flashcard {
    return Flashcard(front = front, back = back)
}

suspend fun prepopulateDb(database: FlashcardsDatabase) {
    val dao = database.flashcardsDatabaseDao

    if (dao.getLatestGroup() != null) {
        return
    }

    val tutorialGroup = FlashcardsGroup(title = "Flashcards guide -- Tap me!")
    val tutorialFlashcards = listOf(
        createFlashcard(
            "It's front side of flashcard. Tap to reverse",
            "Tap again to reverse or click button to go to next flashcard"
        ),
        createFlashcard(
            "Each time you open package, flashcards are shuffled \n -- Tap me --",
            "\"Let's repeat!\" puts flashcard at the end of the queue \n \"I knew\" removes flashcard from current queue"
        ),
        createFlashcard(
            "You can add your own flashcard packages, using floating button in menu \n -- Tap me! --",
            "You can fill package with flashcards, using green \"+\" button on package"
        ),
        createFlashcard(
            "You can delete flashcard packages using red \"X\" button on package \n -- Tap me! --",
            "Then you'll have to click \"OK\" on snackbar that appears to complete deletion"
        )
    )

    dao.insertGroup(tutorialGroup)
    val groupInDb = dao.getLatestGroup()
    groupInDb?.groupId?.let { dao.insertFlashcards(it, tutorialFlashcards) }
}