package es.niux.efc.data.sourrces.local.room.demo.database.item

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val ITEMS_TABLE = "items"
const val ITEMS_COL_NAME = "name"
const val ITEMS_COL_DESC = "desc"
const val ITEMS_COL_IMAGE = "image"

@Entity(tableName = ITEMS_TABLE)
data class ItemEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = ITEMS_COL_NAME)
    val name: String,
    @ColumnInfo(name = ITEMS_COL_DESC)
    val desc: String,
    @ColumnInfo(name = ITEMS_COL_IMAGE)
    val image: Uri
)
