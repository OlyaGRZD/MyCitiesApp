package com.example.mycitiesapp.data.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CityListWithCities(
    @Embedded val list: CityListEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CityListCityCrossRef::class,
            parentColumn = "cityListId",
            entityColumn = "cityId"
        )
    )
    val cities: List<CityEntity>
)
