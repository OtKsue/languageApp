package com.example.language_app.network

interface NetworkStateListener {
    fun onNetworkConnected()
    fun onNetworkDisconnected()
}