package com.naruto.world.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.naruto.world.app.ui.theme.NarutoOrange
import com.naruto.world.app.ui.theme.NinjaBlue

data class CategoryItem(
    val title: String,
    val description: String,
    val icon: @Composable () -> Unit,
    val onClick: () -> Unit,
    val backgroundColor: androidx.compose.ui.graphics.Color
)

@Composable
fun HomeScreen(
    onNavigateToCharacters: () -> Unit,
    onNavigateToClans: () -> Unit,
    onNavigateToVillages: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌟",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Naruto World",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = NarutoOrange
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Discover the Hidden Leaf Village and beyond",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Categories Grid
        val categories = listOf(
            CategoryItem(
                title = "Characters",
                description = "Meet ninjas, heroes, and villains",
                icon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                onClick = onNavigateToCharacters,
                backgroundColor = NarutoOrange
            ),
            CategoryItem(
                title = "Clans",
                description = "Explore powerful ninja families",
                icon = {
                    Text(
                        text = "🏠",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                onClick = onNavigateToClans,
                backgroundColor = NinjaBlue
            ),
            CategoryItem(
                title = "Villages",
                description = "Visit hidden ninja strongholds",
                icon = {
                    Text(
                        text = "🏛️",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                onClick = onNavigateToVillages,
                backgroundColor = MaterialTheme.colorScheme.tertiary
            ),
            CategoryItem(
                title = "Special",
                description = "Kekkei Genkai & Tailed Beasts",
                icon = {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                onClick = { /* TODO: Navigate to special abilities */ },
                backgroundColor = MaterialTheme.colorScheme.secondary
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(categories) { category ->
                CategoryCard(category = category)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryCard(
    category: CategoryItem,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = category.onClick,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = category.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                category.icon()
            }

            // Title
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Description
            Text(
                text = category.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}