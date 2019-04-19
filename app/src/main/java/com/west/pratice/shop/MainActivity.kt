package com.west.pratice.shop

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_funtion.view.*
import org.jetbrains.anko.*
import java.net.URL

class MainActivity : AppCompatActivity(),AnkoLogger {
    private val TAG = MainActivity::class.java.simpleName
    private val RC_NICK: Int = 210
    private val RC_SIGNUP: Int = 200
    var signup = false
    var cacheService: Intent? = null

    val auth = FirebaseAuth.getInstance()
    val funtions = listOf<String>(
        "Camera",
        "Invite friend",
        "Parking",
        "Download coupons",
        "News",
        "Movies",
        "Maps",
        "News",
        "News",
        "News"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        /*if (!signup) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        }*/
        auth.addAuthStateListener { auth ->
            authChanged(auth)
        }
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
//        Spinner
        val colors = arrayOf("Red", "Green", "Blue")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("MainActivity", "onItemSelected: ${colors[position]}")
            }

        }
        //        RecyclerView

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        recycler.adapter = FunctionAdapter()
    }

    inner class FunctionAdapter : RecyclerView.Adapter<FuntionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuntionHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_funtion, parent, false)
            val holder = FuntionHolder(view)
            return holder
        }

        override fun getItemCount(): Int {
            return funtions.size
        }

        override fun onBindViewHolder(holder: FuntionHolder, position: Int) {
            holder.nameText.text = funtions.get(position)
            holder.itemView.setOnClickListener { view ->
                functionClicked(holder, position)
            }
        }

    }

    private fun functionClicked(holder: FuntionHolder, position: Int) {
        Log.d(TAG, "functionClicked: $position")
        when (position) {
            1 -> startActivity(Intent(this, ContactActivity::class.java))
            2 -> startActivity(Intent(this, ParkingActivity::class.java))
            4 -> startActivity(Intent(this, NewsActivity::class.java))
            5 -> startActivity(Intent(this, MovieActivity::class.java))
            6 -> startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    class FuntionHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameText: TextView = view.name
    }


    override fun onResume() {
        super.onResume()
//        nickname.text = getNickname()

//        FirebaseDatabase.getInstance()
//            .getReference("users")
//            .child(auth.currentUser!!.uid)
//            .child("nickname")
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    nickname.text = dataSnapshot.value as String
//                }
//
//            })
    }

    private fun authChanged(auth: FirebaseAuth) {
        if (auth.currentUser == null) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        } else {
            Log.d("MainActivity", "authChanged: ${auth.currentUser?.uid}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, NicknameActivity::class.java)
                startActivityForResult(intent, RC_NICK)
            }
        }
        if (requestCode == RC_NICK) {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action.equals(CacheService.ACTION_CACHE_DONE))
//                toast("MainActivity cache informed")
                info("MainActivity cache informed")
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_cache -> {
                doAsync {
                    val json = URL("https://api.myjson.com/bins/1727dw").readText()
                    val moveis = Gson().fromJson<List<Movie>>(
                        json,
                        object : TypeToken<List<Movie>>() {}.type
                    )
                    moveis.forEach {
                        startService(
                            intentFor<CacheService>(
                                "TITLE" to it.Title,
                                "URL" to it.Poster
                            )
                        )
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(CacheService.ACTION_CACHE_DONE)
        registerReceiver(broadcastReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
//        stopService(cacheService)
        unregisterReceiver(broadcastReceiver)
    }
}
