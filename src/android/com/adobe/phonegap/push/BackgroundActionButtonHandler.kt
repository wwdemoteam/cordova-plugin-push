package com.adobe.phonegap.push

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.RemoteInput

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
  override fun onReceive(context: Context, intent: Intent) {
    val notId = intent.getIntExtra(PushConstants.NOT_ID, 0)
    Log.d(TAG, "Not ID: $notId")

    val notificationManager =
      context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(FCMService.getAppName(context), notId)

    val bundle = intent.extras.getBundle(PushConstants.PUSH_BUNDLE);
/*
    for (String key : bundle.keySet()) {
        Log.d(TAG, "Sadas bundle " + key + ": " bundle?.get(key));
    }*/

    Log.d(TAG, "Sadas bundle: " + bundle.toString())

    if(intent.extras?.getString("orderId") != null) {
      Log.d(TAG, "Sadas has OrderId: " + intent.extras?.getString("orderId"))
    }

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
