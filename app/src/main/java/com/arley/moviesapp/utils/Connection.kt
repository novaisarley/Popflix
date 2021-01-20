package com.arley.moviesapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import com.arley.moviesapp.listener.ConnectionListener

class Connection{
    companion object{
        fun startVerification(context: Context, listener: ConnectionListener){
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            cm?.let {
                it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        listener.onConnectionAvailable()
                    }
                    override fun onLost(network: Network) {
                        listener.onConnectionLost()
                    }
                })
            }
        }

        fun isConnectedToNetwork(context: Context): Boolean{
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

            return isConnected
        }
    }



}