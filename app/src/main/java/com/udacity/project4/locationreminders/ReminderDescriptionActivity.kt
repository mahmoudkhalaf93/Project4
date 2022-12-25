package com.udacity.project4.locationreminders

import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.LocationServices
import com.udacity.project4.R
import com.udacity.project4.authentication.AuthenticationActivity
import com.udacity.project4.databinding.ActivityReminderDescriptionBinding
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


/**
 * Activity that displays the reminder details after the user clicks on the notification
 */
class ReminderDescriptionActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ReminderDataItem = "EXTRA_ReminderDataItem"

        //        receive the reminder object after the user clicks on the notification
        fun newIntent(context: Context, reminderDataItem: ReminderDataItem): Intent {
            val intent = Intent(context, ReminderDescriptionActivity::class.java)
            intent.putExtra(EXTRA_ReminderDataItem, reminderDataItem)
            return intent
        }
    }

    private lateinit var binding: ActivityReminderDescriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_reminder_description
        )


        val reminder = intent.getSerializableExtra(EXTRA_ReminderDataItem) as? ReminderDataItem


//        TODO: Add the implementation of the reminder details
        binding.reminderDataItem = reminder
        binding.lifecycleOwner = this
        binding.doneButton.setOnClickListener {

            val  geofencingClient = LocationServices.getGeofencingClient(this)
            val listGeo  = mutableListOf<String>(reminder!!.id)
            geofencingClient.removeGeofences(listGeo)
            //sorry for handling logic here i am so late
            val repo : RemindersLocalRepository by inject<RemindersLocalRepository>()
            val reminderDTO = ReminderDTO(reminder.title,reminder.description,reminder.location,
            reminder.latitude,reminder.longitude,reminder.id)

          CoroutineScope(Dispatchers.IO).launch {
                  repo.saveReminder(reminderDTO)
          }
            startActivity(Intent(this,AuthenticationActivity::class.java))
        }
    }
}
