package com.adobe.phonegap.push

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.RemoteInput

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat

/**
 * Background Action Button Handler
 */
@Suppress("HardCodedStringLiteral")
@SuppressLint("LongLogTag", "LogConditional")
class BackgroundActionButtonHandler : BroadcastReceiver() {
  companion object {
    private const val TAG: String = "${PushPlugin.PREFIX_TAG} (BackgroundActionButtonHandler)"
  }

  /**
   * @param context
   * @param intent
   */

  private fun mSomeFunction(notManager: NotificationManager, extras: Bundle?, notId: Int, context: Context) {
	  /*val notManager =
      context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager*/

	  //FCMService().createNotification(extras)
    Log.d(TAG, "Sadas mSomeFunction")
    val mBuilder : NotificationCompat.Builder  = NotificationCompat.Builder(context, "PushPluginChannel")
    mBuilder.setSmallIcon(context.applicationInfo.icon);
    mBuilder.setContentTitle("Notification Alert");
    mBuilder.setContentText("Random notification");
    mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
    notManager.notify(FCMService.getAppName(context), notId, mBuilder.build())
    notManager.cancel(FCMService.getAppName(context), notId)

    Log.d(TAG, "Sadas mSomeFunction End")
    //notManager.cancel(FCMService.getAppName(context), notId)
  }

  override fun onReceive(context: Context, intent: Intent) {
    val notId = intent.getIntExtra(PushConstants.NOT_ID, 0)
    Log.d(TAG, "Not ID: $notId")

    val notificationManager =
      context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(FCMService.getAppName(context), notId)

    val bundle = intent.extras?.getBundle(PushConstants.PUSH_BUNDLE);
/*
    for (String key : bundle.keySet()) {
        Log.d(TAG, "Sadas bundle " + key + ": " bundle?.get(key));
    }*/

    Log.d(TAG, "Sadas bundle: " + bundle.toString())

    if(intent.extras?.getString("OrderId") != null) {
      Log.d(TAG, "Sadas has OrderId: " + intent.extras?.getString("OrderId"))
    }
    
    Handler(Looper.getMainLooper()).postDelayed({
      mSomeFunction(notificationManager, intent.extras, notId, context)
    }, 500)

    var channelID: String = "PushPluginChannel"
/*
    if (intent.extras != null) {
      channelID = intent.extras?.getString(PushConstants.ANDROID_CHANNEL_ID)
    }

      // if the push payload specifies a channel use it
    else if (channelID != null) {
        NotificationCompat.Builder(context, channelID)
      } else {
        val channels = notificationManager.notificationChannels

        channelID = if (channels.size == 1) {
        channels[0].id.toString()
      } else {
        PushConstants.DEFAULT_CHANNEL_ID
      }
    }*/

    /*@SuppressLint ("RestrictedApi")
    fun NotificationCompat.Builder.clearActions () {
        mActions.clear()
    }

      val mBuilder: NotificationCompat.Builder =  NotificationCompat.Builder(context, channelID)

      mBuilder.clearActions()*/


      

    intent.extras?.let { extras ->
      Log.d(TAG, "Intent Extras: $extras")
      extras.getBundle(PushConstants.PUSH_BUNDLE)?.apply {
        putBoolean(PushConstants.FOREGROUND, false)
        putBoolean(PushConstants.COLDSTART, false)
        putString(
          PushConstants.ACTION_CALLBACK,
          extras.getString(PushConstants.CALLBACK)
        )

        RemoteInput.getResultsFromIntent(intent)?.let { remoteInputResults ->
          val results = remoteInputResults.getCharSequence(PushConstants.INLINE_REPLY).toString()
          Log.d(TAG, "Inline Reply: $results")

          putString(PushConstants.INLINE_REPLY, results)
        }
      }

      PushPlugin.sendExtras(extras)
    }
  }
}
