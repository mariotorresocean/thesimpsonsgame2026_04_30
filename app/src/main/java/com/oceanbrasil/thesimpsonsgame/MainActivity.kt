package com.oceanbrasil.thesimpsonsgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.oceanbrasil.thesimpsonsgame.ui.theme.TheSimpsonsGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheSimpsonsGameTheme {
                var serieSelecionada by remember { mutableStateOf<String?>(null) }

                if (serieSelecionada == null) {
                    HomeScreen(onSeriesSelected = { serie ->
                        serieSelecionada = serie
                    })
                } else {
                    ListaScreen(
                        serie = serieSelecionada!!,
                        onBack = { serieSelecionada = null }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    serie: String,
    onBack: () -> Unit,
    vm: ListaViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()
    val gridState = rememberLazyGridState()
    val personagens by vm.personagens.collectAsState()

    LaunchedEffect(serie) {
        vm.iniciar(serie)
    }

    val precisaCarregarMais by remember {
        derivedStateOf {
            val ultimoVisivel = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = gridState.layoutInfo.totalItemsCount
            total > 0 && ultimoVisivel >= total - 4
        }
    }

    LaunchedEffect(precisaCarregarMais) {
        if (precisaCarregarMais) vm.carregarPersonagens()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (serie == "simpsons") "The Simpsons" else "Rick and Morty") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        if (state.loading && personagens.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize().padding(padding).padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(personagens) { p ->
                    CardPersonagem(p)
                }
            }
        }
    }
}

@Composable
fun CardPersonagem(personagem: PersonagemEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = personagem.image,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
            )
            Column(Modifier.padding(8.dp)) {
                Text(personagem.name, fontSize = 16.sp, maxLines = 1)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(getCorStatus(personagem.status)))
                    Spacer(Modifier.width(4.dp))
                    Text(getStatusBR(personagem.status), fontSize = 10.sp)
                }
            }
        }
    }
}

private fun getStatusBR(status: String): String = when (status) {
    "Alive" -> "Vivo"
    "Dead" -> "Morto"
    else -> "Desconhecido"
}

private fun getCorStatus(status: String): Color = when (status) {
    "Alive" -> Color.Green
    "Dead" -> Color.Red
    else -> Color(0xFF939393)
}

@Composable
fun GameScreen(vm: GameViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    if (state.loading || state.character == null) {
        CircularProgressIndicator()
    } else {
        Column(
            Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Score: ${state.score}")
            AsyncImage(
                model = state.character?.image,
                contentDescription = null,
                modifier = Modifier.height(300.dp)
            )
            Text(
                state.character?.name ?: "Não carregado",
                fontSize = 32.sp,
                modifier = Modifier.padding(30.dp)
            )
            Row {
                Button(onClick = { vm.resposta(true) }) {
                    Text("VIVO")
                }
                Button(onClick = { vm.resposta(false) }) {
                    Text("MORTO")
                }
            }
        }
    }
}
