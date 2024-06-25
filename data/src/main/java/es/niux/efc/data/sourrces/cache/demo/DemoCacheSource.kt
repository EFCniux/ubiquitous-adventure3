package es.niux.efc.data.sourrces.cache.demo

import es.niux.efc.core.entity.Item
import es.niux.efc.core.util.optional.Optional
import es.niux.efc.data.converters.item.tryConvertAll
import es.niux.efc.data.converters.item.toItemEntity
import es.niux.efc.data.sourrces.cache.CacheSource
import es.niux.efc.data.sourrces.local.room.demo.DemoRoomSource
import es.niux.efc.data.sourrces.local.room.demo.database.item.ItemEntity
import es.niux.efc.data.sourrces.network.demo.DemoNetworkSource
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
class DemoCacheSource @Inject constructor(
    private val networkSource: DemoNetworkSource,
    private val demoDatabase: DemoRoomSource
) : CacheSource<Set<Item>>(
    staleTime = 30.seconds
) {
    override suspend fun cacheCreate() = networkSource
        .retrieveItems()
        .tryConvertAll()

    override suspend fun cacheRead() = demoDatabase
        .getItems()
        .toOptionalEntities()

    override suspend fun cacheUpdate(
        value: Set<Item>
    ) = demoDatabase
        .setItems(
            value
                .asSequence()
                .map { it.toItemEntity() }
                .toList()
        )

    override suspend fun cacheDelete() = demoDatabase
        .deleteItems()

    override fun observe() = demoDatabase
        .observeItems()
        .map { it.toOptionalEntities() }
        .distinctUntilChanged()

    private suspend fun List<ItemEntity>.toOptionalEntities() = this
        .takeIf { it.isNotEmpty() }
        ?.let { Optional(it.tryConvertAll()) }
        ?: Optional()
}
