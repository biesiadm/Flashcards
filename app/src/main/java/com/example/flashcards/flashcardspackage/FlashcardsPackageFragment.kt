package com.example.flashcards.flashcardspackage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.flashcards.R
import com.example.flashcards.database.FlashcardsDatabase
import com.example.flashcards.databinding.FlashcardsPackageFragmentBinding
import com.example.flashcards.resetActionBar
import com.example.flashcards.setActionBar
import com.example.flashcards.setActionBarSubtitle

class FlashcardsPackageFragment : Fragment() {

    private lateinit var flashcardsPackageViewModel: FlashcardsPackageViewModel
    private lateinit var binding: FlashcardsPackageFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resetActionBar(activity)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.flashcards_package_fragment, container, false
        )

        val application = requireNotNull(this.activity).application
        val args = FlashcardsPackageFragmentArgs.fromBundle(requireArguments())

        val dataSource = FlashcardsDatabase.getInstance(application).flashcardsDatabaseDao
        val viewModelFactory =
            FlashcardsPackageViewModelFactory(args.flashcardsGroupKey, dataSource)

        flashcardsPackageViewModel = ViewModelProvider(this, viewModelFactory)
            .get(FlashcardsPackageViewModel::class.java)

        binding.flashcardsPackageViewModel = flashcardsPackageViewModel

        binding.lifecycleOwner = this

        createObservers()

        return binding.root
    }

    private fun createObservers() {
        flashcardsPackageViewModel.changeFlashcardSide.observe(viewLifecycleOwner, {
            if (it == true) {
                flashcardsPackageViewModel.changeSide()

                flashcardsPackageViewModel.doneChangingSide()
            }
        })

        flashcardsPackageViewModel.currentSide.observe(viewLifecycleOwner, {
            displaySide(it)
        })

        flashcardsPackageViewModel.currentFlashcard.observe(viewLifecycleOwner, {
            flashcardsPackageViewModel.currentSide.value?.let {
                displaySide(it)

                setActionBarSubtitle(
                    activity,
                    getString(
                        R.string.subtitle_flashcards_package,
                        flashcardsPackageViewModel.getFlashcardsLeftSize()
                    )
                )
            }
        })

        flashcardsPackageViewModel.eventFlashcardsFinish.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController().navigate(
                    FlashcardsPackageFragmentDirections
                        .actionFlashcardsPackageFragmentToFlashcardsMenuFragment()
                )

                flashcardsPackageViewModel.onFlashcardsFinishComplete()
            }
        })

        flashcardsPackageViewModel.packageTitle.observe(viewLifecycleOwner, {
            it?.let {
                setActionBar(activity, getString(R.string.title_flashcards_package, it))

                if (flashcardsPackageViewModel.flashcardsListInitialised) {
                    setActionBarSubtitle(
                        activity,
                        getString(
                            R.string.subtitle_flashcards_package,
                            flashcardsPackageViewModel.getFlashcardsLeftSize()
                        )
                    )
                }
            }
        })
    }

    private fun displaySide(side: Side) {
        when (side) {
            Side.BACK -> displayBackSide()
            else -> displayFrontSide()
        }
    }

    private fun displayFrontSide() {
        binding.flashcardSide.text = Side.FRONT.toString()
        binding.flashcardText.text = flashcardsPackageViewModel.currentFlashcard.value?.front

        binding.correctButton.visibility = View.INVISIBLE
        binding.incorrectButton.visibility = View.INVISIBLE
    }

    private fun displayBackSide() {
        binding.flashcardSide.text = Side.BACK.toString()
        binding.flashcardText.text = flashcardsPackageViewModel.currentFlashcard.value?.back

        binding.correctButton.visibility = View.VISIBLE
        binding.incorrectButton.visibility = View.VISIBLE
    }
}