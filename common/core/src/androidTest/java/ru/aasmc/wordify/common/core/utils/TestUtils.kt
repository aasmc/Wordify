package ru.aasmc.wordify.common.core.utils

import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlin.math.abs
import kotlin.math.max

/**
 * Function to compare floating point numbers.
 */
fun Double.equalsDelta(other: Double) =
    abs(this - other) < max(Math.ulp(this), Math.ulp(other)) * 2

fun Float.equalsDelta(other: Float) =
    abs(this - other) < max(Math.ulp(this), Math.ulp(other)) * 2


@ExperimentalCoroutinesApi
suspend fun <T : Any> PagingData<T>.collectData(testDispatcher: CoroutineDispatcher): List<T> {
    val dcb = object : DifferCallback {
        override fun onChanged(position: Int, count: Int) {}
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
    }
    val items = mutableListOf<T>()
    val dif = object : PagingDataDiffer<T>(dcb, testDispatcher) {
        override suspend fun presentNewList(
            previousList: NullPaddedList<T>,
            newList: NullPaddedList<T>,
            lastAccessedIndex: Int,
            onListPresentable: () -> Unit
        ): Int? {
            for (idx in 0 until newList.size)
                items.add(newList.getFromStorage(idx))
            onListPresentable()
            return null
        }
    }
    dif.collectFrom(this)
    return items
}