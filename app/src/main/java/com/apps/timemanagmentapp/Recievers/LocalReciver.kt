package com.apps.timemanagmentapp.Recievers
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.apps.timemanagmentapp.R


const val NOTIFICATIONs_ID = 101
const val NOTIFICATIONs_CHANNEL_ID = "10001"
class LocalReciver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification=NotificationCompat.Builder(context, NOTIFICATIONs_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Wow")
            .setContentText("Good work")
            .build()
        Log.d("wow","nice work men")

        val manager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATIONs_ID,notification)
    }

}