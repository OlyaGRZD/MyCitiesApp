package com.example.mycitiesapp.data.repository

import com.example.mycitiesapp.data.dao.cityDao
import com.example.mycitiesapp.data.mapper.toDomain
import com.example.mycitiesapp.data.mapper.toEntity
import com.example.mycitiesapp.domain.model.City
import com.example.mycitiesapp.domain.repository.CityRepository
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val dao: cityDao
) : CityRepository {

    override suspend fun getCities(): List<City> {
        return dao.getAllCities()
            .map { it.toDomain() }
    }

    override suspend fun saveCities(cities: List<City>) {

        val entities = cities.map { it.toEntity() }

        dao.updateCities(entities)
    }
}
