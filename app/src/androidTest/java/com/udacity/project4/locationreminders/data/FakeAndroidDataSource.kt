package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

class FakeAndroidDataSource : ReminderDataSource {
    var reminderData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()

    override suspend fun getReminders(): Result<List<ReminderDTO>> {

        return Result.Success(reminderData.values.toList())
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminderData[reminder.id] = reminder
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {

        reminderData[id]?.let {
            return Result.Success(it)
        }
        return Result.Error("Could not find task")
    }

    override suspend fun deleteAllReminders() {
        reminderData.clear()
    }

}