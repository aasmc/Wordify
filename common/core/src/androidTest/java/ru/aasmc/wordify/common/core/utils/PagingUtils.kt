package ru.aasmc.wordify.common.core.utils

import androidx.paging.DifferCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import ru.aasmc.wordify.common.core.domain.model.Word

class NoopListCallback : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
}

class MyDiffCallback : DiffUtil.ItemCallback<Word>() {
    override fun areItemsTheSame(
        oldItem: Word,
        newItem: Word
    ): Boolean {
        return oldItem.wordId == newItem.wordId
    }

    override fun areContentsTheSame(
        oldItem: Word,
        newItem: Word
    ): Boolean {
        return oldItem == newItem
    }

}

class MyDifferCallback: DifferCallback {
    override fun onChanged(position: Int, count: Int) {}
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}

}