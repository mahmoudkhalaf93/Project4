package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.google.android.gms.tasks.Task
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

//    TODO: Add testing implementation to the RemindersDao.kt
// Executes each task synchronously using Architecture Components.
@get:Rule
var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()



//    @Query("SELECT * FROM reminders")
//    suspend fun getReminders(): List<ReminderDTO>

//
//
//
//
//    @Query("DELETE FROM reminders")
//    suspend fun deleteAllReminders()
//

    @Test
    fun insertReminderAndGetById() = runBlockingTest {
        // GIVEN - Insert a task.
        val reminder = ReminderDTO("title", "description","location",1.1,2.2)
        database.reminderDao().saveReminder(reminder)

        // WHEN - Get the task by id from the database.
        val loaded = database.reminderDao().getReminderById(reminder.id)

        // THEN - The loaded data contains the expected values.
        assertThat<ReminderDTO>(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.id, `is`(reminder.id))
        assertThat(loaded.title, `is`(reminder.title))
        assertThat(loaded.description, `is`(reminder.description))
        assertThat(loaded.location, `is`(reminder.location))
        assertThat(loaded.latitude, `is`(reminder.latitude))
        assertThat(loaded.longitude, `is`(reminder.longitude))
    }

    @Test
    fun getReminders_AndDeleteReminders() = runBlockingTest {
        // 1. Insert a task into the DAO.
        val reminder1 = ReminderDTO("title", "description","location",1.1,2.2)
        database.reminderDao().saveReminder(reminder1)
        // 2. Update the task by creating a new task with the same ID but different attributes.
        val reminder2= ReminderDTO("title2", "description2","location2",3.1,4.2)
        database.reminderDao().saveReminder(reminder2)

        val loaded = database.reminderDao().getReminders()
        // 3. Check that when you get the task by its ID, it has the updated values.
        assertThat(loaded , notNullValue())
        assertThat(loaded[0].id, `is`(reminder1.id))
        assertThat(loaded[0].title, `is`(reminder1.title))
        assertThat(loaded[0].description, `is`(reminder1.description))
        assertThat(loaded[0].location, `is`(loaded[0].location))
        assertThat(loaded[0].latitude, `is`(loaded[0].latitude))
        assertThat(loaded[0].longitude, `is`(loaded[0].longitude))

        database.reminderDao().deleteAllReminders()
        val loaded2 = database.reminderDao().getReminders()

        assertThat(loaded2 , `is`(empty()))

    }
}