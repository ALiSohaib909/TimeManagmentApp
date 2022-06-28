package com.apps.timemanagmentapp.Activities

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.apps.timemanagmentapp.Activities.RegisterActivity.mainurl
import com.apps.timemanagmentapp.Recievers.AlarmReceiver
import com.apps.timemanagmentapp.adapters.TasksItemsAdapter
import com.apps.timemanagmentapp.databinding.ActivityDashboardBinding
import com.apps.timemanagmentapp.objects.TaskObj
import org.json.JSONException


class DashboardActivity : AppCompatActivity() {
    lateinit var binding:ActivityDashboardBinding
    lateinit var adapter:TasksItemsAdapter
    val list=ArrayList<TaskObj>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("savelogin", MODE_PRIVATE)
        extractData(sharedPreferences.getInt("id", 0))
        binding.btnAdd.setOnClickListener{
            startActivity(Intent(this@DashboardActivity,ScheduleTaskActivity::class.java))
        }
        binding.swipe.setOnRefreshListener {
            list.clear()
            binding.chk.visibility= View.INVISIBLE
            extractData(sharedPreferences.getInt("id", 0)) // your code
            binding.swipe.isRefreshing = false
        }
    }

    fun extractData(id: Int) {
        val url: String = mainurl+"/App/getTasks?id="+id
        val requestQueue = Volley.newRequestQueue(applicationContext)
        requestQueue.start()
        val arrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                if(response.length()==0){
                    binding.chk.visibility= View.VISIBLE
                }
                for (i in 0 until response.length()) {
                    try {
                        val obj = response.getJSONObject(i)
                        val data = TaskObj()
                        data.title = obj.getString("title").toString()
                        data.date = obj.getString("date").toString()
                        data.id=obj.getInt("id")
                        list.add(data)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                binding.recTasks.layoutManager = LinearLayoutManager(applicationContext)
                adapter = TasksItemsAdapter(applicationContext, list)
                binding.recTasks.adapter = adapter
                adapter.setOnItemClick(object:TasksItemsAdapter.OnitemClickListener{
                    override fun onEditClick(position: Int) {
                        val bottomSheetDialogFragment = TaskEditBottomSheetFragment(list.get(position).id)
                        bottomSheetDialogFragment.show(
                            supportFragmentManager,
                            bottomSheetDialogFragment.tag
                        )
                    }

                    override fun onDeleteClick(position: Int) {
                        deleteIt(list.get(position).id,position)
                    }



                })



            }) { error ->
            Log.d("happy", error.message!!)
            //    Toast.makeText(shopkeeperrequets.this, "Showing Failed"+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
        requestQueue.add(arrayRequest)
    }


    fun deleteIt(id:Int,position:Int) {
        val url =
            RegisterActivity.mainurl+"App/deleteTask?taskId="+id
        Log.d("urlCreate", url)
        val requestQueue = Volley.newRequestQueue(applicationContext)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                try {
                 list.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    cancelAlarm(id)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        ) { error ->
            Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
        }
        requestQueue.add(request)
    }
    private fun cancelAlarm(id:Int) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        var intents =Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            id,
            intents,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager!!.cancel(pendingIntent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                val builder1 = AlertDialog.Builder(this@DashboardActivity)
                builder1.setMessage("You want to Exit?")
                builder1.setCancelable(true)
                builder1.setPositiveButton(
                    "Yes"
                ) { dialog, id ->
                    this.finish()
                    System.exit(0);
                }
                val alert11 = builder1.create()
                alert11.show()

            return false
        }
        return super.onKeyDown(keyCode, event)
    }
}