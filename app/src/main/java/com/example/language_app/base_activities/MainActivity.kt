package com.example.language_app.base_activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.language_app.databases.Initialization.Companion.supabaseClient
import com.example.language_app.R
import com.example.language_app.adapters.LeaderBoardAdapter
import com.example.language_app.databases.User
import com.example.language_app.databases.UserInfo
import com.example.language_app.databinding.ActivityMainBinding
import com.example.language_app.tasks.audition.AuditionActivity
import com.example.language_app.tasks.game.GameActivity
import com.example.language_app.tasks.guess.GuessActivity
import com.example.language_app.tasks.texting.TextPracticeActivity
import com.example.language_app.profile.UserProfileActivity
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : ActivityBase <ActivityMainBinding>() {

    override val screenBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
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
            screenBinding.rvLeaderBoard.adapter = LeaderBoardAdapter(users)

            screenBinding.ivUserPhoto.setOnClickListener {
                loadLanguagePreference(UserProfileActivity::class.java, "Users_Information", Json.encodeToString(userInfo))
            }

            screenBinding.layoutGuess.setOnClickListener {
                loadLanguagePreference(GuessActivity::class.java, "Users_Information", Json.encodeToString(userInfo))
            }

            screenBinding.layoutTexting.setOnClickListener {
                loadLanguagePreference(TextPracticeActivity::class.java, "Users_Information", Json.encodeToString(userInfo))
            }

            screenBinding.layoutAudition.setOnClickListener {
                loadLanguagePreference(AuditionActivity::class.java, "Users_Information", Json.encodeToString(userInfo))
            }

            screenBinding.layoutGame.setOnClickListener {
                loadLanguagePreference(GameActivity::class.java, "Users_Information", Json.encodeToString(userInfo))
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