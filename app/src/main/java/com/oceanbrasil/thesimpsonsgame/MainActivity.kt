package com.oceanbrasil.thesimpsonsgame

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.oceanbrasil.thesimpsonsgame.ui.theme.TheSimpsonsGameTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://thesimpsonsapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val api = retrofit.create(SimpsonsApi::class.java)

            val homer = api.getCharacter(1)
            Log.d("SIMPSONS", homer.name)
            Log.d("SIMPSONS", homer.status)
        }



        enableEdgeToEdge()
        setContent {
            TheSimpsonsGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheSimpsonsGameTheme {
        Greeting("Android")
    }
}