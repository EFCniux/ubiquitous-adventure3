package es.niux.efc.data.sourrces.local.room.demo

import es.niux.efc.core.di.coroutines.dispatchers.IoDispatcher
import es.niux.efc.data.sourrces.local.room.demo.database.DemoDatabase
import es.niux.efc.data.sourrces.local.room.demo.database.item.ItemEntity
import es.niux.efc.data.sourrces.local.room.tryDatabaseFlow
import es.niux.efc.data.sourrces.local.room.tryDatabaseTask
import es.niux.efc.data.sourrces.local.room.trySusDatabaseTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoRoomSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val database: DemoDatabase
) {
    suspend fun setItems(
        items: Collection<ItemEntity>
    ) = trySusDatabaseTask(ioDispatcher) {
        database
            .itemDao()
            .replaceAll(*items.toTypedArray())
    }

    suspend fun getItems() = trySusDatabaseTask(ioDispatcher) {
        database
            .itemDao()
            .getAll()
    }

    fun observeItems() = tryDatabaseTask {
        database
            .itemDao()
            .observeAll()
            .tryDatabaseFlow(ioDispatcher)
    }

    suspend fun deleteItems() = trySusDatabaseTask(ioDispatcher) {
        database
            .itemDao()
            .deleteAll()
    }
}
