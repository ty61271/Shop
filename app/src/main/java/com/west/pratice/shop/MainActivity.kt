package com.west.pratice.shop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_function.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainActivity : AppCompatActivity(), AnkoLogger {

    private val RC_NICKNAME: Int = 110
    private val RC_SIGNUP: Int = 100
    val myFirebase = MyFirebase()
    val mAuth = myFirebase.auth
    val functiuons = arrayListOf<String>(
        "Invite friend",
        "Parking",
        "Movie",
        "Bus"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        mAuth.addAuthStateListener {
            authStateChanged(it)
        }

        val colors = arrayOf("Red", "Green", "Blue")
        val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                info(colors[position])
            }
        }

        with(recycler_home) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = FunctionAdapter()
        }

    }

    inner class FunctionAdapter : RecyclerView.Adapter<FunctionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_function, parent, false)
            return FunctionHolder(view)
        }

        override fun getItemCount(): Int = functiuons.size

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.onBind(functiuons[position])
            holder.itemView.setOnClickListener {
                onItemClicked(position)
            }
        }

    }

    private fun onItemClicked(position: Int) {
        when (position) {
            0 -> startActivity(Intent(this, ContactActivity::class.java))
            1 -> startActivity(Intent(this, ParkingActivity::class.java))
            2 -> startActivity(Intent(this, MovieActivity::class.java))
            3 -> startActivity(Intent(this, BusActivity::class.java))
        }
    }


    inner class FunctionHolder(view: View) : RecyclerView.ViewHolder(view) {

        val functionName = view.tv_function

        fun onBind(function: String) {
            functionName.text = function
        }
    }

    override fun onResume() {
        super.onResume()
        myFirebase.database
            .child(mAuth.currentUser!!.uid)
            .child(MyFirebaseUtil.CHILD_NICKNAME)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    tv_nickname.text = dataSnapshot.value as String
                }
            })
    }

    private fun authStateChanged(auth: FirebaseAuth) {
        if (auth.currentUser == null) {
            setActivityIntentForResult(this, SignUpActivity::class.java, RC_SIGNUP)
        } else {
            info("${auth.currentUser?.uid}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGNUP ->
                if (resultCode == Activity.RESULT_OK)
                    startActivityForResult(Intent(this, NicknameActivity::class.java), RC_NICKNAME)
            RC_NICKNAME -> {
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
/*
{
      "datas" : [ {
        "BusID" : "505-U7",
        "ProviderID" : "1",
        "DutyStatus" : "90",
        "BusStatus" : "90",
        "RouteID" : "",
        "GoBack" : "",
        "Longitude" : "",
        "Latitude" : "",
        "Speed" : "0",
        "Azimuth" : "0",
        "DataTime" : "",
        "ledstate" : "0",
        "sections" : "1"
      }
   ]
}
*/
