package com.example.flashcards.menu

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
import com.example.flashcards.databinding.FlashcardsMenuFragmentBinding
import com.example.flashcards.resetActionBar
import com.example.flashcards.setActionBar
import com.google.android.material.snackbar.Snackbar

private const val EMPTY_PACKAGE_TOAST_TEXT = "Add some flashcards first."

class FlashcardsMenuFragment : Fragment() {

    private lateinit var flashcardsMenuViewModel: FlashcardsMenuViewModel

    private lateinit var adapter: FlashcardsPackageAdapter

    private lateinit var binding: FlashcardsMenuFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        resetActionBar(activity)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.flashcards_menu_fragment,
            container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = FlashcardsDatabase
            .getInstance(application).flashcardsDatabaseDao

        val viewModelFactory = FlashcardsMenuViewModelFactory(dataSource)

        flashcardsMenuViewModel = ViewModelProvider(this, viewModelFactory)
            .get(FlashcardsMenuViewModel::class.java)

        adapter = FlashcardsPackageAdapter(
            FlashcardsPackageListener(
                onClickPackage(),
                onDeletePackage(),
                onAddFlashcard()
            )
        )

        binding.packagesList.adapter = adapter

        binding.lifecycleOwner = this

        binding.flashcardsMenuViewModel = flashcardsMenuViewModel

        createObservers()

        setActionBar(activity, getString(R.string.title_menu))

        return binding.root
    }

    private fun onClickPackage() = { groupId: Long ->
        flashcardsMenuViewModel.navigateToPackage(groupId)
    }

    private fun onDeletePackage() = { groupId: Long ->
        flashcardsMenuViewModel.onShowSnackbar(groupId)
    }

    private fun onAddFlashcard() = { groupId: Long ->
        flashcardsMenuViewModel.onAddFlashcardButtonClicked(groupId)
    }

    private fun createObservers() {
        flashcardsMenuViewModel.flashcardsPackages.observe(viewLifecycleOwner,
            {
                it?.let { adapter.submitList(it) }
            })

        flashcardsMenuViewModel.navigateToFlashcardsPackage.observe(viewLifecycleOwner,
            { groupId ->
                groupId?.let {
                    this.findNavController().navigate(
                        FlashcardsMenuFragmentDirections
                            .actionFlashcardsMenuFragmentToFlashcardsPackageFragment(groupId)
                    )

                    flashcardsMenuViewModel.onFlashcardsPackageNavigated()
                }
            })

        flashcardsMenuViewModel.navigateToFlashcardsGroupCreator.observe(viewLifecycleOwner,
            {
                if (it == true) {
                    this.findNavController().navigate(
                        FlashcardsMenuFragmentDirections
                            .actionFlashcardsMenuFragmentToFlashcardsGroupCreatorFragment()
                    )

                    flashcardsMenuViewModel.onFlashcardsGroupCreatorNavigated()
                }
            })

        flashcardsMenuViewModel.navigateToFlashcardCreator.observe(viewLifecycleOwner,
            { groupId ->
                groupId?.let {
                    this.findNavController().navigate(
                        FlashcardsMenuFragmentDirections
                            .actionFlashcardsMenuFragmentToFlashcardCreatorFragment(groupId)
                    )

                    flashcardsMenuViewModel.onFlashcardCreatorNavigated()
                }
            })

        flashcardsMenuViewModel.displayEmptyPackageToast.observe(viewLifecycleOwner,
            {
                if (it == true) {
                    Toast.makeText(context, EMPTY_PACKAGE_TOAST_TEXT, Toast.LENGTH_LONG).show()

                    flashcardsMenuViewModel.onDisplayEmptyPackageToastDone()
                }
            })

        flashcardsMenuViewModel.showSnackbarEvent.observe(viewLifecycleOwner,
            { groupId ->
                groupId?.let {
                    val deleteSnackbar = Snackbar.make(
                        binding.coordinatorLayout,
                        getString(R.string.delete_snackbar_text), Snackbar.LENGTH_LONG
                    )
                    deleteSnackbar.setAction(R.string.ok_string) {
                        flashcardsMenuViewModel.onDelete(groupId)
                    }
                    deleteSnackbar.show()

                    flashcardsMenuViewModel.onShowSnackbarDone()
                }
            })
    }
}