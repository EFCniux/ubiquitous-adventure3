package es.niux.efc.core.entity

import android.net.Uri

data class Item(
    val id: String,
    val name: String,
    val desc: String,
    val image: Uri
)
