package com.west.pratice.shop

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_contact.*
import kotlinx.android.synthetic.main.row_contact.view.*
import org.jetbrains.anko.AnkoLogger

class ContactActivity : AppCompatActivity(), AnkoLogger {

    private var cursor: Cursor? = null
    private val RC_CONTACT = 100
    var contacts: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), RC_CONTACT)
        } else {
            readContact()
        }

        with(recycler_contact) {
            layoutManager = LinearLayoutManager(this@ContactActivity)
            setHasFixedSize(true)
            adapter = ContactAdapter()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode == RC_CONTACT) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED -> readContact()
        }
    }

    private fun readContact() {
        cursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        while (cursor!!.moveToNext()) {
            val contact = cursor!!.getString(cursor!!.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            contacts.add(contact)
        }
    }

    inner class ContactAdapter : RecyclerView.Adapter<ContactHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_contact, parent, false)
            return ContactHolder(view)
        }

        override fun getItemCount(): Int = cursor?.count ?: 0

        override fun onBindViewHolder(holder: ContactHolder, position: Int) {
            holder.onBind(contacts[position], position)
        }

    }

    inner class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
        val numberText = view.tv_number
        val contactText = view.tv_contact
        fun onBind(contact: String, position: Int) {
            numberText.text = position.toString()
            contactText.text = contact
        }
    }

}
