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
        Text(
            text = "Characters",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.searchCharacters(it) },
            onClearQuery = { viewModel.clearSearch() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Character Image
            AsyncImage(
                model = character.images?.firstOrNull(),
                contentDescription = "${character.name} image",
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp)
            )

            // Character Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium
                )

                character.personal?.clan?.let { clan ->
                    Text(
                        text = "Clan: $clan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                character.personal?.affiliation?.firstOrNull()?.let { affiliation ->
                    Text(
                        text = "Affiliation: $affiliation",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                character.rank?.ninjaRank?.get("Part I")?.let { rank ->
                    Text(
                        text = "Rank: $rank",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}