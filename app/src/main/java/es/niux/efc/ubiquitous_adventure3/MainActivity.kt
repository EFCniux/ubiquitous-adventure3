package es.niux.efc.ubiquitous_adventure3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import es.niux.efc.ubiquitous_adventure3.ui.composable.ItemGridPreview
import es.niux.efc.ubiquitous_adventure3.ui.theme.Ubiquitousadventure3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Ubiquitousadventure3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ItemGridPreview()
                }
            }
        }
    }
}
