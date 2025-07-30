package com.example.mycitiesapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mycitiesapp.data.db.CityEntity

// data/dao/CityDao.kt

@Dao
interface cityDao {

    @Query("SELECT * FROM cities ORDER BY position ASC")
    suspend fun getAllCities(): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>): List<Long>

    @Update
    suspend fun updateCity(city: CityEntity)

    @Delete
    suspend fun deleteCity(city: CityEntity)

    @Query("DELETE FROM cities")
    suspend fun deleteAll()

    @Update
    suspend fun updateCities(cities: List<CityEntity>)
}
