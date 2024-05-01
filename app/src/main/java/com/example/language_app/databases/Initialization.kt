package com.example.language_app.databases

import android.app.Application
import com.example.language_app.supabase_info.key
import com.example.language_app.supabase_info.url
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

class Initialization : Application(){

    override fun onCreate() {
        super.onCreate()
        storage = Base(this)
        initSupabase()
    }

    companion object {
        lateinit var supabaseClient: SupabaseClient
        lateinit var storage: Base
    }

    suspend fun hasSavedSession(): Boolean {
        var savedSession = supabaseClient.auth.currentSessionOrNull()
        if (savedSession == null) {
            val refreshToken = storage.getString("SessionRefreshToken")
            if (refreshToken != ""){
                try {
                    savedSession =
                        supabaseClient.auth.refreshSession(refreshToken)
                } catch (ignored: RestException) {}
            }
        }
        if (savedSession != null) {
            storage.saveString("SessionAccessToken", savedSession.accessToken)
            storage.saveString("SessionRefreshToken", savedSession.refreshToken)
        }

        return savedSession != null
    }

    private fun initSupabase() {
        supabaseClient = createSupabaseClient(
            supabaseUrl = url,
            supabaseKey = key
        ) {
            defaultSerializer = KotlinXSerializer(Json {
                encodeDefaults = true
                coerceInputValues = true
                ignoreUnknownKeys = true
            })
            install(Auth)
            install(Postgrest)
        }
    }
}