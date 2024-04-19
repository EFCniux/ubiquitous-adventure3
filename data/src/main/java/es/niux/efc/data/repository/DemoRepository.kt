package es.niux.efc.data.repository

import es.niux.efc.core.entity.Item
import es.niux.efc.data.parser.item.toEntities
import es.niux.efc.data.source.network.demo.DemoNetworkSource

class DemoRepository(
    private val networkSource: DemoNetworkSource
) {
    // todo: add cache
    suspend fun getItems(): Set<Item> = networkSource
        .retrieveItems()
        .toEntities()
}
