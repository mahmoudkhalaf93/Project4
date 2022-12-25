package com.udacity.project4.locationreminders.data.local


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.notNullValue
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    TODO: Add testing implementation to the RemindersLocalRepository.kt
// Executes each Reminder synchronously using Architecture Components.
@get:Rule
var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var localDataSource: RemindersLocalRepository

    private lateinit var database: RemindersDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource =
            RemindersLocalRepository(
                database.reminderDao(),
                Dispatchers.Main
            )
    }

    @After
    fun cleanUp() {
        database.close()
    }
   // TODO: Replace with runBlockingTest once issue is resolved
    @Test
    fun saveReminder_retrievesReminder() = runBlocking {
        // GIVEN - A new Reminder saved in the database.
       val reminder1 = ReminderDTO("title", "description","location",1.1,2.2)
        localDataSource.saveReminder(reminder1)

        // WHEN  - Reminder retrieved by ID.
        val result = localDataSource.getReminder(reminder1.id)

        // THEN - Same Reminder is returned.
        assertThat(result.isSuccessful(), `is`(true))
        result as Result.Success
       assertThat(result.data.title, `is`(reminder1.title))
       assertThat(result.data.description, `is`(reminder1.description))
       assertThat(result.data.location, `is`(reminder1.location))
       assertThat(result.data.latitude, `is`(reminder1.latitude))
       assertThat(result.data.longitude, `is`(reminder1.longitude))
    }

    //getReminders()
    //deleteAllReminders()
    @Test
    fun getReminders_deleteAllReminders() = runBlocking {
        // GIVEN - A new Reminder saved in the database.
        val reminder1 = ReminderDTO("title", "description","location",1.1,2.2)
        localDataSource.saveReminder(reminder1)
        val reminder2 = ReminderDTO("title2", "description2","location2",4.1,3.2)
        localDataSource.saveReminder(reminder2)
        val reminder3 = ReminderDTO("title3", "description3","location3",5.1,6.2)
        localDataSource.saveReminder(reminder3)

        // WHEN  - Reminders.
        val result = localDataSource.getReminders()

        // THEN - Same Reminders is returned.
        assertThat(result.isSuccessful(), `is`(true))
        assertThat(result.getCurrentData() , notNullValue())
        result as Result.Success
        assertThat(result.data[0].title, `is`(reminder1.title))
        assertThat(result.data[0].description, `is`(reminder1.description))
        assertThat(result.data[0].location, `is`(reminder1.location))
        assertThat(result.data[0].latitude, `is`(reminder1.latitude))
        assertThat(result.data[0].longitude, `is`(reminder1.longitude))

        //when
        localDataSource.deleteAllReminders()

        val result2 = localDataSource.getReminders()
        //then
        assertThat(result2.getCurrentData() , `is`(empty()))

    }
    

}