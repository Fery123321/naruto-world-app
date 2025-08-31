package com.naruto.world.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.naruto.world.app.data.model.Character
import com.naruto.world.app.ui.components.ErrorScreen
import com.naruto.world.app.ui.components.LoadingScreen
import com.naruto.world.app.viewmodel.CharacterDetailState
import com.naruto.world.app.viewmodel.CharacterDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterId: Long,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterDetailViewModel = koinViewModel(parameters = { parametersOf(characterId) })
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                is CharacterDetailState.Loading -> {
                    LoadingScreen()
                }
                is CharacterDetailState.Success -> {
                    val character = (state as CharacterDetailState.Success).character
                    CharacterDetailContent(character = character)
                }
                is CharacterDetailState.Error -> {
                    ErrorScreen(
                        message = (state as CharacterDetailState.Error).message,
                        onRetry = { viewModel.retry() }
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailContent(character: Character) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Character Image
        AsyncImage(
            model = character.images?.firstOrNull(),
            contentDescription = "${character.name} image",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop
        )

        // Character Name
        Text(
            text = character.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Basic Information Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Basic Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                character.personal?.let { personal ->
                    personal.sex?.let {
                        InfoRow(label = "Gender", value = it)
                    }
                    personal.birthdate?.let {
                        InfoRow(label = "Birthdate", value = it)
                    }
                    personal.clan?.let {
                        InfoRow(label = "Clan", value = it)
                    }
                    personal.affiliation?.firstOrNull()?.let {
                        InfoRow(label = "Affiliation", value = it)
                    }
                    personal.status?.let {
                        InfoRow(label = "Status", value = it)
                    }
                }

                character.rank?.ninjaRank?.get("Part I")?.let {
                    InfoRow(label = "Rank", value = it)
                }
            }
        }

        // Physical Information Card
        character.personal?.let { personal ->
            if (personal.height != null || personal.weight != null || personal.bloodType != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Physical Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        personal.height?.get("Part I")?.let {
                            InfoRow(label = "Height", value = it)
                        }
                        personal.weight?.get("Part I")?.let {
                            InfoRow(label = "Weight", value = it)
                        }
                        personal.bloodType?.let {
                            InfoRow(label = "Blood Type", value = it)
                        }
                    }
                }
            }
        }

        // Abilities Card
        character.personal?.let { personal ->
            if (personal.kekkeiGenkai != null || character.jutsu != null || character.natureType != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Abilities",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        personal.kekkeiGenkai?.let { kekkeiGenkai ->
                            InfoRow(label = "Kekkei Genkai", value = kekkeiGenkai.joinToString(", "))
                        }

                        character.jutsu?.take(3)?.let { jutsu ->
                            InfoRow(label = "Notable Jutsu", value = jutsu.joinToString(", "))
                        }

                        character.natureType?.let { natureTypes ->
                            InfoRow(label = "Nature Types", value = natureTypes.joinToString(", "))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}