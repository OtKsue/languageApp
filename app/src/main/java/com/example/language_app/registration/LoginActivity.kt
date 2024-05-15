package com.example.language_app.registration

import android.content.Intent
import android.os.Bundle
import android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.lifecycleScope
import com.example.language_app.base_activities.ActivityBase
import com.example.language_app.databases.Initialization.Companion.storage
import com.example.language_app.databases.Initialization.Companion.supabaseClient
import com.example.language_app.R
import com.example.language_app.databinding.ActLoginBinding
import com.example.language_app.util.isEmailValid
import com.example.language_app.util.isPasswordValid
import com.example.language_app.language_selection.ActivityLanguage
import com.example.language_app.base_activities.MainActivity
import com.example.language_app.util.showInvalidDataDialog
import com.example.language_app.util.showNoSignInDialog
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivity : ActivityBase<ActLoginBinding>() {

    override val screenBinding: ActLoginBinding by lazy {
        ActLoginBinding.inflate(layoutInflater)
    }

    private lateinit var email: String
    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenBinding.btnBack.setOnClickListener {
            startActivity(Intent(this, ActivityLanguage::class.java))
            finish()
        }


        val registrationBeforeSpan = getString(R.string.login_registration_before_span)
        val registrationSpan = getString(R.string.login_registration_span)

        val spannableRegistration = SpannableString(registrationSpan)

        val clickableSpanRegistration = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginActivity, SignActivity::class.java))
                finish()
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = getColor(this@LoginActivity, R.color.blue)
                ds.isUnderlineText = false
            }
        }

        spannableRegistration.setSpan(clickableSpanRegistration,0, spannableRegistration.length, SPAN_EXCLUSIVE_EXCLUSIVE)

        val combinedRegistrationText = SpannableStringBuilder()
            .append(registrationBeforeSpan)
            .append(" ")
            .append(spannableRegistration)
        screenBinding.tvSignup.text = combinedRegistrationText
        screenBinding.tvSignup.movementMethod = LinkMovementMethod.getInstance()


        val googleBeforeSpan = getString(R.string.login_google_before_span)
        val googleSpan = getString(R.string.login_google_span)
        val googleAfterSpan = getString(R.string.login_google_after_span)

        val spannableGoogle = SpannableString(googleSpan)

        spannableGoogle.setSpan(ForegroundColorSpan(getColor(R.color.blue)), 0, spannableGoogle.length, SPAN_EXCLUSIVE_EXCLUSIVE)

        val combinedGoogleText = SpannableStringBuilder()
            .append(googleBeforeSpan)
            .append(" ")
            .append(spannableGoogle)
            .append(" ")
            .append(googleAfterSpan)
        screenBinding.tvGoogle.text = combinedGoogleText
        setContentView(screenBinding.root)

        screenBinding.btnLogin.setOnClickListener {
            email = screenBinding.inputEmailEditText.text.toString()
            password = screenBinding.etInputPassword.text.toString()

            if (!isEmailValid(email) or !isPasswordValid(password)) {
                showInvalidDataDialog(this)
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    supabaseClient.auth.signInWith(Email) {
                        email = screenBinding.inputEmailEditText.text.toString()
                        password = screenBinding.etInputPassword.text.toString()
                    }

                    val session = supabaseClient.auth.currentSessionOrNull()

                    if (session != null) {
                        storage.saveString("SessionAccessToken", session.accessToken)
                        storage.saveString("SessionRefreshToken", session.refreshToken)
                    }

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()

                } catch (e: Exception) {
                    showNoSignInDialog(this@LoginActivity)
                }
            }
        }

    }
}