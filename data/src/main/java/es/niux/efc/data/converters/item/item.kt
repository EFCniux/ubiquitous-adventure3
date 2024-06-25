package es.niux.efc.data.converters.item

import androidx.core.net.toUri
import es.niux.efc.core.entity.Item
import es.niux.efc.data.converters.tryConvertLocalDatabase
import es.niux.efc.data.converters.tryConvertNetwork
import es.niux.efc.data.sourrces.local.room.demo.database.item.ItemEntity
import es.niux.efc.data.sourrces.network.demo.api.io.output.ItemOutput
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet

// region Network
private fun ItemOutput.toItem() = Item(
    id = this.id,
    name = this.attr.name,
    desc = this.attr.desc,
    image = this.attr.image.url.toUri()
)

private fun Item.toItemOutput() = ItemOutput(
    id = this.id,
    attr = ItemOutput.Attributes(
        name = this.name,
        desc = this.desc,
        image = ItemOutput.Attributes.Image(
            url = this.image.toString()
        )
    )
)

suspend fun ItemOutput.tryConvert() = tryConvertNetwork { this.toItem() }

@JvmName("tryAsItemOutputConvertAll")
suspend fun Iterable<ItemOutput>.tryConvertAll() = this
    .asFlow()
    .map { tryConvertNetwork { it.toItem() } }
    .toSet()
// endregion

// region Database
private fun ItemEntity.toItem() = Item(
    id = this.id,
    name = this.name,
    desc = this.desc,
    image = this.image
)

fun Item.toItemEntity() = ItemEntity(
    id = this.id,
    name = this.name,
    desc = this.desc,
    image = this.image
)

suspend fun ItemEntity.tryConvert() = tryConvertLocalDatabase { this.toItem() }

@JvmName("tryAsItemEntityConvertAll")
suspend fun Iterable<ItemEntity>.tryConvertAll() = this
    .asFlow()
    .map { tryConvertLocalDatabase { it.toItem() } }
    .toSet()
// endregion
