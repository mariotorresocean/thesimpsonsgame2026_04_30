package com.oceanbrasil.thesimpsonsgame

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi

data class ListaState (
    val loading: Boolean = true,
    val paginaAtual: Int = 0,
    val totalPaginas: Int = 1,
    val erro: String? = null,
    val serie: String = ""
)

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ListaState())
    val uiState: StateFlow<ListaState> = _uiState.asStateFlow()
    private val dao = DatabaseFactory.get(application).personagemDao()

    val personagens: StateFlow<List<PersonagemEntity>> = _uiState
        .flatMapLatest { state ->
            dao.observarPorSerie(state.serie)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun iniciar(serie: String) {
        if (_uiState.value.serie != serie) {
            _uiState.value = ListaState(serie = serie)
            carregarPersonagens()
        }
    }

    fun carregarPersonagens() {
        val atual = _uiState.value
        if (atual.serie.isEmpty()) return
        if (atual.totalPaginas > 0 && atual.paginaAtual >= atual.totalPaginas) return

        viewModelScope.launch {
            try {
                if (atual.serie == "rickandmorty") {
                    val proximaPagina = atual.paginaAtual + 1
                    val response = ApiFactory.rickAndMortyApi.getCharacters(proximaPagina)
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        paginaAtual = proximaPagina,
                        totalPaginas = response.info.pages
                    )
                    dao.inserirTodos(response.results.map { it.toEntity("rickandmorty") })
                } else if (atual.serie == "simpsons") {
                    // SampleAPIs não tem paginação, carrega tudo de uma vez
                    val results = ApiFactory.simpsonsApi.getSimpsonsCharacters()
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        paginaAtual = 1,
                        totalPaginas = 1
                    )
                    dao.inserirTodos(results.map { it.toEntity("simpsons") })
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    erro = "Erro ao carregar: ${e.message}"
                )
            }
        }
    }

    private fun CharacterDto.toEntity(serie: String) = PersonagemEntity(
        remoteId = id, name = name, species = species, status = status, image = image, series = serie
    )

    private fun SimpsonsCharacterDto.toEntity(serie: String) = PersonagemEntity(
        remoteId = id, name = name, species = "Human", status = status ?: "Alive", image = image, series = serie
    )
}
