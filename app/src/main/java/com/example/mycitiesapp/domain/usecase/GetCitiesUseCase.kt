package com.example.mycitiesapp.domain.usecase

import com.example.mycitiesapp.domain.model.City
import com.example.mycitiesapp.domain.repository.CityRepository
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: CityRepository
) {
    suspend operator fun invoke(): List<City> {
        return repository.
        getCities()
    }
}