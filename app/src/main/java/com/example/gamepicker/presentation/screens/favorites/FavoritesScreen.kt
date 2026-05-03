package com.example.gamepicker.presentation.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.gamepicker.data.local.entity.GameStatus
import com.example.gamepicker.data.local.entity.getDisplayName
import com.example.gamepicker.data.local.entity.getStatusColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedIds by remember { mutableStateOf(setOf<Int>()) }
    var expandedStatusMenuId by remember { mutableStateOf<Int?>(null) }

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
                            onSelect = { toggleSelection(game.gameId) },
                            onRemove = { viewModel.removeFromFavorites(game.gameId) },
                            onClick = {
                                if (!isSelectionMode) {
                                    navController.navigate("details/${game.gameId}")
                                } else {
                                    toggleSelection(game.gameId)
                                }
                            },
                            onStatusChange = { newStatus ->
                                viewModel.updateStatus(game.gameId, newStatus)
                            },
                            expandedStatusMenuId = expandedStatusMenuId,
                            onExpandedChange = { expandedStatusMenuId = it }
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
    onSelect: () -> Unit,
    onRemove: () -> Unit,
    onClick: () -> Unit,
    onStatusChange: (GameStatus) -> Unit,
    expandedStatusMenuId: Int?,
    onExpandedChange: (Int?) -> Unit
) {
    val currentStatus = try {
        GameStatus.valueOf(game.status)
    } catch (e: Exception) {
        GameStatus.PLAN_TO_PLAY
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                .padding(12.dp),
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
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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

                if (!isSelectionMode) {
                    Box {
                        Row(
                            modifier = Modifier
                                .background(
                                    getStatusColor(currentStatus).copy(alpha = 0.2f),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onExpandedChange(game.gameId) }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentStatus.getDisplayName(),
                                style = MaterialTheme.typography.bodySmall,
                                color = getStatusColor(currentStatus)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Изменить статус",
                                tint = getStatusColor(currentStatus),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = expandedStatusMenuId == game.gameId,
                            onDismissRequest = { onExpandedChange(null) }
                        ) {
                            GameStatus.values().forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status.getDisplayName()) },
                                    onClick = {
                                        onStatusChange(status)
                                        onExpandedChange(null)
                                    },
                                    leadingIcon = {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .background(getStatusColor(status), RoundedCornerShape(6.dp))
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
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