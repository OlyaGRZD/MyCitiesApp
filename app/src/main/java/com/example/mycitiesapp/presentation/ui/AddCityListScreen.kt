package com.example.mycitiesapp.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.mycitiesapp.domain.model.City
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@Composable
fun AddCityListScreen(
    allCities: List<City>,
    onSubmit: (
        selectedCities: List<City>,
        shortName: String,
        fullName: String,
        color: Color
    ) -> Unit,
    onCancel: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var selectedCities by remember { mutableStateOf<List<City>>(emptyList()) }
    var shortName by remember { mutableStateOf(TextFieldValue("")) }
    var fullName by remember { mutableStateOf(TextFieldValue("")) }
    var selectedColor by remember { mutableStateOf(Color.Green) }

    val colorOptions = listOf(Color.Red, Color.Green, Color.Blue, Color.Magenta, Color.Yellow)

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Новый список городов", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = shortName,
            onValueChange = { shortName = it },
            label = { Text("Короткое имя") },
            singleLine = true
        )

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Полное имя") },
            singleLine = true
        )

        Text("Выберите до 5 городов:")

        var selectedCities by remember { mutableStateOf<List<City>>(emptyList()) }

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .height(200.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            allCities.forEach { city ->

                val isSelected = selectedCities.contains(city)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .toggleable(
                            value = isSelected,
                            enabled = isSelected || selectedCities.size < 5,
                            onValueChange = { checked ->
                                selectedCities = if (checked) {
                                    selectedCities + city
                                } else {
                                    selectedCities - city
                                }
                            }
                        )
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = { checked ->
                            selectedCities = if (checked) {
                                selectedCities + city
                            } else {
                                selectedCities - city
                            }
                        },
                        enabled = isSelected || selectedCities.size < 5
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("${city.name} (${city.foundingYear})")
                }
            }
        }

        Text("Выберите цвет:")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            colorOptions.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(color, CircleShape)
                        .clickable { selectedColor = color }
                        .border(
                            width = if (selectedColor == color) 3.dp else 1.dp,
                            color = if (selectedColor == color) Color.Black else Color.Gray,
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = {
                    onSubmit(
                        selectedCities,
                        shortName.text,
                        fullName.text,
                        selectedColor
                    )
                },
                enabled = shortName.text.isNotBlank() &&
                        fullName.text.isNotBlank() &&
                        selectedCities.isNotEmpty()
            ) {
                Text("Сохранить")
            }

            OutlinedButton(onClick = onCancel) {
                Text("Отмена")
            }
        }
    }
}