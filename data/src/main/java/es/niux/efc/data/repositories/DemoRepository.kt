package es.niux.efc.data.repositories

import es.niux.efc.core.entity.Item
import es.niux.efc.data.parsers.item.toEntities
import es.niux.efc.data.sourrces.network.demo.DemoNetworkSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoRepository @Inject constructor(
    private val networkSource: DemoNetworkSource
) {
    // todo: add cache
    suspend fun getItems(): Set<Item> = networkSource
        .retrieveItems()
        .toEntities()
}
