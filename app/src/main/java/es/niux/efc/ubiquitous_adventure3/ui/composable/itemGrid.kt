package es.niux.efc.ubiquitous_adventure3.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.niux.efc.core.entity.Item
import es.niux.efc.core.util.res.getDrawableUri
import es.niux.efc.ubiquitous_adventure3.R
import kotlin.random.Random

@Composable
fun ItemGrid(
    values: List<Item>,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 300.dp),
        modifier = modifier
            .fillMaxSize(),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(values) { value ->
            Item(value = value)
        }
    }
}

@Preview
@Composable
fun ItemGridPreview() {
    val image = LocalContext.current
        .getDrawableUri(R.drawable.ic_launcher_foreground)

    ItemGrid(
        values = (1..100)
            .asSequence()
            .map {
                Item(
                    id = "$it",
                    name = "Name",
                    desc = if(Random.nextBoolean())
                        "Description"
                    else
                        "Long\nmultiline\ndescription",
                    image = image,
                )
            }
            .toList(),
        modifier = Modifier
            .padding(16.dp)
    )
}
