package com.example.ducks

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ducks.api.ApiClient
import com.squareup.picasso.Picasso
import com.bumptech.glide.Glide
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var duckImageView: ImageView
    private lateinit var loadDuckButton: Button
    private lateinit var loadHttpButton: Button
    private lateinit var loadJpgButton: Button
    private lateinit var loadGifButton: Button
    private lateinit var imageIdInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        duckImageView = findViewById(R.id.duckImageView)
        loadDuckButton = findViewById(R.id.loadDuckButton)
        loadHttpButton = findViewById(R.id.loadHttpButton)
        loadJpgButton = findViewById(R.id.loadJpgButton)
        loadGifButton = findViewById(R.id.loadGifButton)
        imageIdInput = findViewById(R.id.imageIdInput)

        loadDuckButton.setOnClickListener { loadRandomDuck() }
        loadHttpButton.setOnClickListener { loadHttpDuck() }
        loadJpgButton.setOnClickListener { loadJpgDuck() }
        loadGifButton.setOnClickListener { loadGifDuck() }

        loadRandomDuck()
    }

    private suspend fun loadImage(url: String, isGif: Boolean = false) {
        withContext(Dispatchers.Main) {
            if (isGif) {
                Glide.with(this@MainActivity)
                    .asGif()
                    .load(url)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.placeholder)
                    .into(duckImageView)
            } else {
                Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.placeholder)
                    .into(duckImageView)
            }
        }
    }

    private fun showInputError() {
        Toast.makeText(this, "Digite um número válido", Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String?) {
        Toast.makeText(this, "Erro: $message", Toast.LENGTH_SHORT).show()
    }


    private fun loadRandomDuck() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.instance.getRandomDuck()
                }

                val imageUrl = response.url
                loadImage(imageUrl)

            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadHttpDuck() {
        val code = imageIdInput.text.toString().trim()
        if (code.isNotBlank()) {
            lifecycleScope.launch {
                try {
                    val url = "https://random-d.uk/api/v2/http/$code"
                    loadImage(url)
                } catch (e: Exception) {
                    showError(e.message)
                }
            }
        } else {
            showInputError()
        }
    }

    private fun loadJpgDuck() {
        val id = imageIdInput.text.toString().trim()
        if (id.isNotBlank()) {
            lifecycleScope.launch {
                try {
                    val url = "https://random-d.uk/api/v2/$id.jpg"
                    loadImage(url)
                } catch (e: Exception) {
                    showError(e.message)
                }
            }
        } else {
            showInputError()
        }
    }

    private fun loadGifDuck() {
        val id = imageIdInput.text.toString().trim()
        if (id.isNotBlank()) {
            lifecycleScope.launch {
                try {
                    val url = "https://random-d.uk/api/v2/$id.gif"
                    loadImage(url, isGif = true)
                } catch (e: Exception) {
                    showError(e.message)
                }
            }
        } else {
            showInputError()
        }
    }

}
