package com.example.language_app.top_users

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.language_app.R.layout.users_points
import com.example.language_app.databases.User
import com.example.language_app.top_users.ViewHolder

class BoardOfLeaders(private val itemList: List<User>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = from(parent.context).inflate(users_points, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(itemList[position])

    override fun getItemCount(): Int = itemList.size
}