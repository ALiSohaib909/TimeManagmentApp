package com.apps.timemanagmentapp.Activities

import android.app.*
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.apps.timemanagmentapp.Recievers.AlarmReceiver
import com.apps.timemanagmentapp.Recievers.NOTIFICATION_CHANNEL_ID
import com.apps.timemanagmentapp.databinding.FragmentTaskEditBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class TaskEditBottomSheetFragment(id:Int) : BottomSheetDialogFragment() {
    var ids:Int=id
    var hour: Int = 0
    var mint: Int = 0
lateinit var binding:FragmentTaskEditBottomSheetBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
        createNotificationChanel()
        getTask(ids)
        binding.btnSave.setOnClickListener{

            save(ids)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentTaskEditBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }


    private fun onClicks() {
        binding.tvSelectTime.setOnClickListener(View.OnClickListener {
            val timeSetListener =
                TimePickerDialog.OnTimeSetListener { timePicker, selectedhour, selectedtime ->
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
                activity,
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

            val datePickerDialog = activity?.let { it1 ->
                DatePickerDialog(
                    it1,
                    { view, year, monthOfYear, dayOfMonth -> binding.tvSelectDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) },
                    mYear,
                    mMonth,
                    mDay,
                )
            }
            datePickerDialog?.show()
        })
    }


    fun getTask(id:Int) {
            val url =
                RegisterActivity.mainurl+"App/getTask?taskId="+id
            Log.d("urlCreate", url)
            val requestQueue = Volley.newRequestQueue(context)
            val request = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->

                    try {
                    binding.etTitle.setText(response.getString("title"))
                        binding.etNoti.setText(response.getString("notification"))
                        binding.tvSelectDate.setText(response.getString("date"))
                        binding.tvSelectTime.setText(response.getString("time"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            ) { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
            requestQueue.add(request)
        }


    private fun cancelAlarm() {
        val alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        var intents =Intent(activity, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            activity,
            ids,
            intents,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager!!.cancel(pendingIntent)
    }


    fun setAlarm(id: Int) {
        val intents = Intent(activity, AlarmReceiver::class.java)
        intents.putExtra("title", binding.etTitle.text.toString())
        intents.putExtra("notification", binding.etNoti.text.toString())
        intents.putExtra("id", id)

        val pendingIntent = PendingIntent.getBroadcast(
            activity,
            id,
            intents,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val dateTime = getTime()
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            dateTime,
            pendingIntent
        )
        openDialog()
    }

    fun save(id: Int) {
        val requestQueue = Volley.newRequestQueue(context)
        val url: String = RegisterActivity.mainurl+"App/UpdateTask"
        Log.e("url", url)
        val Object = JSONObject()
        try {
            Object.put("title", binding.etTitle.text)
            Object.put("date", binding.tvSelectDate.text)
            Object.put("time", binding.tvSelectTime.text)
            Object.put("notification", binding.etNoti.text)
            Object.put("id",ids)
            Object.put("uid", id)
        } catch (ex: JSONException) {
            ex.printStackTrace()
        }
        Log.d("summary", Object.toString() + "")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, Object,
            { response: JSONObject? ->
                if (response != null) {
                    cancelAlarm()
                    setAlarm(response.getInt("ids"))

                }

            }
        ) { error: VolleyError ->
            Toast.makeText(
                context,
                "failed$error",
                Toast.LENGTH_SHORT
            ).show()
        }
        requestQueue.add(jsonObjectRequest)
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
        val date1: Date
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
            val notificationManager = activity?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(chanell)
        }
    }


    fun openDialog(){
        val builder = AlertDialog.Builder(activity)
        //set title for alert dialog
        builder.setTitle("Successful")
        //set message for alert dialog
        builder.setMessage("Successfully Updated")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("ok"){dialogInterface, which ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    }

