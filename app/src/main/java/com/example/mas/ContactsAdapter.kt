package com.example.mas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactsAdapter(private val contactsList: List<String>, private val onItemClick: (String, Boolean) -> Unit) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contactName = contactsList[position]
        holder.bind(contactName) { isChecked ->
            onItemClick(contactName, isChecked)
        }
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewContactName: TextView = itemView.findViewById(R.id.textViewContactName)
        private val checkBoxContact: CheckBox = itemView.findViewById(R.id.checkBoxContact)

        fun bind(contactName: String, onCheckedChangeListener: (Boolean) -> Unit) {
            textViewContactName.text = contactName
            checkBoxContact.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChangeListener(isChecked)
            }
        }
    }
}
