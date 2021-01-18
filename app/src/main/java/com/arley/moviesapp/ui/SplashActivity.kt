package com.arley.moviesapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.arley.moviesapp.R

class SplashActivity : AppCompatActivity() {

    var SPLASH_TIME = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val mySuperIntent = Intent(this, MainActivity::class.java)
            startActivity(mySuperIntent)
            finish()
        }, SPLASH_TIME.toLong())

    }
}