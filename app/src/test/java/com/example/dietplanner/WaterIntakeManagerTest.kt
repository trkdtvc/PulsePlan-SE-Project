package com.example.dietplanner

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.example.dietplanner.session.WaterIntakeManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class WaterIntakeManagerTest {

    private class FakePreferenceDataStore : DataStore<Preferences> {
        private val state = MutableStateFlow<Preferences>(emptyPreferences())

        override val data: Flow<Preferences> = state

        override suspend fun updateData(
            transform: suspend (t: Preferences) -> Preferences
        ): Preferences {
            val updatedPreferences = transform(state.value)
            state.value = updatedPreferences
            return updatedPreferences
        }
    }

    private fun createManager(): WaterIntakeManager {
        return WaterIntakeManager(FakePreferenceDataStore())
    }

    @Test
    fun addRemoveAndResetWater_updatesSavedAmount() = runBlocking {
        val manager = createManager()

        manager.addWater(250)
        manager.addWater(250)
        manager.removeWater(250)

        assertEquals(250, manager.waterStateFlow.first().amountMl)

        manager.resetWater()

        assertEquals(0, manager.waterStateFlow.first().amountMl)
    }

    @Test
    fun removeWater_doesNotGoBelowZero() = runBlocking {
        val manager = createManager()

        manager.removeWater(250)

        assertEquals(0, manager.waterStateFlow.first().amountMl)
    }

    @Test
    fun setGoal_keepsMinimumGoalAt250Ml() = runBlocking {
        val manager = createManager()

        manager.setGoal(100)

        assertEquals(250, manager.waterStateFlow.first().goalMl)
    }
}
