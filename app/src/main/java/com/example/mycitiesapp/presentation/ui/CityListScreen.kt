package com.example.mycitiesapp.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mycitiesapp.domain.model.City
import com.example.mycitiesapp.presentation.ViewModel.CityListViewModel

@Composable
fun CityListScreen(
    viewModel: CityListViewModel = hiltViewModel()
) {
    val cities by viewModel.cities.collectAsState()
    LaunchedEffect(cities) {
        Log.d("CityListScreen", "Current cities: $cities")
    }
    var draggedItemIndex by remember { mutableStateOf<Int?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Заголовок
        Text(
            text = "Города",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            itemsIndexed(cities, key = { _, city -> city.id }) { index, city ->
                val isDragging = draggedItemIndex == index

                CityRow(
                    city = city,
                    isDragging = isDragging,
                    onDragStart = { draggedItemIndex = index },
                    onMove = { targetIndex ->
                        draggedItemIndex?.let { fromIndex ->
                            if (fromIndex != targetIndex) {
                                viewModel.moveItem(fromIndex, targetIndex)
                                draggedItemIndex = targetIndex
                            }
                        }
                    },
                    onDragEnd = { draggedItemIndex = null }
                )

                if (index < cities.lastIndex) {
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        thickness = 1.dp
                    )
                }
            }
        }
    }

}

@Composable
fun CityRow(
    city: City,
    isDragging: Boolean,
    onDragStart: () -> Unit,
    onMove: (Int) -> Unit,
    onDragEnd: () -> Unit
) {
    val backgroundColor = if (isDragging) Color.LightGray else Color.Transparent

    Surface(
        shadowElevation = if (isDragging) 4.dp else 0.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { onDragStart() },
                    onDrag = { change, offset ->
                        val posY = change.position.y
                        val itemHeight = 72.dp.toPx()
                        val shift = (posY / itemHeight).toInt().coerceIn(0, 4)
                        onMove(shift)
                    },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragEnd() }
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = city.name,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = city.foundingYear,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
