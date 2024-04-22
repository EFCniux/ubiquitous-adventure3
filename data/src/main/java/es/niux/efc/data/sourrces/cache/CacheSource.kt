package es.niux.efc.data.sourrces.cache

import es.niux.efc.core.util.optional.Optional
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.TimeSource

abstract class CacheSource<T>(
    private val staleTime: Duration
) {
    protected val mutex = Mutex()
    protected open var lastUpdate: TimeSource.Monotonic.ValueTimeMark? = null
    private var isStale
        get() = lastUpdate
            ?.let { it.elapsedNow() > staleTime }
            ?: true
        set(isStale) {
            lastUpdate = if (!isStale)
                TimeSource.Monotonic.markNow()
            else
                null
        }

    protected abstract suspend fun cacheCreate(): T

    protected abstract suspend fun cacheRead(): Optional<T>

    protected abstract suspend fun cacheUpdate(value: T)

    protected abstract suspend fun cacheDelete()

    private suspend fun refreshCache() = cacheCreate()
        .also { cacheUpdate(it) }
        .also { isStale = false }

    suspend fun read() = mutex.withLock {
        cacheRead()
    }

    suspend fun retrieve(
        forceRefresh: Boolean
    ) = mutex.withLock {
        if (isStale || forceRefresh)
            refreshCache()
        else cacheRead()
            .fold(ifLeft = {
                it
            }, ifRight = {
                refreshCache()
            })
    }

    abstract fun observe(): Flow<Optional<T>>

    fun retrieveAndObserve(
        forceRefresh: Boolean
    ) = observe()
        .onStart {
            val value = retrieve(forceRefresh = forceRefresh)
            emit(Optional(value))
        }
        .distinctUntilChanged()
}
