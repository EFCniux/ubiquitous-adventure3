package es.niux.efc.data.parser.item

import androidx.core.net.toUri
import es.niux.efc.core.entity.Item
import es.niux.efc.data.parser.tryParse
import es.niux.efc.data.source.network.demo.api.io.output.ItemOutput
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet

private fun ItemOutput.parse() = Item(
    id = this.id,
    name = this.attr.name,
    desc = this.attr.desc,
    image = this.attr.image.url.toUri()
)

suspend fun ItemOutput.toEntity() = tryParse { this.parse() }

suspend fun Iterable<ItemOutput>.toEntities() = this
    .asFlow()
    .map { tryParse { it.parse() } }
    .toSet()
