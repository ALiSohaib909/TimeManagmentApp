package com.apps.timemanagmentapp.Activities

import android.app.*
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.apps.timemanagmentapp.Activities.RegisterActivity.mainurl
import com.apps.timemanagmentapp.Recievers.AlarmReceiver
import com.apps.timemanagmentapp.Recievers.NOTIFICATION_CHANNEL_ID
import com.apps.timemanagmentapp.databinding.ActivityScheduleTaskBinding
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class ScheduleTaskActivity : AppCompatActivity() {
    lateinit var binding: ActivityScheduleTaskBinding
    var hour: Int = 0
    var mint: Int = 0
var status:Boolean=false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleTaskBinding.inflate(layoutInflater)
        createNotificationChanel()
        setContentView(binding.root)
        onClicks()
    }

    private fun onClicks() {
        binding.tvSelectTime.setOnClickListener(View.OnClickListener {
            val timeSetListener =
                OnTimeSetListener { timePicker, selectedhour, selectedtime ->
                    binding.tvSelectTime.setText(
                        String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            selectedhour,
                            selectedtime
                        )
                    )
                }
            val timePickerDialog = TimePickerDialog(
                this,
                timeSetListener,
                hour,
                mint,
                false
            )
            timePickerDialog.setTitle("Select time")
            timePickerDialog.show()
        })
        binding.tvSelectDate.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val mYear = c[Calendar.YEAR]
            val mMonth = c[Calendar.MONTH]
            val mDay = c[Calendar.DAY_OF_MONTH]

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth -> binding.tvSelectDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) },
                mYear,
                mMonth,
                mDay,
            )
            datePickerDialog.show()
        })
        binding.btnSave.setOnClickListener {
            val sharedPreferences = getSharedPreferences("savelogin", MODE_PRIVATE)
            save(sharedPreferences.getInt("id", 0))

     openDialog()

 }


    }

    fun save(id: Int) {
        val requestQueue = Volley.newRequestQueue(applicationContext)
        val url: String = mainurl+"App/addTask"
        Log.e("url", url)
        val Object = JSONObject()
        try {
            Object.put("title", binding.etTitle.text)
            Object.put("date", binding.tvSelectDate.text)
            Object.put("time", binding.tvSelectTime.text)
            Object.put("notification", binding.etNoti.text)
            Object.put("uid", id)
        } catch (ex: JSONException) {
            ex.printStackTrace()
        }
        Log.d("summary", Object.toString() + "")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, Object,
            { response: JSONObject? ->
                if (response != null) {

                    setAlarm(response.getInt("ids"))

                }

            }
        ) { error: VolleyError ->
            Toast.makeText(
                applicationContext,
                "failed$error",
                Toast.LENGTH_SHORT
            ).show()
        }
        requestQueue.add(jsonObjectRequest)
    }

    fun setAlarm(id: Int) {
        status=true

        val intents = Intent(this, AlarmReceiver::class.java)
        intents.putExtra("title", binding.etTitle.text.toString())
        intents.putExtra("notification", binding.etNoti.text.toString())
        intents.putExtra("id", id)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            id,
            intents,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val dateTime = getTime()
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            dateTime,
            pendingIntent
        )
    }

    private fun getTime(): Long {
        val date = ArrayList<String>()
        val sValues: List<String> = binding.tvSelectDate.text.split("/").map { it -> it.trim() }
        sValues.forEach { it ->
            date.add(it)
        }
        val time = ArrayList<String>()
        val timelist: List<String> = binding.tvSelectTime.text.split(":").map { it -> it.trim() }
        timelist.forEach { it ->
            time.add(it)
        }
        val formatter1 = SimpleDateFormat("dd/MM/yyyy")
        val date1:Date
        date1=formatter1.parse(binding.tvSelectDate.text.toString())
        val calend = Calendar.getInstance()
        calend.setTimeInMillis(System.currentTimeMillis())
        calend.set(Calendar.DATE,date1.date)
        Log.d("day of month",date.elementAt(0))
        calend.set(Calendar.HOUR_OF_DAY, time.elementAt(0).toInt())

        calend.set(Calendar.MINUTE, time.elementAt(1).toInt())
        calend.set(Calendar.SECOND, 0)

        Log.d("original",date.elementAt(1))
        calend.set(Calendar.YEAR, date.elementAt(2).toInt())
        return calend.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChanel() {
        val name = "AlarmReceiver"
        val desc = "desc"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chanell = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            chanell.description = desc
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(chanell)
        }
    }
    fun openDialog(){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Successful")
        //set message for alert dialog
        builder.setMessage("Successfully Scheduled")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("ok"){dialogInterface, which ->
            val intent = Intent(this@ScheduleTaskActivity, DashboardActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}