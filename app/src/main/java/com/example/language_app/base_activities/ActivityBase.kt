package com.example.language_app.base_activities

import android.content.IntentFilter
import android.graphics.Color.TRANSPARENT
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Bundle
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.SystemBarStyle.Companion.dark
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat.CONSUMED
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.updateLayoutParams
import androidx.viewbinding.ViewBinding
import com.example.language_app.databinding.ActNoConnectionBinding
import com.example.language_app.databinding.ActNoConnectionBinding.inflate
import com.example.language_app.network.NetworkState
import com.example.language_app.network.NetworkStateListener

abstract class ActivityBase<T: ViewBinding> : AppCompatActivity(), NetworkStateListener {

    private val networkReceiver = NetworkState()
    private var isInternetAvailable = true
    protected var isShouldStart = true
    private val noConnectionBinding: ActNoConnectionBinding by lazy {
        inflate(layoutInflater)
    }

    protected abstract val screenBinding:T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = dark(TRANSPARENT))
        networkReceiver.addListener(this)
        registerReceiver(networkReceiver, IntentFilter(CONNECTIVITY_ACTION))
        noConnectionBinding.btnCheckConnection.setOnClickListener {
            if (isInternetAvailable) setContentView(screenBinding.root)
        }
    }

    override fun onStart() {
        super.onStart()

        setOnApplyWindowInsetsListener(screenBinding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(systemBars())
            v.updateLayoutParams<MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }
            CONSUMED
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }

    override fun onNetworkConnected() {
        isInternetAvailable = true
    }

    override fun onNetworkDisconnected() {
        isInternetAvailable = false
        setContentView(noConnectionBinding.root)
    }
}