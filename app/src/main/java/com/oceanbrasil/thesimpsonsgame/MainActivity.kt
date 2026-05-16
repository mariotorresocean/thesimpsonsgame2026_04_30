package com.oceanbrasil.thesimpsonsgame

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
                //GameScreen()
                ListaScreen()
            }
        }
    }
}

@Composable
fun ListaScreen(vm: ListaViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    val gridState = rememberLazyGridState()

    val precisaCarregarMais by remember {
        derivedStateOf {
            val ultimoVisivel = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = gridState.layoutInfo.totalItemsCount
            total > 0 && ultimoVisivel >= total -4
        }
    }
    LaunchedEffect(precisaCarregarMais) {
        if (precisaCarregarMais) vm.carregarPersonagens()
    }

    if (state.loading) {
        CircularProgressIndicator()
    } else {
        LazyVerticalGrid(columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = gridState,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            items(state.personagens) { p ->
                CardPersonagem(p)
            }
        }
    }
}

@Composable
fun CardPersonagem(personagem: CharacterDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = personagem.image,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
            )
            Column(Modifier.padding(8.dp)) {
                Text(personagem.name, fontSize = 24.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).clip(CircleShape).background(getCorStatus(personagem.status)))
                    Text(personagem.status, fontSize = 12.sp)
                }
            }
        }
    }
}

private fun getCorStatus(status: String): Color = when (status) {
    "Alive" -> Color.Green
    "Dead" -> Color.Red
    else -> Color(0xFF939393)//Color.Yellow
} as Color

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