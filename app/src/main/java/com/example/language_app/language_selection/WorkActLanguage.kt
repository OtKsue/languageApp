package com.example.language_app.language_selection

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.language_app.R
import com.example.language_app.R.layout.btn_language
import com.example.language_app.language_selection.WorkActLanguage.ViewHolder

class WorkActLanguage(
    private val itemList: List<DataLanguageCapture>,
    private val itemClickListener: (Int) -> Unit,
) : Adapter<ViewHolder>() {

    class ViewHolder(itemView: View, clickListener: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.btnLanguage)
        init {
            button.setOnClickListener {
                clickListener(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = from(parent.context).inflate(btn_language, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.button.text = currentItem.name
        holder.button.isSelected = currentItem.isSelectActivity
    }

    override fun getItemCount(): Int = itemList.size
}