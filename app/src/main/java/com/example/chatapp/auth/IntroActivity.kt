package com.example.chatapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityIntroBinding
import com.example.chatapp.databinding.ActivityMainBinding

class IntroActivity : AppCompatActivity() {

    private val binding: ActivityIntroBinding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val intent  = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        binding.joinBtn.setOnClickListener{
            val intent  = Intent(this, JoinActivity::class.java)
            startActivity(intent)

        }
    }
}