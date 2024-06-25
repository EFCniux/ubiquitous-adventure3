package es.niux.efc.domain.interactors

import es.niux.efc.core.di.coroutines.dispatchers.DefDispatcher
import es.niux.efc.core.entity.Item
import es.niux.efc.core.exception.CoreException
import es.niux.efc.core.util.either.Either
import es.niux.efc.core.util.either.Left
import es.niux.efc.core.util.either.Right
import es.niux.efc.core.util.optional.Optional
import es.niux.efc.core.util.optional.getOr
import es.niux.efc.core.util.optional.getOrNull
import es.niux.efc.core.util.optional.isEmpty
import es.niux.efc.data.repositories.DemoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

private typealias Result = Either<List<Item>, CoreException>

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
            .map { true }
            .onStart { emit(false) }
            .flatMapLatest { forceRefresh ->
                repository
                    .observeItems(forceRefresh = forceRefresh)
                    .map { it.getOr { emptySet() } }
                    .map { Left(it.toList()) }
                    .catch<Result> {
                        if (it is CoreException)
                            this.emit(Right(it))
                        else
                            throw it
                    }
                    .map { Optional(it) }
                    .onStart<Optional<Result>> {
                        this.emit(Optional())
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
