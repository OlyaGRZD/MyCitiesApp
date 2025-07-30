package com.example.mycitiesapp.data.repository

import com.example.mycitiesapp.data.dao.CityListDao
import com.example.mycitiesapp.data.dao.cityDao
import com.example.mycitiesapp.data.db.CityListCityCrossRef
import com.example.mycitiesapp.data.mapper.toDomain
import com.example.mycitiesapp.data.mapper.toEntity
import com.example.mycitiesapp.domain.model.CityList
import com.example.mycitiesapp.domain.repository.CityListRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityListRepositoryImpl @Inject constructor(
    private val listDao: CityListDao,
    private val cityDao: cityDao
) : CityListRepository {

    override suspend fun insertList(cityList: CityList): Long {
        val newEntities = cityList.cities
            .filter { it.id == 0 }
            .map { it.toEntity() }

        val newIds = cityDao.insertCities(newEntities)

        val idIterator = newIds.iterator()
        val allIds = cityList.cities.map { city ->
            if (city.id != 0) city.id
            else idIterator.next().toInt()
        }

        val listId = listDao.insert(cityList.toEntity()).toInt()

        val refs = allIds.map { cityId ->
            CityListCityCrossRef(cityListId = listId, cityId = cityId)
        }
        listDao.insertCityListCrossRefs(refs)

        return listId.toLong()
    }

    override suspend fun getAllLists(): List<CityList> {
        return listDao
            .getAllCityListsWithCities()
            .map { it.toDomain() }
    }

    override suspend fun getListById(id: Int): CityList {
        val withCities = listDao.getCityListWithCitiesById(id)
            ?: throw IllegalArgumentException("List $id not found")
        return withCities.toDomain()
    }
}
