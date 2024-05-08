package com.example.language_app.exercises

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.language_app.base_activities.ActivityBase
import com.example.language_app.databases.Initialization
import com.example.language_app.R
import com.example.language_app.databases.Animal
import com.example.language_app.databases.UserInfo
import com.example.language_app.databinding.ActAnimalBinding
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class ex_animals : ActivityBase<ActAnimalBinding>() {

    private val gameManager = ex_animal_work()

    private lateinit var currentAnimal: Animal

    override val screenBinding: ActAnimalBinding by lazy {
        ActAnimalBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        lifecycleScope.launch {
            currentAnimal = getNewAnimal()
            setDefaultUI()
        }
        setContentView(screenBinding.root)
    }

    private fun bind() {

        screenBinding.btnCheckAnimal.setOnClickListener {
            if (screenBinding.inputAnimalEditText.text.toString() == "") return@setOnClickListener
            val correctAnswer: Boolean = isCorrectAnswer()
            if (correctAnswer) {
                lifecycleScope.launch {
                    updatePoints()
                    setSuccessUI()
                }
            } else {
                gameManager.resetStreak()
                setWrongUI()
            }
        }

        screenBinding.btnBack.setOnClickListener {
            finish()
        }

        screenBinding.btnAnimalNext.setOnClickListener {
            lifecycleScope.launch {
                currentAnimal = getNewAnimal()
                setDefaultUI()
            }
        }

        screenBinding.btnAnimalAgain.setOnClickListener {
            setDefaultUI()
        }
    }

    private fun setSuccessUI() {
        screenBinding.ivAnimalPhoto.visibility = View.GONE
        screenBinding.tvInputTitle.visibility = View.GONE
        screenBinding.btnCheckAnimal.visibility = View.GONE
        screenBinding.animalTextInputLayout.visibility = View.GONE
        screenBinding.tvResultIcon.text = getString(R.string.animal_good_answer_icon)
        screenBinding.tvAnimalMessage.text = getString(R.string.animal_exercise_good)
        screenBinding.btnAnimalNext.visibility = View.VISIBLE
        screenBinding.tvResultIcon.visibility = View.VISIBLE
        screenBinding.tvAnimalMessage.visibility = View.VISIBLE
        screenBinding.ivTopBlock.setBackgroundColor(getColor(R.color.green))
    }

    private fun setWrongUI() {
        screenBinding.ivAnimalPhoto.visibility = View.GONE
        screenBinding.tvInputTitle.visibility = View.GONE
        screenBinding.btnCheckAnimal.visibility = View.GONE
        screenBinding.animalTextInputLayout.visibility = View.GONE
        screenBinding.tvResultIcon.text = getString(R.string.animal_bad_answer_icon)
        screenBinding.tvAnimalMessage.text = getString(R.string.animal_exercise_bad, currentAnimal.correctAnswer)
        screenBinding.btnAnimalNext.visibility = View.VISIBLE
        screenBinding.tvResultIcon.visibility = View.VISIBLE
        screenBinding.tvAnimalMessage.visibility = View.VISIBLE
        screenBinding.ivTopBlock.setBackgroundColor(getColor(R.color.red))
        screenBinding.btnAnimalAgain.visibility = View.VISIBLE
    }

    private fun setDefaultUI() {
        screenBinding.btnAnimalNext.visibility = View.GONE
        screenBinding.tvResultIcon.visibility = View.GONE
        screenBinding.tvAnimalMessage.visibility = View.GONE
        screenBinding.ivTopBlock.setBackgroundColor(getColor(R.color.deep_blue))
        screenBinding.btnAnimalAgain.visibility = View.GONE
        screenBinding.ivAnimalPhoto.visibility = View.VISIBLE
        screenBinding.tvInputTitle.visibility = View.VISIBLE
        screenBinding.btnCheckAnimal.visibility = View.VISIBLE
        screenBinding.inputAnimalEditText.setText("")
        screenBinding.animalTextInputLayout.visibility = View.VISIBLE
        screenBinding.ivAnimalPhoto.load(currentAnimal.imageUrl) {
            fallback(R.drawable.default_user_photo)
            transformations(RoundedCornersTransformation(20f))
        }
    }

    private fun isCorrectAnswer(): Boolean {
        val userAnswer = screenBinding.inputAnimalEditText.text.toString()
        return userAnswer.equals(currentAnimal.correctAnswer, ignoreCase = true)
    }

    private suspend fun updatePoints() {
        val user = Initialization.supabaseClient.auth.currentUserOrNull()
        val userInfo = Initialization.supabaseClient.postgrest.from("Users_Information").select {
            filter { eq("id", user?.id ?: "") }
        }.decodeSingle<UserInfo>()

        val newPoints: Double = userInfo.points + gameManager.getPoints()

        Initialization.supabaseClient.from("Users_Information").update(
            {
                set("points", newPoints)
            }
        ) {
            filter {
                eq("id", userInfo.id)
            }
        }
    }

    private suspend fun getNewAnimal(): Animal {
        val animal: Animal = try {
            Initialization.supabaseClient.postgrest.from("exercises_animal").select {
                limit(1)
            }.decodeSingle<Animal>()
        } catch (e: Exception) {
            println(e)
            Animal(0, "", "")
        }
        return animal
    }
}