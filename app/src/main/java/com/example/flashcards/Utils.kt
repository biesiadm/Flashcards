package com.example.flashcards

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

fun resetActionBar(activity: FragmentActivity?) {
    (activity as AppCompatActivity).supportActionBar?.title = ""
    (activity as AppCompatActivity).supportActionBar?.subtitle = ""
}

fun setActionBar(activity: FragmentActivity?, title: String, subtitle: String = "") {
    (activity as AppCompatActivity).supportActionBar?.title = title
    (activity as AppCompatActivity).supportActionBar?.subtitle = subtitle
}

fun setActionBarSubtitle(activity: FragmentActivity?, subtitle: String) {
    (activity as AppCompatActivity).supportActionBar?.subtitle = subtitle
}