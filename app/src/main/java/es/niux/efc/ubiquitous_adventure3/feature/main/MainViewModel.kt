package es.niux.efc.ubiquitous_adventure3.feature.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.niux.efc.domain.interactors.MainInteractor
import es.niux.efc.ubiquitous_adventure3.components.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    interactor: MainInteractor
) : BaseViewModel() {
    private val reload = MutableSharedFlow<Unit>()

    val state = interactor
        .invoke(reload = reload)
        .stateIn(default = MainInteractor.State())

    fun reload() {
        viewModelScope.launch { reload.emit(Unit) }
    }
}
