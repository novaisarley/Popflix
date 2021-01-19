package com.arley.moviesapp.listener

interface ConnectionListener {
    fun onConnectionLost()
    fun onConnectionAvailable()
}