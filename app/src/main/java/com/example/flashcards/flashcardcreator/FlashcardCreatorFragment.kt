package com.example.flashcards.flashcardcreator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flashcards.R
import com.example.flashcards.database.FlashcardsDatabase
import com.example.flashcards.databinding.FlashcardCreatorFragmentBinding
import com.example.flashcards.resetActionBar
import com.example.flashcards.setActionBar

class FlashcardCreatorFragment : Fragment() {

    private lateinit var flashcardCreatorViewModel: FlashcardCreatorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        resetActionBar(activity)

        val binding: FlashcardCreatorFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.flashcard_creator_fragment, container, false
        )

        val application = requireNotNull(this.activity).application
        val args = FlashcardCreatorFragmentArgs.fromBundle(requireArguments())

        val dataSource = FlashcardsDatabase
            .getInstance(application).flashcardsDatabaseDao

        val viewModelFactory = FlashcardCreatorViewModelFactory(args.flashcardsGroupKey, dataSource)

        flashcardCreatorViewModel = ViewModelProvider(this, viewModelFactory)
            .get(FlashcardCreatorViewModel::class.java)

        binding.lifecycleOwner = this

        binding.flashcardCreatorViewModel = flashcardCreatorViewModel

        flashcardCreatorViewModel.packageTitle.observe(viewLifecycleOwner,
            {
                it?.let {
                    setActionBar(
                        activity,
                        getString(R.string.title_flashcard_creator),
                        getString(R.string.subtitle_flashcard_creator, it)
                    )
                }
            })

        flashcardCreatorViewModel.createNewFlashcardEvent.observe(viewLifecycleOwner,
            {
                if (it == true) {
                    if (binding.frontSideValue.text.isBlank() or binding.backSideValue.text.isBlank()) {
                        Toast.makeText(
                            context, "Flashcard sides cannot be empty",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        flashcardCreatorViewModel.addNewFlashcard(
                            binding.frontSideValue.text.toString(),
                            binding.backSideValue.text.toString()
                        )

                        binding.frontSideValue.text.clear()
                        binding.backSideValue.text.clear()
                    }

                    flashcardCreatorViewModel.onCreateFlashcardDone()
                }
            })

        return binding.root
    }
}