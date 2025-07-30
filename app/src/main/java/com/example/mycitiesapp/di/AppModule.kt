package com.example.mycitiesapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mycitiesapp.data.AppDatabase
import com.example.mycitiesapp.data.dao.CityListDao
import com.example.mycitiesapp.data.dao.cityDao
import com.example.mycitiesapp.data.repository.CityListRepositoryImpl
import com.example.mycitiesapp.data.repository.CityRepositoryImpl
import com.example.mycitiesapp.domain.repository.CityListRepository
import com.example.mycitiesapp.domain.repository.CityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "city_db"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("""
                  INSERT INTO cities (id, name, foundingYear, position) VALUES
                    (1,'Париж','III век до н. э.',0),
                    (2,'Вена','1147 год',1),
                    (3,'Берлин','1237 год',2),
                    (4,'Варшава','1321 год',3),
                    (5,'Милан','1899 год',4),
                    (6,'Москва','1147 год',5),
                    (7,'Хабаровск','1858 год',6),
                    (8,'Ираклион','824 год',7),
                    (9,'Афины','V век до н. э.',8),
                    (10,'Мюнхен','1158 год',9),
                    (11,'Линц','799 год',10),
                    (12,'Зальцбург','696 год',11),
                    (13,'Рим','753 год до н. э.',12),
                    (14,'Барселона','15 год до н. э.',13),
                    (15,'Мадрид','9 год',14),
                    (16,'Лондон','43 год',15),
                    (17,'Амстердам','1275 год',16),
                    (18,'Лиссабон','1147 год',17),
                    (19,'Прага','870 год',18),
                    (20,'Будапешт','1873 год',19)
                """.trimIndent())
                }
            })
            .build()

    @Provides fun provideCityDao(db: AppDatabase): cityDao = db.cityDao()
    @Provides @Singleton fun provideCityRepository(dao: cityDao): CityRepository =
        CityRepositoryImpl(dao)

    @Provides fun provideCityListDao(db: AppDatabase): CityListDao = db.cityListDao()

    @Provides @Singleton
    fun provideCityListRepository(
        listDao: CityListDao,
        cityDao: cityDao
    ): CityListRepository =
        CityListRepositoryImpl(listDao, cityDao)
}
