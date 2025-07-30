package com.example.mycitiesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mycitiesapp.data.dao.CityListDao
import com.example.mycitiesapp.data.db.CityEntity
import com.example.mycitiesapp.data.dao.cityDao
import com.example.mycitiesapp.data.db.CityListCityCrossRef
import com.example.mycitiesapp.data.db.CityListEntity

@Database(entities = [CityEntity::class, CityListEntity::class, CityListCityCrossRef::class],
    version = 7)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao() : cityDao

    abstract fun cityListDao(): CityListDao

}