package com.example.mycitiesapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.mycitiesapp.data.db.CityListCityCrossRef
import com.example.mycitiesapp.data.db.CityListEntity
import com.example.mycitiesapp.data.db.CityListWithCities

@Dao
interface CityListDao {
    @Query("SELECT * FROM city_lists")
    suspend fun getAll(): List<CityListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: CityListEntity): Long

    @Query("DELETE FROM city_lists")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM city_lists")
    suspend fun getAllCityListsWithCities(): List<CityListWithCities>

    @Transaction
    @Query("SELECT * FROM city_lists WHERE id = :listId")
    suspend fun getCityListWithCitiesById(listId: Int): CityListWithCities?

    @Insert
    suspend fun insertCityListCrossRefs(refs: List<CityListCityCrossRef>)
}