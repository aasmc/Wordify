package ru.aasmc.wordify.common.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CoroutineDispatchersProvider : DispatchersProvider {
    override fun io(): CoroutineDispatcher =
        Dispatchers.IO


    override fun computation(): CoroutineDispatcher =
        Dispatchers.Default


    override fun mainThread(): CoroutineDispatcher =
        Dispatchers.Main


}