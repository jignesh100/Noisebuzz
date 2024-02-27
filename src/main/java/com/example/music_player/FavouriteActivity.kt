package com.example.music_player

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.music_player.databinding.ActivityFavouriteBinding

class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding= ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtnFA.setOnClickListener {
            val intent=Intent(this@FavouriteActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }
}