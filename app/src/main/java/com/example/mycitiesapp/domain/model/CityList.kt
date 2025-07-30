package com.example.mycitiesapp.domain.model

import androidx.compose.ui.graphics.Color

data class CityList(
    val id: Int = 0,
    val shortName: String,
    val fullName: String,
    val color: Color,
    val cities: List<City>
)
