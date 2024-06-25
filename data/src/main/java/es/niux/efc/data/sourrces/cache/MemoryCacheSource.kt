package es.niux.efc.data.sourrces.cache

import es.niux.efc.core.util.optional.Optional
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration

abstract class MemoryCacheSource<T>(
    staleTime: Duration
) : CacheSource<T>(
    staleTime
) {
    private val cache = MutableStateFlow<Optional<T>>(Optional())

    override suspend fun cacheRead() = cache.value

    override suspend fun cacheUpdate(value: T) {
        cache.value = Optional(value)
    }

    override suspend fun cacheDelete() {
        cache.value = Optional()
    }

    override fun observe() = cache
}
