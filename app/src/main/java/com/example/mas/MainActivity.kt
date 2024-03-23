package com.example.mas

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewContacts: RecyclerView
    private lateinit var recyclerViewSelectedContacts: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var selectedContactsAdapter: SelectedContactsAdapter
    private val contactsList = mutableListOf<String>()
    private val selectedContactsSet = mutableListOf<String>()

    companion object {
        private const val PERMISSION_REQUEST_READ_CONTACTS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewContacts = findViewById(R.id.recyclerViewContacts)
        recyclerViewSelectedContacts = findViewById(R.id.recyclerViewSelectedContacts)

        contactsAdapter = ContactsAdapter(contactsList) { contactName, isSelected ->
            if (isSelected) {
                selectedContactsSet.add(contactName)
            } else {
                selectedContactsSet.remove(contactName)
            }
        }

        selectedContactsAdapter = SelectedContactsAdapter(selectedContactsSet)

        recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        recyclerViewSelectedContacts.layoutManager = LinearLayoutManager(this)

        recyclerViewContacts.adapter = contactsAdapter
        recyclerViewSelectedContacts.adapter = selectedContactsAdapter

        findViewById<android.view.View>(R.id.buttonFetchContacts).setOnClickListener {
            checkContactsPermission()
            recyclerViewContacts.visibility = View.VISIBLE
            recyclerViewSelectedContacts.visibility = View.GONE
        }

        findViewById<android.view.View>(R.id.buttonClearContacts).setOnClickListener {
            clearContactList()
        }
    }

    private fun checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSION_REQUEST_READ_CONTACTS
            )
        } else {
            fetchContacts()
        }
    }

    @SuppressLint("Range")
    private fun fetchContacts() {
        val contactsCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        contactsList.clear() // Clear only the complete contact list
        contactsCursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                // Add contact to the complete contact list only if it's not already selected
                if (!selectedContactsSet.contains(name)) {
                    contactsList.add(name)
                }
            }
        }
        // Mark previously selected contacts as selected
        for (contactName in selectedContactsSet) {
            if (contactsList.contains(contactName)) {
                selectedContactsAdapter.addSelectedContact(contactName)
            }
        }
        contactsAdapter.notifyDataSetChanged()
    }


    private fun clearContactList() {
        // Show all selected contacts again
        recyclerViewContacts.visibility = View.GONE
        recyclerViewSelectedContacts.visibility = View.VISIBLE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchContacts()
            } else {
                Toast.makeText(
                    this,
                    "Permission denied to read contacts",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
