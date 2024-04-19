package es.niux.efc.data.source.network.demo

import es.niux.efc.data.source.network.demo.api.DemoNetworkApi
import es.niux.efc.data.source.network.tryNetworkRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DemoNetworkSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val api: DemoNetworkApi
) {
    suspend fun retrieveItems() = tryNetworkRequest(ioDispatcher) {
        api.retrieveItems()
    }
}
