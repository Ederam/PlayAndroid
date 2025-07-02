package com.example.playandroid

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var btnRed: Button
    private lateinit var btnGreen: Button
    private lateinit var btnBlue: Button
    private lateinit var btnYellow: Button
    private lateinit var btnStart: Button
    private lateinit var tvStatus: TextView

    private val colorButtons = mutableListOf<Button>()
    private val sequence = mutableListOf<Int>()
    private var userIndex = 0
    private var playing = false

    private val handler = Handler()
    private val flashDuration = 400L
    private val delayBetween = 600L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnRed = findViewById(R.id.btnRed)
        btnGreen = findViewById(R.id.btnGreen)
        btnBlue = findViewById(R.id.btnBlue)
        btnYellow = findViewById(R.id.btnYellow)
        btnStart = findViewById(R.id.btnStart)
        tvStatus = findViewById(R.id.tvStatus)

        colorButtons.addAll(listOf(btnRed, btnGreen, btnBlue, btnYellow))

        // Manejo del botón de iniciar
        btnStart.setOnClickListener {
            sequence.clear()
            userIndex = 0
            tvStatus.text = "Observa la secuencia..."
            playing = false
            nextRound()
        }

        // Clicks del jugador
        for ((i, button) in colorButtons.withIndex()) {
            button.setOnClickListener {
                if (!playing) return@setOnClickListener
                flashButton(button)
                checkUserInput(i)
            }
        }
    }

    private fun flashButton(button: Button) {
        val originalColor = button.backgroundTintList
        button.setBackgroundColor(Color.WHITE)
        handler.postDelayed({ button.backgroundTintList = originalColor }, flashDuration)
    }

    private fun playSequence() {
        playing = false
        userIndex = 0
        var delay = 0L

        for (index in sequence) {
            val button = colorButtons[index]
            handler.postDelayed({
                flashButton(button)
            }, delay)
            delay += delayBetween
        }

        handler.postDelayed({
            tvStatus.text = "Tu turno"
            playing = true
        }, delay)
    }

    private fun nextRound() {
        val next = Random.nextInt(0, 4)
        sequence.add(next)
        tvStatus.text = "Nivel: ${sequence.size}"
        playSequence()
    }

    private fun checkUserInput(choice: Int) {
        if (choice == sequence[userIndex]) {
            userIndex++
            if (userIndex == sequence.size) {
                tvStatus.text = "¡Correcto! Próximo nivel..."
                playing = false
                handler.postDelayed({ nextRound() }, 1000)
            }
        } else {
            playing = false
            tvStatus.text = "¡Fallaste! Puntaje: ${sequence.size - 1}"
        }
    }
}
