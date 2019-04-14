package com.west.pratice.shop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_funtion.view.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val RC_NICK: Int = 210
    private val RC_SIGNUP: Int = 200
    var signup = false
    val auth = FirebaseAuth.getInstance()
    val funtions = listOf<String>(
        "Camera",
        "Invite friend",
        "Parking",
        "Download coupons",
        "News",
        "A",
        "B",
        "News",
        "News",
        "News",
        "Maps"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        /*if (!signup) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        }*/

        auth.addAuthStateListener {
            authChanged(it)
        }

//        spinner
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
        with(recycler) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
        recycler.adapter = FuntionAdapter()

    }

    inner class FuntionAdapter : RecyclerView.Adapter<FuntionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuntionHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_funtion, parent, false)
            return FuntionHolder(view)
        }

        override fun getItemCount(): Int = funtions.size

        override fun onBindViewHolder(holder: FuntionHolder, position: Int) {
            holder.nameText.text = funtions[position]
            holder.itemView.setOnClickListener {
                functionClicked(holder, position)
            }
        }

    }

    private fun functionClicked(holder: FuntionHolder, position: Int) {
        Log.d(TAG, "functionClicked: $position")
        when (position) {
            1 -> startActivity(Intent(this, ContactActivity::class.java))
        }
    }

    class FuntionHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameText = view.tv_name
    }

    override fun onResume() {
        super.onResume()
//        ed_nickname.text = getNickname()
        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(auth.currentUser!!.uid)
            .child("nickname")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    ed_nickname.text = dataSnapshot.value as String
                }
            })
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
