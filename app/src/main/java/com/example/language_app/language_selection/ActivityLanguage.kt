package com.example.language_app.language_selection

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.language_app.base_actitvities.ActivityBase
import com.example.language_app.databinding.ActivityLanguageSelectBinding
import com.example.language_app.databinding.ActivityLanguageSelectBinding.inflate
import com.example.language_app.base_actitvities.setLocale
import com.example.language_app.base_actitvities.MainActivity
import com.example.language_app.account.login.LoginActivity
import com.example.language_app.base_actitvities.setLocale
import com.example.language_app.databinding.ActivityLanguageSelectBinding
import com.example.language_app.databinding.ActivityLanguageSelectBinding.inflate


class ActivityLanguage : ActivityBase<ActivityLanguageSelectBinding>() {

    override val screenBinding: ActivityLanguageSelectBinding by lazy {
        inflate(layoutInflater)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(screenBinding.root)

        val languages = listOf(
            LanguageItem("Russian"),
            LanguageItem("English"),
            LanguageItem("Chinese"),
            LanguageItem("Belarus"),
            LanguageItem("Kazakh"),
        )

        screenBinding.rvLanguageButtons.layoutManager = LinearLayoutManager(this)
        screenBinding.rvLanguageButtons.adapter = LanguageAdapter(languages) {
            languages.forEachIndexed { index, item ->
                item.isSelectActivity = index == it
            }
            screenBinding.rvLanguageButtons.adapter?.notifyDataSetChanged()
        }

        screenBinding.btnChooseLanguage.setOnClickListener {
            var id = -1
            var name = ""
            languages.forEachIndexed { index, item ->
                if (item.isSelectActivity) {
                    name = item.name
                    id = index
                }
            }
            if (id != -1) {
                val selectedLocale = when (name) {
                    "Russian" -> "ru"
                    "English" -> "en"
                    "Chinese" -> "ch"
                    "Belarus" -> "be"
                    "Kazakh" -> "ka"
                    else -> "en"
                }
                setLocale(selectedLocale, this)
                recreate()

                val isProfileChange = intent.getBooleanExtra("ProfileChange", false)
                if (!isProfileChange)  {
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }

                finish()
            }
        }

    }

    data class LanguageItem(
        val name: String,
        var isSelectActivity: Boolean = false
    )
}

class LanguageAdapter(
    private val itemList: List<LanguageItem>,
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
        val view = from(parent.context).inflate(R.layout.item_language_button, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.button.text = currentItem.name
        holder.button.isSelected = currentItem.isSelectActivity
    }

    override fun getItemCount(): Int = itemList.size
}