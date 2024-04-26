package com.example.language_app.base_activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.language_app.databases.Initialization.Companion.supabaseClient
import com.example.language_app.R
import com.example.language_app.databases.User
import com.example.language_app.databases.UserInfo
import com.example.language_app.databinding.ActMainBinding
import com.example.language_app.exercises.ex_audition
import com.example.language_app.exercises.ex_text_words_mm
import com.example.language_app.exercises.ex_animals
import com.example.language_app.exercises.ex_text_words
import com.example.language_app.user_profile.UserProfileActivity
import com.example.language_app.top_users.BoardOfLeaders
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : ActivityBase <ActMainBinding>() {

    override val screenBinding: ActMainBinding by lazy {
        ActMainBinding.inflate(layoutInflater)
    }
    private val usersTableName = "Users_Information"
    private var users: MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val user = supabaseClient.auth.currentUserOrNull()
            val id = user?.id ?: ""
            val userInfo = supabaseClient.postgrest.from(usersTableName).select {
                filter { eq("id", id) }
            }.decodeSingle<UserInfo>()

            screenBinding.tvUserWelcome.text = getString(R.string.main_activity_welcome, userInfo.firstName)

            screenBinding.ivUserPhoto.load(userInfo.userPhotoUrl) {
                fallback(R.drawable.default_user_photo)
                transformations(CircleCropTransformation())
            }

            val topUsers = supabaseClient.postgrest.from(usersTableName).select {
                order("points", Order.DESCENDING)
                range(0, 3)
            }.decodeList<UserInfo>()

            topUsers.forEach { it ->
                users.add(User(it.userPhotoUrl, it.firstName, it.secondName, it.points))
            }

            screenBinding.rvLeaderBoard.layoutManager = LinearLayoutManager(this@MainActivity)
            screenBinding.rvLeaderBoard.adapter = BoardOfLeaders(users)

            screenBinding.ivUserPhoto.setOnClickListener {
                loadLanguagePreference(UserProfileActivity::class.java, "Users_Information", Json.encodeToString(userInfo))
            }

            screenBinding.layoutGuess.setOnClickListener {
                loadLanguagePreference(ex_animals::class.java, "Users_Information", Json.encodeToString(userInfo))
            }

            screenBinding.layoutTexting.setOnClickListener {
                loadLanguagePreference(ex_text_words::class.java, "Users_Information", Json.encodeToString(userInfo))
            }

            screenBinding.layoutAudition.setOnClickListener {
                loadLanguagePreference(ex_audition::class.java, "Users_Information", Json.encodeToString(userInfo))
            }

            screenBinding.layoutGame.setOnClickListener {
                loadLanguagePreference(ex_text_words_mm::class.java, "Users_Information", Json.encodeToString(userInfo))
            }

            setContentView(screenBinding.root)
        }
    }
    private fun loadLanguagePreference(clazz: Class<*>?, name: String, value: String) {
        val intent = Intent(this@MainActivity, clazz)
        intent.putExtra(name, value)
        startActivity(intent)
    }
}