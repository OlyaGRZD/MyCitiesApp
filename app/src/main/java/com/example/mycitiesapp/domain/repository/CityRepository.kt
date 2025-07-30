package com.example.mycitiesapp.domain.repository

import com.example.mycitiesapp.domain.model.City

interface CityRepository {
    suspend fun getCities(): List<City>

    suspend fun saveCities(cities: List<City>)
}