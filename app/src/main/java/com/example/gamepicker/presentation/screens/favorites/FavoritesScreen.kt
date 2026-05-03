package com.example.gamepicker.presentation.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedIds by remember { mutableStateOf(setOf<Int>()) }

    fun toggleSelection(gameId: Int) {
        selectedIds = if (selectedIds.contains(gameId)) {
            selectedIds - gameId
        } else {
            selectedIds + gameId
        }
        if (selectedIds.isEmpty()) {
            isSelectionMode = false
        }
    }

    fun deleteSelected() {
        viewModel.removeMultipleFromFavorites(selectedIds.toList())
        selectedIds = emptySet()
        isSelectionMode = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSelectionMode) {
                        Text("Выбрано: ${selectedIds.size}")
                    } else {
                        Text("Избранное")
                    }
                },
                navigationIcon = {
                    if (isSelectionMode) {
                        IconButton(onClick = {
                            isSelectionMode = false
                            selectedIds = emptySet()
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Выйти")
                        }
                    }
                },
                actions = {
                    if (isSelectionMode && selectedIds.isNotEmpty()) {
                        IconButton(onClick = { deleteSelected() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
                        }
                    } else if (!isSelectionMode && state.favorites.isNotEmpty()) {
                        IconButton(onClick = { isSelectionMode = true }) {
                            Icon(Icons.Default.Checklist, contentDescription = "Выбрать")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.favorites.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Нет избранных игр")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { navController.navigate("home") }) {
                            Text("Добавить игры")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.favorites) { game ->
                        FavoriteCard(
                            game = game,
                            isSelected = selectedIds.contains(game.gameId),
                            isSelectionMode = isSelectionMode,
                            onLongClick = {
                                if (!isSelectionMode) {
                                    isSelectionMode = true
                                    selectedIds = setOf(game.gameId)
                                }
                            },
                            onSelect = { toggleSelection(game.gameId) },
                            onRemove = { viewModel.removeFromFavorites(game.gameId) },
                            onClick = {
                                if (!isSelectionMode) {
                                    navController.navigate("details/${game.gameId}")
                                } else {
                                    toggleSelection(game.gameId)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteCard(
    game: com.example.gamepicker.data.local.entity.FavoriteGameEntity,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onLongClick: () -> Unit,
    onSelect: () -> Unit,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (isSelectionMode) Modifier else Modifier
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .then(
                    if (!isSelectionMode) Modifier
                        .clickable { onLongClick() }
                        .combinedClickable(
                            onClick = {},
                            onLongClick = onLongClick
                        )
                    else Modifier
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onSelect() }
                )
            }

            AsyncImage(
                model = game.backgroundImage,
                contentDescription = game.name,
                modifier = Modifier
                    .size(60.dp, 80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )
                Text(
                    text = game.released?.take(4) ?: "Неизвестно",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Рейтинг: ${game.rating}/5",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Жанры: ${game.genres}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }

            if (!isSelectionMode) {
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}