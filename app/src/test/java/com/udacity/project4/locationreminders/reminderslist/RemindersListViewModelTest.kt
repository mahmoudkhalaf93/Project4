package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    //TODO: provide testing to the RemindersListViewModel and its live data objects

    private lateinit var reminderListViewModel: RemindersListViewModel


    private lateinit var fakeDataSource: FakeDataSource


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {

        fakeDataSource = FakeDataSource()
        val reminder1 = ReminderDTO("Title1", "Description1", "location1", 1.1, 2.2)
        val reminder2 = ReminderDTO("Title2", "Description2", "location2", 3.3, 4.4)
        val reminder3 = ReminderDTO("Title3", "Description3", "location3", 5.5, 6.6)

        fakeDataSource.addTasks(reminder1, reminder2, reminder3)

        reminderListViewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(),fakeDataSource)

    }

    @After
    fun clearInit() {
        stopKoin()
    }


    @Test
    fun loadReminders_status_true() {
//given

//when
/** testing loading */
        mainCoroutineRule.pauseDispatcher()

        reminderListViewModel.loadReminders()

        //then

        val showLoadingBefore: Boolean = reminderListViewModel.showLoading.getOrAwaitValue()
        assertThat(showLoadingBefore, `is`(true))

        mainCoroutineRule.resumeDispatcher()

        val showLoadingAfter: Boolean = reminderListViewModel.showLoading.getOrAwaitValue()
        assertThat(showLoadingAfter, `is`(false))
        /** end of testing loading */
        /** testing data in live data  */
        mainCoroutineRule.launch {
            //i know that , this logic of assert is not a best practice to test live data object result but it works
            assertThat(
                reminderListViewModel.remindersList.getOrAwaitValue()[0].title, `is`(
                    fakeDataSource.getReminders().getCurrentData()!![0].title
                )
            )
        }
// i thing this way is better
        assertThat(reminderListViewModel.showNoData.getOrAwaitValue() , `is`(false))
    }

    @Test
    fun loadReminders_status_false() {
        //given
        fakeDataSource.setReturnError(true)
        //when
        reminderListViewModel.loadReminders()

        //then
        //toast
       assertThat(reminderListViewModel.showSnackBar.getOrAwaitValue() , `is`(fakeDataSource.errorMessage))

        assertThat(reminderListViewModel.showNoData.getOrAwaitValue() , `is`(true))

    }
}