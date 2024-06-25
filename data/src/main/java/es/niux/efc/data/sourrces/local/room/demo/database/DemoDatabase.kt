package es.niux.efc.data.sourrces.local.room.demo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.niux.efc.data.sourrces.local.room.SharedConverters
import es.niux.efc.data.sourrces.local.room.demo.database.item.ItemDao
import es.niux.efc.data.sourrces.local.room.demo.database.item.ItemEntity

const val DEMO_DB_NAME = "demo-db"
const val DEMO_DB_VERSION = 1

@Database(
    version = DEMO_DB_VERSION,
    entities = [
        ItemEntity::class
    ]
)
@TypeConverters(
    SharedConverters::class
)
abstract class DemoDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
