package es.niux.efc.ubiquitous_adventure3.components.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class BaseViewModel : ViewModel() {
    fun <T> Flow<T>.stateIn(
        default: T,
        stopTimeout: Duration = 5.seconds
    ) = this.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeout),
        default
    )
}
