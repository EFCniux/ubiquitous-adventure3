package es.niux.efc.ubiquitous_adventure3.feature.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import es.niux.efc.ubiquitous_adventure3.components.activity.BaseActivity
import es.niux.efc.ubiquitous_adventure3.ui.composable.ItemGrid
import es.niux.efc.ubiquitous_adventure3.ui.theme.Ubiquitousadventure3Theme

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityUi()
        }
    }
}

@Composable
fun MainActivityUi(
    viewModel: MainViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Ubiquitousadventure3Theme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ItemGrid(values = state.data ?: emptyList())
        }
    }
}
