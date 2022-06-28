package com.apps.timemanagmentapp.Recievers

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context

import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.apps.timemanagmentapp.R

import java.util.*
import kotlin.collections.ArrayList


const val NOTIFICATION_CHANNEL_ID = "1000"

class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

      var  title = intent.getStringExtra("title").toString()
       var notification = intent.getStringExtra("notification").toString()
        var id=intent.getIntExtra("id",0)
        callApi(id,title,notification,context)
        Log.d("good",id.toString())
        Log.d("resutl",title.toString())

    }
    private fun callApi(id:Int,title:String,Noti:String,context: Context) {
                val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText(Noti)
                    .build()

                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(id, notification)
            }


}