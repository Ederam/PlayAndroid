package com.example.playandroid

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var monster: ImageView
    private lateinit var tvScore: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvGameOver: TextView
    private lateinit var btnRestart: Button
    private lateinit var layout: RelativeLayout

    private var score = 0
    private var playing = false
    private lateinit var hitSound: MediaPlayer
    private lateinit var gameOverSound: MediaPlayer
    private lateinit var countDown: CountDownTimer
    private lateinit var moveTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        monster = findViewById(R.id.imgMonster)
        tvScore = findViewById(R.id.tvScore)
        tvTimer = findViewById(R.id.tvTimer)
        tvGameOver = findViewById(R.id.tvGameOver)
        btnRestart = findViewById(R.id.btnRestart)
        layout = findViewById(R.id.mainLayout)

//        hitSound = MediaPlayer.create(this, R.raw.hit)
//        gameOverSound = MediaPlayer.create(this, R.raw.game_over)

        monster.setOnClickListener {
            if (playing) {
                score++
                tvScore.text = "Puntos: $score"
                hitSound.start()
                moveMonster()
            }
        }

        btnRestart.setOnClickListener {
            startGame()
        }

        startGame()
    }

    private fun startGame() {
        score = 0
        tvScore.text = "Puntos: 0"
        tvTimer.text = "Tiempo: 30"
        tvGameOver.visibility = View.GONE
        btnRestart.visibility = View.GONE
        monster.visibility = View.VISIBLE
        playing = true

        // Movimiento del monstruo cada 1 segundo
        moveTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                moveMonster()
            }
            override fun onFinish() {}
        }
        moveTimer.start()

        // Temporizador principal
        countDown = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                tvTimer.text = "Tiempo: $seconds"
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDown.start()
    }

    private fun endGame() {
        playing = false
        monster.visibility = View.INVISIBLE
        tvGameOver.visibility = View.VISIBLE
        btnRestart.visibility = View.VISIBLE
        tvTimer.text = "Tiempo: 0"
        gameOverSound.start()
        countDown.cancel()
        moveTimer.cancel()
    }

    private fun moveMonster() {
        monster.post {
            val maxX = layout.width - monster.width
            val maxY = layout.height - monster.height
            val randomX = Random.nextInt(maxX)
            val randomY = Random.nextInt(maxY)

            monster.x = randomX.toFloat()
            monster.y = randomY.toFloat()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::hitSound.isInitialized) hitSound.release()
        if (::gameOverSound.isInitialized) gameOverSound.release()
        if (::countDown.isInitialized) countDown.cancel()
        if (::moveTimer.isInitialized) moveTimer.cancel()
    }
}
