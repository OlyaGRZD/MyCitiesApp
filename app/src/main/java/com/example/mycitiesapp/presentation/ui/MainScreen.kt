package com.example.mycitiesapp.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mycitiesapp.domain.model.CityList
import com.example.mycitiesapp.presentation.ViewModel.AddCityListViewModel
import com.example.mycitiesapp.presentation.ViewModel.CityListSelectorViewModel
import com.example.mycitiesapp.presentation.ViewModel.CityListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val selectorVM: CityListSelectorViewModel = hiltViewModel()
    val cityListVM: CityListViewModel = hiltViewModel()
    val addVM: AddCityListViewModel = hiltViewModel()

    val cityLists by selectorVM.cityLists.collectAsState()
    val selectedListId by selectorVM.selectedListId.collectAsState()
    val selectedList by selectorVM.selectedList.collectAsState()

    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "Города") },
                    label = { Text("Города") },
                    selected = navController.currentDestination?.route == "cities" && !showSheet,
                    onClick = {
                        showSheet = false
                        navController.navigate("cities") {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                NavigationBarItem(
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(selectedList?.color ?: MaterialTheme.colorScheme.primary)
                        )
                    },
                    label = { Text(selectedList?.shortName ?: "Списки") },
                    selected = showSheet,
                    onClick = { showSheet = true }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Navigation graph
            NavHost(
                navController = navController,
                startDestination = "cities",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("cities") {
                    CityListScreen(viewModel = cityListVM)
                }
                composable("add_list") {
                    val allCities by addVM.allCities.collectAsState()
                    AddCityListScreen(
                        allCities = allCities,
                        onSubmit = { selectedCities, shortName, fullName, color ->
                            selectorVM.addNewList(
                                CityList(
                                    id = 0,
                                    shortName = shortName,
                                    fullName = fullName,
                                    color = color,
                                    cities = selectedCities
                                )
                            )
                            navController.popBackStack()
                        },
                        onCancel = { navController.popBackStack() }
                    )
                }
            }

            if (showSheet && navController.currentDestination?.route == "cities") {
                CityListBottomSheet(
                    lists = cityLists,
                    selectedId = selectedListId,
                    onSelect = { list ->
                        selectorVM.selectListById(list.id)
                        showSheet = false
                    },
                    onAddClick = {
                        showSheet = false
                        navController.navigate("add_list")
                    },
                    onDismiss = {
                        showSheet = false
                    }
                )
            }
        }
    }
}
