package com.example.dietplanner.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

data class WaterIntakeState(
    val amountMl: Int = 0,
    val goalMl: Int = 2000
)

@Singleton
class WaterIntakeManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val WATER_AMOUNT = intPreferencesKey("water_amount_ml")
        private val WATER_GOAL = intPreferencesKey("water_goal_ml")
        private val WATER_DATE = stringPreferencesKey("water_date")
    }

    private fun today(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    val waterStateFlow: Flow<WaterIntakeState> = dataStore.data.map { prefs ->
        val savedDate = prefs[WATER_DATE]
        val goal = prefs[WATER_GOAL] ?: 2000
        val amount = if (savedDate == today()) {
            prefs[WATER_AMOUNT] ?: 0
        } else {
            0
        }

        WaterIntakeState(
            amountMl = amount,
            goalMl = goal
        )
    }

    suspend fun addWater(amountMl: Int) {
        dataStore.edit { prefs ->
            val currentDate = prefs[WATER_DATE]
            val currentAmount = if (currentDate == today()) {
                prefs[WATER_AMOUNT] ?: 0
            } else {
                0
            }

            prefs[WATER_DATE] = today()
            prefs[WATER_AMOUNT] = currentAmount + amountMl
        }
    }

    suspend fun removeWater(amountMl: Int) {
        dataStore.edit { prefs ->
            val currentDate = prefs[WATER_DATE]
            val currentAmount = if (currentDate == today()) {
                prefs[WATER_AMOUNT] ?: 0
            } else {
                0
            }

            prefs[WATER_DATE] = today()
            prefs[WATER_AMOUNT] = (currentAmount - amountMl).coerceAtLeast(0)
        }
    }

    suspend fun resetWater() {
        dataStore.edit { prefs ->
            prefs[WATER_DATE] = today()
            prefs[WATER_AMOUNT] = 0
        }
    }

    suspend fun setGoal(goalMl: Int) {
        dataStore.edit { prefs ->
            prefs[WATER_GOAL] = goalMl.coerceAtLeast(250)
        }
    }
}