package com.naruto.world.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.naruto.world.app.data.model.Clan
import com.naruto.world.app.ui.components.LoadingScreen
import com.naruto.world.app.ui.navigation.Screen
import com.naruto.world.app.viewmodel.ClanListState
import com.naruto.world.app.viewmodel.ClanListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClanListScreen(
    navController: NavController,
    viewModel: ClanListViewModel = koinViewModel()
) {
    val clanListState by viewModel.clanListState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val hasMorePages by viewModel.hasMorePages.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Naruto Clans", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6B35),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchClans(it) },
                placeholder = { Text("Search clans...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF6B35),
                    focusedLeadingIconColor = Color(0xFFFF6B35)
                )
            )

            // Content
            when (val state = clanListState) {
                is ClanListState.Loading -> {
                    LoadingScreen()
                }
                is ClanListState.Success -> {
                    if (state.clans.isEmpty()) {
                        EmptyState()
                    } else {
                        ClanList(
                            clans = state.clans,
                            onClanClick = { clan ->
                                navController.navigate(Screen.ClanDetail.createRoute(clan.id))
                            },
                            onLoadMore = { viewModel.loadMoreClans() },
                            isLoadingMore = isLoadingMore,
                            hasMorePages = hasMorePages,
                            listState = listState,
                            viewModel = viewModel
                        )
                    }
                }
                is ClanListState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.refreshClans() }
                    )
                }
            }
        }
    }
}

@Composable
private fun ClanList(
    clans: List<Clan>,
    onClanClick: (Clan) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingMore: Boolean,
    hasMorePages: Boolean,
    listState: LazyListState,
    viewModel: ClanListViewModel
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(clans) { clan ->
            ClanCard(
                clan = clan,
                onClick = { onClanClick(clan) }
            )
        }

        // Load more indicator
        if (hasMorePages && clans.isNotEmpty()) {
            item {
                if (isLoadingMore) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = Color(0xFFFF6B35)
                        )
                    }
                } else {
                    // Invisible item to trigger load more when approaching end
                    Spacer(modifier = Modifier.height(1.dp))
                }
            }
        }
    }

    // Trigger load more when user scrolls near the end
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex: Int? ->
                lastVisibleItemIndex?.let {
                    if (viewModel.shouldLoadMoreClans(it)) {
                        viewModel.loadMoreClans()
                    }
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClanCard(
    clan: Clan,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFF6B35).copy(alpha = 0.1f),
                                Color(0xFFF7931E).copy(alpha = 0.1f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Clan Symbol/Icon placeholder
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFF6B35)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = clan.name.firstOrNull()?.toString() ?: "C",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = clan.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )

                    clan.description?.let { description ->
                        Text(
                            text = description.take(100) + if (description.length > 100) "..." else "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF718096),
                            maxLines = 2
                        )
                    }

                    clan.characters?.let { characters ->
                        Text(
                            text = "${characters.size} members",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFFF6B35),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No clans found",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF718096),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Oops! Something went wrong",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFFE53E3E),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF718096),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B35)
            )
        ) {
            Text("Try Again")
        }
    }
}