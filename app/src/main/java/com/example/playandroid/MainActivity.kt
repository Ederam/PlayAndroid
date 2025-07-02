package com.example.playandroid


import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import android.media.MediaPlayer


class MainActivity : AppCompatActivity() {

    private lateinit var tapButton: Button
    private lateinit var scoreText: TextView
    private lateinit var timerText: TextView
    private lateinit var gameOverText: TextView
    private lateinit var restartButton: Button
    private lateinit var mainLayout: RelativeLayout

    private var score = 0
    private var gameRunning = true
    private lateinit var gameTimer: CountDownTimer

    private val totalTime = 30_000L // 30 segundos
    private val interval = 1_000L   // Actualiza cada segundo

    private lateinit var highScoreText: TextView
    private var highScore = 0
    private val PREFS_NAME = "TapGamePrefs"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tapSound = MediaPlayer.create(this, R.raw.tap)



        // Enlazar vistas
        tapButton = findViewById(R.id.btnTapMe)
        scoreText = findViewById(R.id.tvScore)
        timerText = findViewById(R.id.tvTimer)
        gameOverText = findViewById(R.id.tvGameOver)
        restartButton = findViewById(R.id.btnRestart)
        mainLayout = findViewById(R.id.mainLayout)
        highScoreText = findViewById(R.id.tvHighScore)

// Cargar mejor puntaje
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        highScore = prefs.getInt("HIGH_SCORE", 0)
        highScoreText.text = "Mejor: $highScore"


        startGame()

        tapButton.setOnClickListener {
            tapSound.start()

            if (gameRunning) {
                score++
                scoreText.text = "Puntos: $score"
                moveButtonRandomly()
            }
        }

        restartButton.setOnClickListener {
            resetGame()
        }
    }

    private fun startGame() {
        score = 0
        gameRunning = true
        scoreText.text = "Puntos: $score"
        timerText.text = "Tiempo: 30"
        tapButton.visibility = View.VISIBLE
        gameOverText.visibility = View.GONE
        restartButton.visibility = View.GONE

        gameTimer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerText.text = "Tiempo: $secondsLeft"
            }

            override fun onFinish() {
                endGame()
            }
        }.start()

        moveButtonRandomly()
    }

    private fun endGame() {
        val tapEndSound = MediaPlayer.create(this, R.raw.temples)
        tapEndSound.start()
        gameRunning = false
        tapButton.visibility = View.GONE
        gameOverText.visibility = View.VISIBLE
        restartButton.visibility = View.VISIBLE

        if (score > highScore) {
            highScore = score
            highScoreText.text = "Mejor: $highScore"

            val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            prefs.edit().putInt("HIGH_SCORE", highScore).apply()
        }
    }


    private fun resetGame() {
        startGame()
    }

    private fun moveButtonRandomly() {
        tapButton.post {
            val maxX = mainLayout.width - tapButton.width
            val maxY = mainLayout.height - tapButton.height

            val randomX = Random.nextInt(maxX)
            val randomY = Random.nextInt(maxY)

            tapButton.x = randomX.toFloat()
            tapButton.y = randomY.toFloat()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::gameTimer.isInitialized) gameTimer.cancel()
    }
}
