package com.example.playandroid

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var tvMessage: TextView
    private lateinit var tvRound: TextView
    private lateinit var tvWinner: TextView
    private lateinit var btnRestart: Button

    private val handler = Handler(Looper.getMainLooper())
    private var readyToTap = false
    private var round = 1
    private val totalRounds = 5
    private var scoreA = 0
    private var scoreB = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnA = findViewById(R.id.btnPlayerA)
        btnB = findViewById(R.id.btnPlayerB)
        tvMessage = findViewById(R.id.tvMessage)
        tvRound = findViewById(R.id.tvRound)
        tvWinner = findViewById(R.id.tvWinner)
        btnRestart = findViewById(R.id.btnRestart)

        btnRestart.setOnClickListener {
            round = 1
            scoreA = 0
            scoreB = 0
            tvWinner.visibility = TextView.GONE
            btnRestart.visibility = Button.GONE
            nextRound()
        }

        btnA.setOnClickListener {
            if (readyToTap) {
                scoreA++
                finishRound("¡Jugador A gana la ronda!")
            } else {
                finishRound("¡Jugador A se adelantó!")
            }
        }

        btnB.setOnClickListener {
            if (readyToTap) {
                scoreB++
                finishRound("¡Jugador B gana la ronda!")
            } else {
                finishRound("¡Jugador B se adelantó!")
            }
        }

        nextRound()
    }

    private fun nextRound() {
        if (round > totalRounds) {
            showFinalWinner()
            return
        }

        tvRound.text = "Ronda $round de $totalRounds"
        tvMessage.text = "Prepárense..."
        readyToTap = false

        // Espera entre 2 y 5 segundos antes de decir "¡YA!"
        val delay = Random.nextLong(2000L, 5000L)
        handler.postDelayed({
            tvMessage.text = "¡YA!"
            readyToTap = true
        }, delay)
    }

    private fun finishRound(resultText: String) {
        tvMessage.text = resultText
        readyToTap = false
        round++

        handler.postDelayed({ nextRound() }, 2000)
    }

    private fun showFinalWinner() {
        val winner = when {
            scoreA > scoreB -> "🏆 ¡Jugador A gana el juego!"
            scoreB > scoreA -> "🏆 ¡Jugador B gana el juego!"
            else -> "¡Empate!"
        }

        tvWinner.text = winner
        tvWinner.visibility = TextView.VISIBLE
        btnRestart.visibility = Button.VISIBLE
        tvMessage.text = "Juego terminado"
    }
}
