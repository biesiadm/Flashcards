package com.example.flashcards.groupcreator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.flashcards.R
import com.example.flashcards.database.FlashcardsDatabase
import com.example.flashcards.databinding.FlashcardsGroupCreatorFragmentBinding
import com.example.flashcards.resetActionBar
import com.example.flashcards.setActionBar

private const val MAX_PACKAGE_TITLE_LENGTH = 25
private const val EMPTY_PACKAGE_TITLE_TOAST = "Enter package name"
private const val TO_LONG_PACKAGE_TITLE_TOAST = "Package name can be up to $MAX_PACKAGE_TITLE_LENGTH letters"

class FlashcardsGroupCreatorFragment : Fragment() {

    private lateinit var flashcardsGroupCreatorViewModel: FlashcardsGroupCreatorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        resetActionBar(activity)

        val binding: FlashcardsGroupCreatorFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.flashcards_group_creator_fragment,
            container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = FlashcardsDatabase
            .getInstance(application).flashcardsDatabaseDao

        val viewModelFactory = FlashcardsGroupCreatorViewModelFactory(dataSource)

        flashcardsGroupCreatorViewModel = ViewModelProvider(this, viewModelFactory)
            .get(FlashcardsGroupCreatorViewModel::class.java)

        binding.lifecycleOwner = this

        binding.flashcardsGroupCreatorViewModel = flashcardsGroupCreatorViewModel

        flashcardsGroupCreatorViewModel.createNewPackageEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                val newPackageName = binding.newPackageName.text.toString()

                when {
                    newPackageName.isBlank() -> {
                        Toast.makeText(context, EMPTY_PACKAGE_TITLE_TOAST, Toast.LENGTH_SHORT).show()
                    }
                    newPackageName.length > MAX_PACKAGE_TITLE_LENGTH -> {
                        Toast.makeText(context, TO_LONG_PACKAGE_TITLE_TOAST, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        flashcardsGroupCreatorViewModel.addNewPackage(newPackageName)

                        this.findNavController().navigate(
                            FlashcardsGroupCreatorFragmentDirections
                                .actionFlashcardsGroupCreatorFragmentToFlashcardsMenuFragment()
                        )
                    }
                }

                flashcardsGroupCreatorViewModel.onCreateNewPackageDone()
            }
        })

        setActionBar(activity, getString(R.string.title_flashcards_group_creator))

        return binding.root
    }
}