package es.niux.efc.data.sourrces.local.room

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter

class SharedConverters {
    @TypeConverter
    fun stringToUri(
        value: String?
    ) = value
        ?.toUri()

    @TypeConverter
    fun uriToString(
        value: Uri?
    ) = value
        ?.toString()
}
