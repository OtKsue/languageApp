package com.example.language_app.registration

import android.content.Intent
import android.os.Bundle
import android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod.getInstance
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.language_app.base_activities.ActivityBase
import com.example.language_app.databases.Initialization.Companion.supabaseClient
import com.example.language_app.R
import com.example.language_app.R.string.sign_before_span
import com.example.language_app.R.string.sign_complete
import com.example.language_app.R.string.sign_continue
import com.example.language_app.R.string.sign_span
import com.example.language_app.databases.UserInfo
import com.example.language_app.databinding.ActSignupBinding
import com.example.language_app.databinding.ActSignupBinding.inflate
import com.example.language_app.util.isEmailValid
import com.example.language_app.util.isNameValid
import com.example.language_app.util.isPasswordValid
import com.example.language_app.base_activities.MainActivity
import com.example.language_app.util.showEmailIsBusy
import com.example.language_app.util.showInvalidDataDialog
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray

class SignActivity : ActivityBase<ActSignupBinding>() {

    private val usersTableName = "Users_Information"

    private val fragList = listOf(
        FirstSignFragment(),
        SecSignFragment(),
    )

    private var firstName = ""
    private var secondName = ""
    private var email = ""
    private var password = ""
    private var confirm = ""

    private var currentFragment: Int = 0

    override val screenBinding: ActSignupBinding by lazy {
        inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btnText = listOf(
            getString(sign_continue),
            getString(sign_complete),
        )

        val adapter = SignFragmentsWork(this, fragList)

        screenBinding.btnBack.setOnClickListener {
            currentFragment--
            if (currentFragment < 0) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                screenBinding.vpSign.currentItem = currentFragment
            }
        }

        screenBinding.btnSign.setOnClickListener {
            lifecycleScope.launch {
                val firstFragment = adapter.getFragmentByPosition(0) as FirstSignFragment
                val secondFragment = adapter.getFragmentByPosition(1) as SecSignFragment
                if (currentFragment == 0) {
                    firstName = firstFragment.getFirstName()
                    secondName = firstFragment.getSecondName()
                    email = firstFragment.getEmail()
                    if (!isEmailValid(email) or !isNameValid(firstName) or !isNameValid(secondName)) {
                        showInvalidDataDialog(this@SignActivity)
                        return@launch
                    }

                    val emailCounts =
                        supabaseClient.postgrest.from(usersTableName).select {
                            filter { eq("email", email) }
                        }.decodeAs<JsonArray>().size

                    if (emailCounts > 0) {
                        showEmailIsBusy(this@SignActivity)
                        return@launch
                    }

                } else {
                    password = secondFragment.getPassword()
                    confirm = secondFragment.getConfirm()
                    if (!isPasswordValid(password) or !isPasswordValid(confirm) or (password != confirm)) {
                        showInvalidDataDialog(this@SignActivity)
                        return@launch
                    }
                }
                currentFragment++
                if (currentFragment > 1) {
                    try {
                        with(supabaseClient.auth) {
                            clearSession()
                            signUpWith(Email) {
                                email = this@SignActivity.email
                                password = this@SignActivity.password
                            }
                            val userInfo = UserInfo(retrieveUserForCurrentSession().id, firstName, secondName, email)
                            supabaseClient.postgrest.from(usersTableName).insert(userInfo)
                        }

                        startActivity(Intent(this@SignActivity, MainActivity::class.java))
                        finish()

                    } catch (e: Exception) {
                        val errorMessage = if (e.message == "No session found") {
                            "Подтвердите email и авторизируйтесь"
                        } else {
                            e.message
                        }

                        AlertDialog.Builder(this@SignActivity)
                            .setTitle("Something wrong")
                            .setMessage(errorMessage)
                            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                            .show()
                    }
                } else {
                    screenBinding.vpSign.currentItem = currentFragment
                }
            }
        }

        screenBinding.vpSign.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentFragment = position
                screenBinding.btnSign.text = btnText[currentFragment]
            }
        })

        with(screenBinding.vpSign) {
            isUserInputEnabled = false
            this.adapter = adapter
            currentItem = currentFragment
        }

        val clickableSpanLogin = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@SignActivity, LoginActivity::class.java))
                finish()
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@SignActivity, R.color.blue)
                ds.isUnderlineText = false
            }
        }

        val loginBeforeSpan = getString(sign_before_span)
        val loginSpan = getString(sign_span)
        val spannableLogin = SpannableString(loginSpan)
        spannableLogin.setSpan(
            clickableSpanLogin,
            0,
            spannableLogin.length,
            SPAN_EXCLUSIVE_EXCLUSIVE,
        )
        val combinedLoginText = SpannableStringBuilder()
            .append(loginBeforeSpan)
            .append(" ")
            .append(spannableLogin)

        with(screenBinding.tvSignToLogin) {
            text = combinedLoginText
            movementMethod = getInstance()
        }
        setContentView(screenBinding.root)
    }
}