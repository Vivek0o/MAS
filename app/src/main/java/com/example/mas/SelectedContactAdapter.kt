package com.example.mas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SelectedContactsAdapter(private val selectedContactsList: MutableList<String>)  :
    RecyclerView.Adapter<SelectedContactsAdapter.SelectedContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selected_contact, parent, false)
        return SelectedContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedContactViewHolder, position: Int) {
        val contactName = selectedContactsList[position]
        holder.bind(contactName)
    }

    override fun getItemCount(): Int {
        return selectedContactsList.size
    }

    // Method to add a selected contact
    fun addSelectedContact(contactName: String) {
        selectedContactsList.add(contactName)
        notifyItemInserted(selectedContactsList.size - 1)
    }

    inner class SelectedContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewSelectedContactName: TextView = itemView.findViewById(R.id.textViewSelectedContactName)

        fun bind(contactName: String) {
            textViewSelectedContactName.text = contactName
        }
    }
}
