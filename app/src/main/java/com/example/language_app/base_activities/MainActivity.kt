package com.example.language_app.base_activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.language_app.databases.Initialization
import com.example.language_app.R
import com.example.language_app.databases.User
import com.example.language_app.databases.UserInfo
import com.example.language_app.databinding.ActMainBinding
import com.example.language_app.exercises.ExAnimals
import com.example.language_app.exercises.ExAudition
import com.example.language_app.exercises.ExTextWordsMM
import com.example.language_app.exercises.ExTextWords
import com.example.language_app.top_users.BoardOfLeaders
import com.example.language_app.user_profile.UserProfileActivity
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

class MainActivity : ActivityBase<ActMainBinding>() {

    override val screenBinding: ActMainBinding by lazy {
        ActMainBinding.inflate(layoutInflater)
    }

    private var userItems: MutableList<User> = mutableListOf()

    private lateinit var userInfo: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(screenBinding.root)

        screenBinding.exAnimal.setOnClickListener {
            val intent = Intent(this@MainActivity, ExAnimals::class.java)
            startActivity(intent)
        }

        screenBinding.exWord.setOnClickListener {
            val intent = Intent(this@MainActivity, ExTextWords::class.java)
            startActivity(intent)
        }

        screenBinding.exMultiplayer.setOnClickListener {
            val intent = Intent(this@MainActivity, ExTextWordsMM::class.java)
            startActivity(intent)
        }

        screenBinding.exAudition.setOnClickListener {
            val intent = Intent(this@MainActivity, ExAudition::class.java)
            startActivity(intent)
        }

        screenBinding.ivUserPhoto.setOnClickListener {
            val intent = Intent(this@MainActivity, UserProfileActivity::class.java)
            startActivity(intent)
        }

        screenBinding.rvTopUsers.layoutManager = LinearLayoutManager(this@MainActivity)
        screenBinding.rvTopUsers.adapter = BoardOfLeaders(userItems)
    }

    override fun onStart() {
        super.onStart()
        if (!isShouldStart) return
        lifecycleScope.launch {
            try {
                Initialization.supabaseClient.auth.awaitInitialization()
                val user = Initialization.supabaseClient.auth.currentUserOrNull()
                val id = user?.id ?: ""
                userInfo = Initialization.supabaseClient.postgrest.from("Users_Information").select {
                    filter { eq("id", id) }
                }.decodeSingle<UserInfo>()

                screenBinding.tvUserWelcome.text = getString(R.string.main_activity_welcome, userInfo.firstName)

                screenBinding.ivUserPhoto.load(userInfo.userPhotoUrl) {
                    fallback(R.drawable.default_user_photo)
                    transformations(CircleCropTransformation())
                }
            } catch (e: Exception) {
                print(e.message)
            }


            val topUsers = Initialization.supabaseClient.postgrest.from("Users_Information").select {
                order("points", Order.DESCENDING)
                range(0, 2)
            }.decodeList<UserInfo>()

            userItems.clear()

            topUsers.forEach { it ->
                userItems.add(User(it.userPhotoUrl, it.firstName, it.secondName, it.points))
            }

            screenBinding.rvTopUsers.adapter?.notifyDataSetChanged()
        }
    }

}