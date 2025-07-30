package com.example.mycitiesapp.data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.mycitiesapp.data.db.CityListEntity
import com.example.mycitiesapp.domain.model.CityList
import androidx.core.graphics.toColorInt
import com.example.mycitiesapp.data.db.CityListWithCities
import com.example.mycitiesapp.data.mapper.toDomain as cityToDomain

fun CityListWithCities.toDomain(): CityList {
    return CityList(
        id = list.id,
        shortName = list.shortName,
        fullName = list.fullName,
        color = Color(list.colorHex.toColorInt()),
        cities = cities.map { it.cityToDomain() }
    )
}

fun CityListEntity.toDomain(): CityList = CityList(
    id = id,
    shortName = shortName,
    fullName = fullName,
    color = Color(colorHex.toColorInt()),
    cities = emptyList() // DYBVFYBT
)

fun CityList.toEntity(): CityListEntity = CityListEntity(
    id = id,
    shortName = shortName,
    fullName = fullName,
    colorHex = "#${Integer.toHexString(color.toArgb()).substring(2)}"
)