package com.example.music_player

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.music_player.databinding.ActivityPlayeractivityBinding

class playeractivity : AppCompatActivity() ,MediaPlayer.OnCompletionListener {

    private lateinit var runnable: Runnable

    companion object{
        lateinit var musicListPA : ArrayList<Music>
        var SongPosition: Int = 0
        var mediaPlayer: MediaPlayer? = null
        var isPlaying:Boolean = false
    }

    private lateinit var binding: ActivityPlayeractivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlayeractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        creat()
        binding.PlayPausebtn.setOnClickListener {
            if(isPlaying) pauseMusic()
            else playMusic()
        }
        binding.previosbtnPA.setOnClickListener {
            prevNext(increment = false)
        }
        binding.nextbtnPA.setOnClickListener {
            prevNext(increment = true)
        }
        binding.seekBarPA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mediaPlayer!!.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
        seekbarSetup()
        binding.backBtnPA.setOnClickListener {
            val intent = Intent(this@playeractivity,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun creat()
    {
        SongPosition= intent.getIntExtra("index",0)
        when(intent.getStringExtra("class"))
        {
            "musicAdapter"->{
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                setLayout()
                creatMediaPlayer()
            }
            "MainActivity"->{
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                musicListPA.shuffle()
                setLayout()
                creatMediaPlayer()
            }
        }
    }

    private fun setLayout(){
        Glide.with(this)
            .load(musicListPA[SongPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.noise).centerCrop())
            .into(binding.SongImagePa)
        binding.SongNamePa.text = musicListPA[SongPosition].title
    }
    private fun creatMediaPlayer()
    {
        try {
            if(mediaPlayer == null) mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(musicListPA[SongPosition].path)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            isPlaying =true
            binding.PlayPausebtn.setIconResource(R.drawable.pause_icon)
            binding.TvseekBarstart.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            binding.TvseekBarending.text = formatDuration(mediaPlayer!!.duration.toLong())
            binding.seekBarPA.progress=0
            binding.seekBarPA.max = mediaPlayer!!.duration
            mediaPlayer!!.setOnCompletionListener{this.setLayout()}
            mediaPlayer!!.setOnCompletionListener{this.creatMediaPlayer()}
        }catch (e:java.lang.Exception){
            return
        }
    }

    private fun playMusic(){
        binding.PlayPausebtn.setIconResource(R.drawable.pause_icon)
        isPlaying = true
        mediaPlayer!!.start()
    }

    private fun pauseMusic(){
        binding.PlayPausebtn.setIconResource(R.drawable.play_icon)
        isPlaying = false
        mediaPlayer!!.pause()
    }

    private fun prevNext(increment: Boolean){
        if (increment){
            setSongPostion(increment = true)
            setLayout()
            creatMediaPlayer()
        }
        else{
            setSongPostion(increment = false)
            setLayout()
            creatMediaPlayer()
        }
    }

    private fun setSongPostion(increment: Boolean){
        if (increment){
            if (musicListPA.size -1 == SongPosition)
                SongPosition = 0
            else ++SongPosition
        }else{
            if(0 == SongPosition)
                SongPosition = musicListPA.size-1
            else --SongPosition
        }
    }

    fun seekbarSetup(){
        runnable = Runnable {
            binding.TvseekBarstart.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            binding.seekBarPA.progress= mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable,200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        setSongPostion(increment = true)
        creatMediaPlayer()
        setLayout()

    }

}