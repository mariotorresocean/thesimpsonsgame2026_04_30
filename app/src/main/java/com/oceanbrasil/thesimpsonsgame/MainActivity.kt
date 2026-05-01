package com.oceanbrasil.thesimpsonsgame

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.oceanbrasil.thesimpsonsgame.ui.theme.TheSimpsonsGameTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





        enableEdgeToEdge()
        setContent {
            TheSimpsonsGameTheme {
                GameScreen()
            }
        }
    }
}

@Composable
fun GameScreen(vm: GameViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    if (state.loading || state.character == null) {
        CircularProgressIndicator()
        //Text("Carregando...",modifier = Modifier.padding(30.dp))
    } else {
        Column(Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Score: ${state.score}")
            AsyncImage(
                model = state.character?.image,
                contentDescription = null,
                modifier = Modifier.height(300.dp)
            )
            Text(state.character?.name ?: "Não carregado",
                fontSize = 32.sp,
                modifier = Modifier.padding(30.dp))
            Row {
                Button(onClick = {vm.resposta(true)}) {
                    Text("VIVO")
                }
                Button(onClick = {vm.resposta(false)}) {
                    Text("MORTO")
                }
            }

        }
    }
}