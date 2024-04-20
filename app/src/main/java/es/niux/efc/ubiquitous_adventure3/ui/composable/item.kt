package es.niux.efc.ubiquitous_adventure3.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import es.niux.efc.core.entity.Item
import es.niux.efc.core.util.res.getDrawableUri
import es.niux.efc.ubiquitous_adventure3.R

@Composable
fun Item(
    value: Item,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(value.image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(3f / 2f),
                placeholder = painterResource(
                    id = R.drawable.ic_launcher_foreground
                )
            )
            Spacer(
                modifier = Modifier
                    .height(height = 8.dp)
            )
            Text(
                text = "${value.id} - ${value.name}",
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = value.desc,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(
                modifier = Modifier
                    .height(height = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun ItemPreview() {
    Item(
        value = Item(
            id = "1",
            name = "Name",
            desc = "Description",
            image = LocalContext.current
                .getDrawableUri(R.drawable.ic_launcher_foreground),
        ),
        modifier = Modifier
            .padding(16.dp)
    )
}
