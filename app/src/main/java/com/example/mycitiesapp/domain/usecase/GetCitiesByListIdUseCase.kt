package com.example.mycitiesapp.domain.usecase

import com.example.mycitiesapp.domain.model.City
import com.example.mycitiesapp.domain.repository.CityListRepository
import javax.inject.Inject

class GetCitiesByListIdUseCase @Inject constructor(
    private val repository: CityListRepository
) {
    suspend operator fun invoke(listId: Int): List<City> =
        repository.getListById(listId).cities
}