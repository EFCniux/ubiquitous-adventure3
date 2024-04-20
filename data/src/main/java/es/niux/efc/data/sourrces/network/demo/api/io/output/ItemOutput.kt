package es.niux.efc.data.sourrces.network.demo.api.io.output

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemOutputSet(
    @SerialName("dataCollection")
    val values: Set<Value>
) {
    @Serializable
    data class Value(
        @SerialName("item")
        val value: ItemOutput
    )
}

@Serializable
data class ItemOutput(
    @SerialName("id")
    val id: String,
    @SerialName("attributes")
    val attr: Attributes
) {
    @Serializable
    data class Attributes(
        @SerialName("name")
        val name: String,
        @SerialName("description")
        val desc: String,
        @SerialName("imageInfo")
        val image: Image
    ) {
        @Serializable
        data class Image(
            @SerialName("imageUrl")
            val url: String
        )
    }
}
