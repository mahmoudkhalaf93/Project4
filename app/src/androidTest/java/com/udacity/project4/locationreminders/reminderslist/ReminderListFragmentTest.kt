package com.udacity.project4.locationreminders.reminderslist

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.di.testingModule
import com.udacity.project4.locationreminders.data.FakeAndroidDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest : KoinTest {

//    TODO: test the navigation of the fragments.
//    TODO: test the displayed data on the UI.
//    TODO: add testing for the error messages.

  val reminderDataSource : ReminderDataSource  by inject()



@Before
fun initDataSource(){

    stopKoin()
    startKoin {
        androidLogger()
        androidContext(ApplicationProvider.getApplicationContext())
        modules(testingModule(ApplicationProvider.getApplicationContext()))
    }
    runBlocking {
    reminderDataSource.deleteAllReminders() }
}

    @Test
    fun addReminder_verifyThatNavigation_work() = runTest {
        // GIVEN - Add Reminder

        val reminder =  ReminderDTO("title", "description","location",1.1,2.2)
        reminderDataSource.saveReminder(reminder)

        // WHEN - ReminderList Fragment launched to display reminders

        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(it.view!!, navController)
        }


        onView(withId(R.id.addReminderFAB)).perform(click())

        // THEN - Verify that we navigate to the add screen
        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
        )

    }


    @Test
    fun saveItemAndDetectWhetherItsDisplayedOrNot() = runTest{
        //Given

        val reminder =  ReminderDTO("title", "description","location",1.1,2.2)
        reminderDataSource.saveReminder(reminder)
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)

        scenario.onFragment {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the first list item
        onView(withId(R.id.reminderssRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    //Then Verify whether we have a view with the added item
                    hasDescendant(withText(reminder.title)), click()
                ))
    }


}