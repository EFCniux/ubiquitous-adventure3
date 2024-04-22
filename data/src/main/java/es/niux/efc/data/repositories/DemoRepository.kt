package es.niux.efc.data.repositories

import es.niux.efc.data.sourrces.cache.demo.DemoCacheSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoRepository @Inject constructor(
    private val cacheSource: DemoCacheSource
) {
    fun observeItems(
        forceRefresh: Boolean = false
    ) = cacheSource
        .retrieveAndObserve(forceRefresh)
}
