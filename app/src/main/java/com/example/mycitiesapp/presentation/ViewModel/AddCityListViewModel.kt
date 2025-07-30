package com.example.mycitiesapp.presentation.ViewModel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycitiesapp.domain.model.City
import com.example.mycitiesapp.domain.model.CityList
import com.example.mycitiesapp.domain.repository.CityRepository
import com.example.mycitiesapp.domain.usecase.InsertCityListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCityListViewModel @Inject constructor(
    private val cityRepository: CityRepository,
    private val insertCityListUseCase: InsertCityListUseCase
) : ViewModel() {

    private val _allCities = MutableStateFlow<List<City>>(emptyList())
    val allCities: StateFlow<List<City>> = _allCities

    init {
        viewModelScope.launch {
            _allCities.value = cityRepository.getCities()
        }
    }

    fun saveCityList(
        selectedCities: List<City>,
        shortName: String,
        fullName: String,
        color: Color
    ) {
        val cityList = CityList(
            shortName = shortName,
            fullName = fullName,
            color = color,
            cities = selectedCities
        )
        viewModelScope.launch {
            insertCityListUseCase(cityList)
        }
    }
}
