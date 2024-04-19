package es.niux.efc.data.source.network.demo.api.io.output

data class ItemOutput(
    val id: String,
    val attr: Attributes
) {
    data class Attributes(
        val name: String,
        val desc: String,
        val image: Image
    ) {
        data class Image(
            val url: String
        )
    }
}
