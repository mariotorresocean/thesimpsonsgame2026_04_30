package com.oceanbrasil.thesimpsonsgame

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ListaState (
    val loading: Boolean = true,
    val personagens: List<CharacterDto> = emptyList()
)

class ListaViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ListaState())
    val uiState: StateFlow<ListaState> = _uiState.asStateFlow()

    init {
        carregarPersonagens()
    }

    fun carregarPersonagens() {
        viewModelScope.launch {
            val response = ApiFactory.api.getCharacters(1)
            _uiState.value = ListaState(loading=false,
                personagens = response.results)
            Log.d("RICKANDMORTY", response.results[0].name)
        }
    }
}