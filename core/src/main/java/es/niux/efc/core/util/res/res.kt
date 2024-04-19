package es.niux.efc.core.util.res

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import androidx.annotation.DrawableRes

fun Context.getDrawableUri(
    @DrawableRes id: Int
) = Uri.Builder()
    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
    .authority(resources.getResourcePackageName(id))
    .appendPath(resources.getResourceTypeName(id))
    .appendPath(resources.getResourceEntryName(id))
    .build()
    ?: throw Resources.NotFoundException("No resource found with id: $id")
