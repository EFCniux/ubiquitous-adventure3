package es.niux.efc.ubiquitous_adventure3.feature.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.SnackbarResult.Dismissed
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import es.niux.efc.core.exception.CoreException
import es.niux.efc.ubiquitous_adventure3.R
import es.niux.efc.ubiquitous_adventure3.components.activity.BaseActivity
import es.niux.efc.ubiquitous_adventure3.ui.composable.ItemGrid
import es.niux.efc.ubiquitous_adventure3.ui.theme.Ubiquitousadventure3Theme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityUi() }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainActivityUi(
    viewModel: MainViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    Ubiquitousadventure3Theme {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.reload() }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = stringResource(
                                    R.string.action_retry_error
                                )
                            )
                        }
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) { scaffoldPadding ->
            Box(
                modifier = Modifier
                    .padding(scaffoldPadding)
            ) {
                ItemGrid(
                    values = state.data ?: emptyList(),
                    contentPadding = PaddingValues(16.dp)
                )
                if (state.isLoading) LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }

    state.error?.let { error ->
        val errorMsg = when (error) {
            is CoreException.Network.Connection -> stringResource(
                id = R.string.error_network_connection
            )

            is CoreException.Network.Server -> stringResource(
                id = R.string.error_network_server,
                error.code
            )

            is CoreException.Network.Parse -> stringResource(
                id = R.string.error_network_parse
            )
        }

        val action = stringResource(
            R.string.action_retry_error
        )

        coroutineScope.launch {
            snackBarHostState
                .showSnackbar(
                    message = errorMsg,
                    actionLabel = action,
                    duration = SnackbarDuration.Indefinite
                )
                .let {
                    when (it) {
                        Dismissed -> Unit
                        ActionPerformed -> viewModel
                            .reload()
                    }
                }
        }
    } ?: run {
        snackBarHostState
            .currentSnackbarData
            ?.dismiss()
    }
}
