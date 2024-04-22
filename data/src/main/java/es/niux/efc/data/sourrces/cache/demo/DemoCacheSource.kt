package es.niux.efc.data.sourrces.cache.demo

import es.niux.efc.core.entity.Item
import es.niux.efc.data.parsers.item.toEntities
import es.niux.efc.data.sourrces.cache.MemoryCacheSource
import es.niux.efc.data.sourrces.network.demo.DemoNetworkSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
class DemoCacheSource @Inject constructor(
    private val networkSource: DemoNetworkSource
) : MemoryCacheSource<Set<Item>>(
    staleTime = 30.seconds
) {
    override suspend fun cacheCreate() = networkSource
        .retrieveItems()
        .toEntities()
}
