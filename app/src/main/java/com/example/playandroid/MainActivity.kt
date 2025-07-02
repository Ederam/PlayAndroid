package com.example.playandroid

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var tapButton: Button
    private lateinit var scoreText: TextView
    private var score = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tapButton = findViewById(R.id.btnTapMe)
        scoreText = findViewById(R.id.tvScore)

        tapButton.setOnClickListener {
            score++
            scoreText.text = "Puntos: $score"
            moveButtonRandomly()
        }

        moveButtonRandomly() // mover desde el inicio
    }

    private fun moveButtonRandomly() {
        val layout = tapButton.parent as View
        val maxX = layout.width - tapButton.width
        val maxY = layout.height - tapButton.height

        if (maxX <= 0 || maxY <= 0) {
            // Esperar hasta que el layout esté medido
            handler.postDelayed({ moveButtonRandomly() }, 100)
            return
        }

        val randomX = Random.nextInt(maxX)
        val randomY = Random.nextInt(maxY)

        tapButton.x = randomX.toFloat()
        tapButton.y = randomY.toFloat()
    }
}
