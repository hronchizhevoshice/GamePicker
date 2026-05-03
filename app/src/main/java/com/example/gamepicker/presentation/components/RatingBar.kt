package com.example.gamepicker.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxStars: Int = 5
) {
    var currentRating by remember { mutableStateOf(rating) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(maxStars) { index ->
            val starNumber = index + 1
            Icon(
                imageVector = if (starNumber <= currentRating) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Оценка $starNumber",
                tint = if (starNumber <= currentRating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        val newRating = if (starNumber == currentRating && starNumber > 0) starNumber - 1 else starNumber
                        currentRating = newRating
                        onRatingChange(newRating)
                    }
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}