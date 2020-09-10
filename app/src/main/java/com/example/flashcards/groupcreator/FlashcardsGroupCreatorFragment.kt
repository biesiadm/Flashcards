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
                if (binding.newPackageName.text.toString().isNotBlank()) {
                    flashcardsGroupCreatorViewModel.addNewPackage(binding.newPackageName.text.toString())

                    this.findNavController().navigate(
                        FlashcardsGroupCreatorFragmentDirections
                            .actionFlashcardsGroupCreatorFragmentToFlashcardsMenuFragment()
                    )
                } else {
                    Toast.makeText(context, "Enter package name", Toast.LENGTH_SHORT).show()
                }

                flashcardsGroupCreatorViewModel.onCreateNewPackageDone()
            }
        })

        setActionBar(activity, getString(R.string.title_flashcards_group_creator))

        return binding.root
    }
}