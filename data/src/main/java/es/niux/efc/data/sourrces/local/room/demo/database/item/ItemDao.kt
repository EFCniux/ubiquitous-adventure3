package es.niux.efc.data.sourrces.local.room.demo.database.item

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ItemDao {
    @Insert
    abstract suspend fun insert(
        vararg values: ItemEntity
    )

    @Query("SELECT * FROM $ITEMS_TABLE")
    abstract suspend fun getAll(): List<ItemEntity>

    @Query("SELECT * FROM $ITEMS_TABLE")
    abstract fun observeAll(): Flow<List<ItemEntity>>

    @Query("DELETE FROM $ITEMS_TABLE")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun replaceAll(
        vararg values: ItemEntity
    ) {
        deleteAll()
        insert(*values)
    }
}
