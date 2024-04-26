package com.example.language_app.top_users

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.language_app.databases.User
import com.example.language_app.R
import com.example.language_app.R.string.first_second_names_pair


class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivUserPhoto: ImageView = itemView.findViewById(R.id.ivUserPhoto)
    private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
    private val tvPoints: TextView = itemView.findViewById(R.id.tvPoints)

    fun bind(user: User) {
        val context = ivUserPhoto.context
        ivUserPhoto.load(user.photoUrl) {
            fallback(R.drawable.default_user_photo)
            transformations(CircleCropTransformation())
        }
        tvUserName.text = context.getString(first_second_names_pair, user.firstName, user.secondName)
        tvPoints.text = user.points.toString()
    }
}