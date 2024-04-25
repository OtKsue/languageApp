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
import com.example.language_app.R.string.signup_before_span
import com.example.language_app.R.string.signup_complete
import com.example.language_app.R.string.signup_continue
import com.example.language_app.R.string.signup_span
import com.example.language_app.databases.UserInfo
import com.example.language_app.databinding.ActivitySignupBinding
import com.example.language_app.databinding.ActivitySignupBinding.inflate
import com.example.language_app.base_activities.isEmailValid
import com.example.language_app.base_activities.isNameValid
import com.example.language_app.base_activities.isPasswordValid
import com.example.language_app.account.login.LoginActivity
import com.example.language_app.base_activities.MainActivity
import com.example.language_app.base_activities.showEmailIsBusy
import com.example.language_app.base_activities.showInvalidDataDialog
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
class SignActivity : ActivityBase<ActivitySignupBinding>() {

    private val usersTableName = "Users_Information"

    private val fragList = listOf(
        SignupFirstFragment(),
        SignupSecondFragment(),
    )

    private var firstName = ""
    private var secondName = ""
    private var email = ""
    private var password = ""
    private var confirm = ""

    private var currentFragment: Int = 0

    override val screenBinding: ActivitySignupBinding by lazy {
        inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btnText = listOf(
            getString(signup_continue),
            getString(signup_complete),
        )

        val adapter = SignupAdapter(this, fragList)

        screenBinding.ivBack.setOnClickListener {
            currentFragment--
            if (currentFragment < 0) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                screenBinding.vpSignup.currentItem = currentFragment
            }
        }

        screenBinding.btnSignup.setOnClickListener {
            lifecycleScope.launch {
                val firstFragment = adapter.getFragmentByPosition(0) as SignupFirstFragment
                val secondFragment = adapter.getFragmentByPosition(1) as SignupSecondFragment
                if (currentFragment == 0) {
                    firstName = firstFragment.getFirstName()
                    secondName = firstFragment.getSecondName()
                    email = firstFragment.getEmail()
                    if (!isEmailValid(email) or !isNameValid(firstName) or !isNameValid(secondName)) {
                        showInvalidDataDialog(this@SignupActivity)
                        return@launch
                    }

                    val emailCounts =
                        supabaseClient.postgrest.from(usersTableName).select {
                            filter { eq("email", email) }
                        }.decodeAs<JsonArray>().size

                    if (emailCounts > 0) {
                        showEmailIsBusy(this@SignupActivity)
                        return@launch
                    }

                } else {
                    password = secondFragment.getPassword()
                    confirm = secondFragment.getConfirm()
                    if (!isPasswordValid(password) or !isPasswordValid(confirm) or (password != confirm)) {
                        showInvalidDataDialog(this@SignupActivity)
                        return@launch
                    }
                }
                currentFragment++
                if (currentFragment > 1) {
                    try {
                        with(supabaseClient.auth) {
                            clearSession()
                            signUpWith(Email) {
                                email = this@SignupActivity.email
                                password = this@SignupActivity.password
                            }
                            val userInfo = UserInfo(retrieveUserForCurrentSession().id, firstName, secondName, email)
                            supabaseClient.postgrest.from(usersTableName).insert(userInfo)
                        }

                        startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                        finish()

                    } catch (e: Exception) {
                        AlertDialog.Builder(this@SignupActivity)
                            .setTitle("Ошибка")
                            .setMessage(e.message)
                            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                            .show()
                    }
                } else {
                    screenBinding.vpSignup.currentItem = currentFragment
                }
            }
        }

        screenBinding.vpSignup.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentFragment = position
                screenBinding.btnSignup.text = btnText[currentFragment]
            }
        })

        with(screenBinding.vpSignup) {
            isUserInputEnabled = false
            this.adapter = adapter
            currentItem = currentFragment
        }

        val clickableSpanLogin = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                finish()
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@SignupActivity, R.color.blue)
                ds.isUnderlineText = false
            }
        }

        val loginBeforeSpan = getString(signup_before_span)
        val loginSpan = getString(signup_span)
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

        with(screenBinding.tvSignupToLogin) {
            text = combinedLoginText
            movementMethod = getInstance()
        }
        setContentView(screenBinding.root)
    }
}