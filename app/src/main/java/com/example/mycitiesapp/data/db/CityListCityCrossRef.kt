package com.example.mycitiesapp.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    primaryKeys = ["cityListId", "cityId"],
    foreignKeys = [
        ForeignKey(
            entity = CityListEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityListId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = CASCADE
        )
    ]
)
data class CityListCityCrossRef(
    val cityListId: Int,
    val cityId: Int
)
