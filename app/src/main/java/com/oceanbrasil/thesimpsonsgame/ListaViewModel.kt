package com.oceanbrasil.thesimpsonsgame

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ListaState (
    val loading: Boolean = true,
    val personagens: List<CharacterDto> = emptyList(),
    val paginaAtual: Int = 0,
    val totalPaginas: Int = 0,
    val erro: String? = null
)

class ListaViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ListaState())
    val uiState: StateFlow<ListaState> = _uiState.asStateFlow()
    private val dao = DatabaseFactory.get(application).personagemDao()

    init {
        carregarPersonagens()
    }

    fun carregarPersonagens() {
        val atual = _uiState.value
        if (atual.loading && atual.personagens.isNotEmpty()) return
        if (atual.totalPaginas > 0 && atual.paginaAtual >= atual.totalPaginas) return

        //if (atual.personagens.isNotEmpty())

        viewModelScope.launch {
            try {
                val proximaPagina = atual.paginaAtual + 1
                val response = ApiFactory.api.getCharacters(proximaPagina)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    personagens = _uiState.value.personagens + response.results,
                    paginaAtual = proximaPagina,
                    totalPaginas = response.info.pages
                )

                Log.d("RICKANDMORTY", response.results[0].name)
                //dao.inserirTodos(response.results)
                dao.inserirTodos(response.results.map { it.toEntity() })
            } catch (e:Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    erro = "Erro ao carregar: ${e.message}"
                )
            }
        }
    }
    private fun CharacterDto.toEntity() = PersonagemEntity(
        id = id, name = name, species = species, status = status, image = image
    )
}
