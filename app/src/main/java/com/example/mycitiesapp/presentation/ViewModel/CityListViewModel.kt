package com.example.mycitiesapp.presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycitiesapp.domain.model.City
import com.example.mycitiesapp.domain.store.SelectedListStore
import com.example.mycitiesapp.domain.usecase.GetCitiesByListIdUseCase
import com.example.mycitiesapp.domain.usecase.SaveCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val getCitiesByListId: GetCitiesByListIdUseCase,
    private val saveCitiesUseCase: SaveCitiesUseCase,
    private val store: SelectedListStore
) : ViewModel() {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities.asStateFlow()

    init {
        viewModelScope.launch {
            store.selectedListId
                .filterNotNull()
                .collectLatest { listId ->
                    val list = getCitiesByListId(listId)
                    _cities.value = list.sortedBy { it.position }
                }
        }
    }

    fun moveItem(fromIndex: Int, toIndex: Int) {
        val updated = _cities.value.toMutableList().apply {
            add(toIndex, removeAt(fromIndex))
        }.mapIndexed { idx, city -> city.copy(position = idx) }
        _cities.value = updated

        viewModelScope.launch {
            saveCitiesUseCase(updated)
        }
    }
}
