package com.example.mycitiesapp.domain.repository

import com.example.mycitiesapp.domain.model.CityList

interface CityListRepository {
    suspend fun getAllLists(): List<CityList>
    suspend fun insertList(list: CityList): Long

    suspend fun getListById(id: Int): CityList
}