package com.example.mycitiesapp.data.repository

import com.example.mycitiesapp.domain.repository.SelectedListStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectedListStoreImpl @Inject constructor() : SelectedListStore {
    private val _selectedListId = MutableStateFlow<Int?>(null)
    override val selectedListId: StateFlow<Int?> = _selectedListId.asStateFlow()

    override suspend fun selectList(id: Int) {
        _selectedListId.emit(id)
    }
}