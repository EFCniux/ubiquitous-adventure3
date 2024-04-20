package es.niux.efc.data.sourrces.network.demo

import es.niux.efc.core.di.coroutines.dispatchers.IoDispatcher
import es.niux.efc.data.sourrces.network.demo.api.DemoNetworkApi
import es.niux.efc.data.sourrces.network.tryNetworkRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoNetworkSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val api: DemoNetworkApi
) {
    suspend fun retrieveItems() = tryNetworkRequest(ioDispatcher) {
        api.retrieveItems()
            .values
            .asSequence()
            .map { it.value }
            .toSet()
    }
}
