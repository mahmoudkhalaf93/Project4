package com.udacity.project4.locationreminders.savereminder

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.mockito.Mock

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {
    //TODO: provide testing to the SaveReminderView and its live data objects


    private lateinit var saveReminderViewModel: SaveReminderViewModel


    private lateinit var fakeDataSource: FakeDataSource


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


     var  context : Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setupViewModel() {

        fakeDataSource = FakeDataSource()
        val reminder1 = ReminderDTO("Title1", "Description1", "location1", 1.1, 2.2)
        val reminder2 = ReminderDTO("Title2", "Description2", "location2", 3.3, 4.4)
        val reminder3 = ReminderDTO("Title3", "Description3", "location3", 5.5, 6.6)

        fakeDataSource.addTasks(reminder1, reminder2, reminder3)

        saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)

    }
@After
fun clearInit(){
    stopKoin()
}
    @Test
    fun saveReminder_status_success() {
//given
        val reminder4 = ReminderDataItem("Title4", "Description4", "location4", 7.7, 8.8)
//when

        mainCoroutineRule.pauseDispatcher()

        saveReminderViewModel.saveReminder(reminder4)

        //then
/** test lodaing */
        val showLoadingBefore: Boolean =  saveReminderViewModel.showLoading.getOrAwaitValue()
        assertThat(showLoadingBefore, `is`(true))

        mainCoroutineRule.resumeDispatcher()

        val showLoadingAfter: Boolean =  saveReminderViewModel.showLoading.getOrAwaitValue()
        assertThat(showLoadingAfter, `is`(false))
        /** end of test lodaing */

        /** test saved reminder */
        assertThat(fakeDataSource.reminderData[reminder4.id]?.title, `is`(reminder4.title))
        assertThat(fakeDataSource.reminderData[reminder4.id]?.location, `is`(reminder4.location))
        assertThat(fakeDataSource.reminderData[reminder4.id]?.description, `is`(reminder4.description))
        assertThat(fakeDataSource.reminderData[reminder4.id]?.latitude, `is`(reminder4.latitude))
        assertThat(fakeDataSource.reminderData[reminder4.id]?.longitude, `is`(reminder4.longitude))

        /** check toast message */
        val testText: String =  saveReminderViewModel.showToast.getOrAwaitValue()

        assertThat(testText, `is`( context.getString(R.string.reminder_saved)))

        /** check navigation statue */
        val navigationCommand: NavigationCommand =  saveReminderViewModel.navigationCommand.getOrAwaitValue()

        assertThat(navigationCommand, `is`(NavigationCommand.Back))
    }

    @Test
    fun validateEnteredData_status_validate(){
        //given
        val reminder5 = ReminderDataItem("Title5", "Description5", "location5", 9.9, 10.1)
        //when
      val result =  saveReminderViewModel.validateEnteredData(reminder5)
        //then
        assertThat(result, `is`(true))
    }
    @Test
    fun validateEnteredData_status_null(){
        //given
        val reminder5 = ReminderDataItem(null, "Description5", "location5", 9.9, 10.1)
        //when
        val result =  saveReminderViewModel.validateEnteredData(reminder5)
        //then
        assertThat(result, `is`(false))

        //given
        val reminder6 = ReminderDataItem("Title5", "Description5", null, 9.9, 10.1)
        //when
        val result2 =  saveReminderViewModel.validateEnteredData(reminder6)
        //then
        assertThat(result2, `is`(false))
    }
    @Test
    fun validateEnteredData_status_empty(){
        //given
        val reminder5 = ReminderDataItem("", "Description5", "location5", 9.9, 10.1)
        //when
        val result =  saveReminderViewModel.validateEnteredData(reminder5)
        //then
        assertThat(result, `is`(false))

        //given
        val reminder6 = ReminderDataItem("Title5", "Description5", "", 9.9, 10.1)
        //when
        val result2 =  saveReminderViewModel.validateEnteredData(reminder6)
        //then
        assertThat(result2, `is`(false))
    }
}