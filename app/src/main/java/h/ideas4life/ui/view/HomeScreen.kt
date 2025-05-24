package h.ideas4life.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lint.kotlin.metadata.Visibility
import dagger.hilt.android.lifecycle.HiltViewModel
import h.ideas4life.viewmodel.IdeaViewModel

@Composable
fun HomeScreen() {
    val viewModel: IdeaViewModel = hiltViewModel()
    val isAddVisible by viewModel.isAddIdeaVisible.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.toggleAddIdea() }) {
                Icon(
                    imageVector = if (isAddVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = "Mostrar/ocultar formulario"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            if (isAddVisible) {
                NewIdeaScreen(viewModel)
                Spacer(modifier = Modifier.height(16.dp))
            }

            IdeaScreen(viewModel)
        }
    }
}