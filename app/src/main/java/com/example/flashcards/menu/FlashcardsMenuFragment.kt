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

class FlashcardsMenuFragment : Fragment() {

    private lateinit var flashcardsMenuViewModel: FlashcardsMenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FlashcardsMenuFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.flashcards_menu_fragment,
            container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = FlashcardsDatabase
            .getInstance(application).flashcardsDatabaseDao

        val viewModelFactory = FlashcardsMenuViewModelFactory(dataSource)

        flashcardsMenuViewModel = ViewModelProvider(this, viewModelFactory)
            .get(FlashcardsMenuViewModel::class.java)

        val adapter = FlashcardsPackageAdapter(FlashcardsPackageListener(
            { groupId ->
                Toast.makeText(context, "$groupId clicked", Toast.LENGTH_SHORT).show()

                flashcardsMenuViewModel.onFlashcardsPackageClicked(groupId)
            },
            { groupId ->
                Toast.makeText(context, "$groupId deleted", Toast.LENGTH_SHORT).show()

                flashcardsMenuViewModel.onDelete(groupId)
            }
        ))

        binding.packagesList.adapter = adapter

        binding.lifecycleOwner = this

        binding.flashcardsMenuViewModel = flashcardsMenuViewModel

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

        return binding.root
    }
}