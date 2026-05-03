package com.example.gamepicker.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.gamepicker.presentation.theme.Teal

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= state.games.size - 3 &&
                    !state.isLoading &&
                    state.hasMore) {
                    viewModel.loadNextPage()
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Поле поиска
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.searchGames(it) },
            label = { Text("Поиск игр") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )

        // Список игр
        when {
            state.isLoading && state.games.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.error != null && state.games.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Ошибка: ${state.error}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadGames() }) {
                            Text("Повторить")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.games) { game ->
                        GameCard(game = game)
                    }

                    if (state.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameCard(game: com.example.gamepicker.data.remote.dto.GameDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = game.backgroundImage,
                contentDescription = game.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium
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
                    text = "Жанры: ${game.genres.joinToString { it.name }}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}