@file:Suppress("DEPRECATION")

package com.example.language_app.util

import android.content.Context
import android.content.res.Configuration
import android.util.Patterns.EMAIL_ADDRESS
import androidx.appcompat.app.AlertDialog
import com.example.language_app.databases.Initialization.Companion.storage
import com.example.language_app.R
import java.util.*
import java.util.Locale.setDefault

private const val NAME_REGEX_PATTERN = "^[a-zA-Zа-яА-Я]{2,}$"
private const val PASSWORD_REGEX_PATTERN = "^[a-zA-Z0-9@\$!%*?&]{6,}$"
fun setLocale(language: String, context: Context) {
    val locale = Locale(language)
    setDefault(locale)
    val resources = context.resources
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)
    saveLanguagePreference(language)
}

fun saveLanguagePreference(language: String) = storage.saveString("language", language)

fun isEmailValid(email: String): Boolean =
    EMAIL_ADDRESS.matcher(email).matches()

fun isNameValid(name: String): Boolean = name.matches(NAME_REGEX_PATTERN.toRegex())

fun isPasswordValid(password: String): Boolean = password.matches(PASSWORD_REGEX_PATTERN.toRegex())

fun showInvalidDataDialog(context: Context) {
    AlertDialog.Builder(context)
        .setTitle(R.string.invalid_data_title)
        .setMessage(R.string.invalid_data_message)
        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        .show()
}

fun showNoSignInDialog(context: Context) {
    AlertDialog.Builder(context)
        .setTitle(R.string.no_sign_in_title)
        .setMessage(R.string.no_sign_in_message)
        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        .show()
}

fun showEmailIsBusy(context: Context) {
    AlertDialog.Builder(context)
        .setTitle(R.string.busy_email_title)
        .setMessage(R.string.busy_email_message)
        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        .show()
}