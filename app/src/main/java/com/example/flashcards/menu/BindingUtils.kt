package com.example.flashcards.menu

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.flashcards.database.FlashcardsPackage

@BindingAdapter("flashcardsPackageTitle")
fun TextView.setFlashcardsPackageTitle(item: FlashcardsPackage?) {
    item?.let {
        text = item.group.title
    }
}

@BindingAdapter("flashcardsPackageCardsQuantity")
fun TextView.setFlashcardsPackageCardsQuantity(item: FlashcardsPackage?) {
    item?.let {
        text = item.flashcards.size.toString()
    }
}