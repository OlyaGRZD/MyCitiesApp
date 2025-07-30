package com.example.mycitiesapp.data.mapper

import androidx.compose.ui.graphics.Color
import com.example.mycitiesapp.data.db.CityEntity
import com.example.mycitiesapp.data.db.CityListWithCities
import com.example.mycitiesapp.domain.model.City
import com.example.mycitiesapp.domain.model.CityList
import androidx.core.graphics.toColorInt

fun CityEntity.toDomain() = City(
    id = id,
    name = name,
    foundingYear = foundingYear,
    position = position
)

fun City.toEntity() = CityEntity(
    id = id,
    name = name,
    foundingYear = foundingYear,
    position = position
)
