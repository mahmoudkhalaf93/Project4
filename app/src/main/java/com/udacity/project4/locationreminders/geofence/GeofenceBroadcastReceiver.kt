package com.udacity.project4.locationreminders.geofence

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

import androidx.core.content.ContextCompat
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.LocationServices
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragment.Companion.ACTION_GEOFENCE_EVENT
import com.udacity.project4.utils.sendNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject


/**
 * Triggered by the Geofence.  Since we can have many Geofences at once, we pull the request
 * ID from the first Geofence, and locate it within the cached data in our Room DB
 *
 * Or users can add the reminders and then close the app, So our app has to run in the background
 * and handle the geofencing in the background.
 * To do that you can use https://developer.android.com/reference/android/support/v4/app/JobIntentService to do that.
 *
 */
private const val TAG = "GeofenceReceiver"
class GeofenceBroadcastReceiver() : BroadcastReceiver() {
    //private val myhelper: MyHelper by lazy { MyHelper() }
    override fun onReceive(context: Context, intent: Intent) {
        GeofenceTransitionsJobIntentService.enqueueWork(context,intent)
       // myhelper.onReceive(context, intent)
//TODO: implement the onReceive method to receive the geofencing events at the background

    }
}
//class MyHelper : KoinComponent {
//    @SuppressLint("MissingPermission")
//    fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == ACTION_GEOFENCE_EVENT) {
//            val geofencingEvent = GeofencingEvent.fromIntent(intent)
//
//            if (geofencingEvent.hasError()) {
//                val errorMessage = errorMessage(context, geofencingEvent.errorCode)
//                Log.e(TAG, errorMessage)
//                return
//            }
//
//            if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
//                || geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
//                Log.v(TAG, context.getString(R.string.geofence_entered))
//
//                val triggeringGeofences = geofencingEvent.triggeringGeofences
//
//                if (triggeringGeofences.isNullOrEmpty())
//                    return
//                CoroutineScope(Dispatchers.IO).launch {
//                    triggeringGeofences.forEach {
//                      //  it.requestI
//                        val reminderRepository: RemindersLocalRepository by inject<RemindersLocalRepository>()
//                        displayNotificationForReminder(context,reminderRepository.getReminder(it.requestId),intent)
//                    }
//                }
////
//            }
//        }
//    }
//    private fun displayNotificationForReminder(context: Context,reminderDataResult: Result<ReminderDTO>,intent: Intent){
//        var reminderDataItem : ReminderDataItem? = null
//
//        if (!reminderDataResult.isSuccessful())
//            return
//
//        reminderDataResult.getCurrentData()?.let {
//            reminderDataItem =ReminderDataItem(it.title,it.description,it.location,it.latitude,it.longitude,it.id)
//        }
//        reminderDataItem?.let { reminderData ->
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                sendNotification(context, reminderData)
//            }
//        }
//    }
//}