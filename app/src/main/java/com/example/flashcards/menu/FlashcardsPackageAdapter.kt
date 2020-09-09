package com.example.flashcards.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.database.FlashcardsPackage
import com.example.flashcards.databinding.ListItemFlashcardGroupBinding

class FlashcardsPackageAdapter(private val clickListener: FlashcardsPackageListener) :
    ListAdapter<FlashcardsPackage, FlashcardsPackageAdapter.ViewHolder>(
        FlashcardsPackageDiffCallback()
    ) {

    class ViewHolder private constructor(private val binding: ListItemFlashcardGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FlashcardsPackage, clickListener: FlashcardsPackageListener) {
            binding.flashcardsPackage = item
            binding.clickListener = clickListener

            
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemFlashcardGroupBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}

class FlashcardsPackageDiffCallback : DiffUtil.ItemCallback<FlashcardsPackage>() {
    override fun areItemsTheSame(oldItem: FlashcardsPackage, newItem: FlashcardsPackage): Boolean {
        return oldItem.group.groupId == newItem.group.groupId
    }

    override fun areContentsTheSame(
        oldItem: FlashcardsPackage,
        newItem: FlashcardsPackage
    ): Boolean {
        return oldItem == newItem
    }

}

class FlashcardsPackageListener(
    val clickListener: (groupId: Long) -> Unit,
    val deleteListener: (groupId: Long) -> Unit,
    val addListener: (groupId: Long) -> Unit
) {
    fun onClick(flashcardsPackage: FlashcardsPackage) =
        clickListener(flashcardsPackage.group.groupId)

    fun onDelete(flashcardsPackage: FlashcardsPackage) =
        deleteListener(flashcardsPackage.group.groupId)

    fun onAdd(flashcardsPackage: FlashcardsPackage) =
        addListener(flashcardsPackage.group.groupId)
}