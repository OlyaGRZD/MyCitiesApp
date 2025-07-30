package com.example.mycitiesapp.presentation.ViewModel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycitiesapp.domain.model.City
import com.example.mycitiesapp.domain.model.CityList
import com.example.mycitiesapp.domain.store.SelectedListStore
import com.example.mycitiesapp.domain.usecase.GetCityListsUseCase
import com.example.mycitiesapp.domain.usecase.InsertCityListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListSelectorViewModel @Inject constructor(
    private val getCityLists: GetCityListsUseCase,
    private val insertCityList: InsertCityListUseCase,
    private val store: SelectedListStore
) : ViewModel() {

    private val _cityLists = MutableStateFlow<List<CityList>>(emptyList())
    val cityLists: StateFlow<List<CityList>> = _cityLists.asStateFlow()

    val selectedListId: StateFlow<Int?> = store.selectedListId
    val selectedList: StateFlow<CityList?> = combine(
        _cityLists,
        selectedListId
    ) { lists, id -> lists.find { it.id == id } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        loadLists()
    }

    fun loadLists() {
        viewModelScope.launch {
            val lists = getCityLists()
            if (lists.isEmpty()) {
                val defaultCities = listOf(
                    City(id = 0, name = "Париж", foundingYear = "III век до н. э.", position = 0),
                    City(id = 0, name = "Вена", foundingYear = "1147 год", position = 1),
                    City(id = 0, name = "Берлин", foundingYear = "1237 год", position = 2),
                    City(id = 0, name = "Варшава", foundingYear = "1321 год", position = 3),
                    City(id = 0, name = "Милан", foundingYear = "1899 год", position = 4)
                )
                val defaultList = CityList(
                    shortName = "Европа",
                    fullName = "Пять европейских столиц",
                    color = Color.Blue,
                    cities = defaultCities
                )
                val insertedId = insertCityList(defaultList)
                val refreshed = getCityLists()

                _cityLists.value = refreshed
                store.selectList(insertedId.toInt())

                Log.d("CityListSelector", "Inserted default list with id=$insertedId")
            } else {
                _cityLists.value = lists

                if (store.selectedListId.value == null) {
                    store.selectList(lists.first().id)
                }
            }
            Log.d("CityListSelector", "Loaded ${_cityLists.value.size} lists")
        }
    }

    fun selectListById(id: Int) {
        viewModelScope.launch { store.selectList(id) }
    }

    fun addNewList(newList: CityList) {
        viewModelScope.launch {
            val insertedId = insertCityList(newList)
            loadLists()
            store.selectList(insertedId.toInt())
        }
    }
}
