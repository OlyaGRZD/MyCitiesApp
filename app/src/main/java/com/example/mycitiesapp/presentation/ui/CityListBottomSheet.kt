package com.example.mycitiesapp.presentation.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycitiesapp.domain.model.CityList
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CityListBottomSheet(
    lists: List<CityList>,
    selectedId: Int?,
    onSelect: (CityList) -> Unit,
    onAddClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        sheetState.show()
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val focusedSize = 72.dp
    val sidePadding = (screenWidth - focusedSize) / 2

    val extendedLists = listOf<CityList?>(null) + lists

    val initialPage = lists.indexOfFirst { it.id == selectedId }
        .takeIf { it >= 0 }?.plus(1) ?: 1

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        initialPageOffsetFraction = 0f,
        pageCount = { extendedLists.size }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }
            .filter { !it }
            .collect {
                val to = if (pagerState.currentPage == 0 && extendedLists.size > 1) 1
                else pagerState.currentPage
                pagerState.animateScrollToPage(to)
            }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .navigationBarsPadding()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Collapse",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(32.dp),
                tint = Color.Gray
            )
            Spacer(Modifier.height(16.dp))

            HorizontalPager(
                state = pagerState,
                pageSpacing = 12.dp,
                contentPadding = PaddingValues(horizontal = sidePadding),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) { page ->
                val item = extendedLists[page]
                val isSelected = pagerState.currentPage == page

                val size by animateDpAsState(if (isSelected) focusedSize else 56.dp)
                val font by animateFloatAsState(if (isSelected) 16f else 13f)

                if (item == null) {
                    IconButton(
                        onClick = onAddClick,
                        modifier = Modifier
                            .size(size)
                            .background(Color.LightGray, CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add list")
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .width(100.dp)
                            .clickable {
                                scope.launch {
                                    if (!isSelected) {
                                        pagerState.animateScrollToPage(page)
                                    } else {
                                        onSelect(item)
                                    }
                                }
                            }
                    ) {
                        Box(
                            Modifier
                                .size(size)
                                .clip(CircleShape)
                                .background(item.color)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = item.shortName,
                            fontSize = font.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            lists.find { it.id == selectedId }?.let { sel ->
                Text(
                    text = sel.fullName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
