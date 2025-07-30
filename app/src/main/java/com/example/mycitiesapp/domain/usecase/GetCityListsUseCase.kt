package com.example.mycitiesapp.domain.usecase

import com.example.mycitiesapp.domain.model.CityList
import com.example.mycitiesapp.domain.repository.CityListRepository
import javax.inject.Inject

class GetCityListsUseCase @Inject constructor(
    private val repository: CityListRepository
) {
    suspend operator fun invoke(): List<CityList> {
        return repository.getAllLists()
    }
}
