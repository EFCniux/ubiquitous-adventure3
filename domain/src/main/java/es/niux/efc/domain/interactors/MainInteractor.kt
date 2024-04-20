package es.niux.efc.domain.interactors

import es.niux.efc.core.di.coroutines.dispatchers.DefDispatcher
import es.niux.efc.core.entity.Item
import es.niux.efc.core.exception.CoreException
import es.niux.efc.core.util.either.Left
import es.niux.efc.core.util.either.Right
import es.niux.efc.core.util.optional.Optional
import es.niux.efc.core.util.optional.getOrNull
import es.niux.efc.core.util.optional.isEmpty
import es.niux.efc.data.repositories.DemoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainInteractor @Inject constructor(
    @DefDispatcher private val defDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val repository: DemoRepository
) {
    data class State(
        val isLoading: Boolean = false,
        val data: List<Item>? = null,
        val error: CoreException? = null
    )

    operator fun invoke(
        reload: Flow<Unit>
    ) = channelFlow {
        val state = MutableStateFlow(State())

        launch {
            reload
                .onStart { emit(Unit) }
                .flatMapLatest {
                    flow {
                        emit(Optional())

                        val value = try {
                            Left(
                                repository
                                    .getItems()
                                    .toList()
                            )
                        } catch (e: CoreException) {
                            Right(e)
                        }

                        emit(Optional(value))
                    }
                }
                .collect { optResult ->
                    state.update { prevState ->
                        prevState.copy(
                            isLoading = optResult.isEmpty,
                            // Null if never loaded
                            data = optResult.getOrNull()?.leftOrNull() ?: prevState.data,
                            // Null if loading or no error was produced
                            error = optResult.getOrNull()?.rightOrNull()
                        )
                    }
                }
        }

        state
            .collect { send(it) }
    }.flowOn(defDispatcher)
}
