package com.example.mycitiesapp.domain.store

import kotlinx.coroutines.flow.StateFlow

interface SelectedListStore {
    val selectedListId: StateFlow<Int?>
    suspend fun selectList(id: Int)
}