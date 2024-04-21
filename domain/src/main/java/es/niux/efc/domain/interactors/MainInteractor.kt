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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        reload: Flow<Unit>
    ) = flow {
        val stateMutex = Mutex()
        var state = State()

        suspend fun update(
            action: suspend (State) -> State
        ) = stateMutex.withLock {
            state = action(state)
            state
        }

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
                emit(update { prevState ->
                    prevState.copy(
                        isLoading = optResult.isEmpty,
                        // Null if never loaded
                        data = optResult.getOrNull()?.leftOrNull() ?: prevState.data,
                        // Null if loading or no error was produced
                        error = optResult.getOrNull()?.rightOrNull()
                    )
                })
            }
    }.flowOn(defDispatcher)
}
