package com.example.language_app.exercises

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.language_app.R
import com.example.language_app.databinding.ActWordsBinding
import com.example.language_app.base_activities.ActivityBase
import com.example.language_app.databases.Initialization
import com.example.language_app.databases.OneWord
import com.example.language_app.databases.UserInfo
import com.example.language_app.databases.Words
import kotlinx.coroutines.launch
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from

class ex_text_words : ActivityBase<ActWordsBinding>() {

    override val screenBinding: ActWordsBinding by lazy {
        ActWordsBinding.inflate(layoutInflater)
    }

    private var isNext = false

    private var wordList: MutableList<OneWord> = mutableListOf()

    private var correct: String = ""

    private val gameManager = ex_text_words_work()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            getNewExercise()
            bind()
        }
        setContentView(screenBinding.root)
    }

    private fun bind() {
        screenBinding.btnAction.setOnClickListener {
            lifecycleScope.launch {

                if (isNext) {
                    getNewExercise()
                    screenBinding.btnAction.setText(R.string.word_exercise_check)
                    isNext = false
                }
                else {
                    var noSkip = false
                    wordList.forEach { wordItem ->
                        noSkip = noSkip || wordItem.isSelected
                    }
                    if (!noSkip) return@launch

                    wordList.forEach { wordItem ->
                        if (wordItem.word == correct) {
                            if (wordItem.isSelected) updatePoints() else gameManager.resetStreak()
                            wordItem.isCorrect = true
                        }
                        if (wordItem.isSelected && (wordItem.word != correct)) {
                            wordItem.isWrong = true
                        }
                    }
                    isNext = true
                    screenBinding.btnAction.setText(R.string.word_exercise_next)
                    screenBinding.rvWords.adapter?.notifyDataSetChanged()
                }
            }
        }

        screenBinding.btnBack.setOnClickListener {
            finish()
        }

        screenBinding.rvWords.layoutManager = LinearLayoutManager(this)
        screenBinding.rvWords.adapter = WordsHolderWorks(wordList) { position ->
            wordList.forEachIndexed { index, item ->
                item.isSelected = index == position
            }
            screenBinding.rvWords.adapter?.notifyDataSetChanged()
        }
    }

    private suspend fun getNewExercise() {
        wordList.clear()
        val wordExercise = Initialization.supabaseClient.postgrest.from("exercises_words").select {
            limit(1)
        }.decodeSingle<Words>()
        wordList.add(OneWord(wordExercise.variant1, false, false, false))
        wordList.add(OneWord(wordExercise.variant2, false, false, false))
        wordList.add(OneWord(wordExercise.variant3, false, false, false))
        wordList.add(OneWord(wordExercise.variant4, false, false, false))
        correct = wordExercise.correct

        screenBinding.tvEnglishVariant.text = wordExercise.englishVariant
        screenBinding.tvTranscription.text = wordExercise.transcription

        screenBinding.rvWords.adapter?.notifyDataSetChanged()
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
}