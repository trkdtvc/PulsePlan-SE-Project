package com.example.dietplanner.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.room.Room
import com.example.dietplanner.dao.FoodDao
import com.example.dietplanner.dao.MealLogDao
import com.example.dietplanner.dao.UserDao
import com.example.dietplanner.database.AppDatabase
import com.example.dietplanner.repository.FoodRepository
import com.example.dietplanner.repository.FoodRepositoryImpl
import com.example.dietplanner.repository.MealLogRepository
import com.example.dietplanner.repository.MealLogRepositoryImpl
import com.example.dietplanner.repository.UserRepository
import com.example.dietplanner.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory



@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "diet_planner2.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideFoodDao(db: AppDatabase): FoodDao = db.foodDao()

    @Provides
    @Singleton
    fun provideMealLogDao(db: AppDatabase): MealLogDao = db.mealLogDao()



    @Provides
    @Singleton
    fun provideUserRepository(dao: UserDao): UserRepository = UserRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideFoodRepository(dao: FoodDao): FoodRepository = FoodRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideMealLogRepository(dao: MealLogDao): MealLogRepository = MealLogRepositoryImpl(dao)



    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("user_prefs") }
        )
    }
}