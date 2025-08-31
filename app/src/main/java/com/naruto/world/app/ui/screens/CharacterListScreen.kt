package com.naruto.world.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.naruto.world.app.data.model.Character
import com.naruto.world.app.ui.components.ErrorScreen
import com.naruto.world.app.ui.components.LoadingScreen
import com.naruto.world.app.viewmodel.CharacterListState
import com.naruto.world.app.viewmodel.CharacterListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterListScreen(
    onCharacterClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        // Header with Naruto theme
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "👥",
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = "Ninja Characters",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Meet the heroes and villains of the Naruto world",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.searchCharacters(it) },
            onClearQuery = { viewModel.clearSearch() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        when (state) {
            is CharacterListState.Loading -> {
                LoadingScreen()
            }
            is CharacterListState.Success -> {
                val characters = (state as CharacterListState.Success).characters
                CharacterList(
                    characters = characters,
                    onCharacterClick = onCharacterClick,
                    onLoadMore = { viewModel.loadMoreCharacters() }
                )
            }
            is CharacterListState.Error -> {
                ErrorScreen(
                    message = (state as CharacterListState.Error).message,
                    onRetry = { viewModel.retry() }
                )
            }
        }
    }
}


@Composable
private fun CharacterList(
    characters: List<Character>,
    onCharacterClick: (Long) -> Unit,
    onLoadMore: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(characters) { character ->
            CharacterItem(
                character = character,
                onClick = { onCharacterClick(character.id) }
            )
        }

        item {
            Button(
                onClick = onLoadMore,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Load More")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search characters...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearQuery) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear search"
                    )
                }
            }
        },
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterItem(
    character: Character,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Character Image with placeholder
            Surface(
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                AsyncImage(
                    model = character.images?.firstOrNull(),
                    contentDescription = "${character.name} image",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Character Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Character details in a more organized way
                val details = mutableListOf<String>()

                character.personal?.clan?.let { clan ->
                    details.add("🏠 $clan")
                }

                character.personal?.affiliation?.firstOrNull()?.let { affiliation ->
                    details.add("🏛️ $affiliation")
                }

                character.rank?.ninjaRank?.get("Part I")?.let { rank ->
                    details.add("⭐ $rank")
                }

                if (details.isNotEmpty()) {
                    Text(
                        text = details.joinToString(" • "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                // Status indicator
                character.personal?.status?.let { status ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = when (status.lowercase()) {
                            "alive" -> MaterialTheme.colorScheme.primaryContainer
                            "deceased" -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = status,
                            style = MaterialTheme.typography.labelSmall,
                            color = when (status.lowercase()) {
                                "alive" -> MaterialTheme.colorScheme.onPrimaryContainer
                                "deceased" -> MaterialTheme.colorScheme.onErrorContainer
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Navigation indicator
            Text(
                text = "→",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}