package es.niux.efc.ubiquitous_adventure3.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    contentHeader: (@Composable LazyStaggeredGridItemScope.() -> Unit)? = null,
    contentFooter: (@Composable LazyStaggeredGridItemScope.() -> Unit)? = null
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 300.dp),
        modifier = modifier
            .fillMaxSize(),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPadding
    ) {
        contentHeader?.let { item(content = it) }

        items(values) { value ->
            Item(value = value)
        }

        contentFooter?.let { item(content = it) }
    }
}

@Preview
@Composable
fun ItemGridPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val image = LocalContext.current
            .getDrawableUri(R.drawable.ic_launcher_foreground)

        ItemGrid(
            values = (1..10)
                .asSequence()
                .map {
                    Item(
                        id = "$it",
                        name = "Name",
                        desc = if (Random.nextBoolean())
                            "Description"
                        else
                            "Long\nmultiline\ndescription",
                        image = image,
                    )
                }
                .toList(),
            contentPadding = PaddingValues(16.dp),
            contentHeader = {
                Text(text = "Header")
            },
            contentFooter = {
                Text(text = "Footer")
            }
        )
    }
}
