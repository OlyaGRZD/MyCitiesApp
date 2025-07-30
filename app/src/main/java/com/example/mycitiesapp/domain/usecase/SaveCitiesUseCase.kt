package com.example.mycitiesapp.domain.usecase

import com.example.mycitiesapp.domain.model.City
import com.example.mycitiesapp.domain.repository.CityRepository
import javax.inject.Inject

class SaveCitiesUseCase @Inject constructor(
    private val repository: CityRepository
) {
     suspend operator fun invoke(cities: List<City>) {
        repository.saveCities(cities)
     }
}