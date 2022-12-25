package com.udacity.project4.locationreminders.data

import androidx.lifecycle.MutableLiveData
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.runBlocking

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {
    val errorMessage = "Test exception"
    //    TODO: Create a fake data source to act as a double to the real data source
    private var shouldReturnError = false
    var reminderData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()


    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldReturnError) {
            return Result.Error(errorMessage)
        }
        return Result.Success(reminderData.values.toList())
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminderData[reminder.id] = reminder
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError) {
            return Result.Error(errorMessage)
        }
        reminderData[id]?.let {
            return Result.Success(it)
        }
        return Result.Error("Could not find task")
    }

    override suspend fun deleteAllReminders() {
        reminderData.clear()
    }

    fun addTasks(vararg tasks: ReminderDTO) {
        for (task in tasks) {
            reminderData[task.id] = task
        }

    }
}