package ru.aasmc.wordify.common.core.utils

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {
    fun io(): CoroutineDispatcher
    fun computation(): CoroutineDispatcher
    fun mainThread(): CoroutineDispatcher
}