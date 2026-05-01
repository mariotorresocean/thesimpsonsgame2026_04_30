package com.oceanbrasil.thesimpsonsgame

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class GameState(
    val loading: Boolean = true,
    val character: CharacterDto?= null,
    val score: Int = 0
)

class GameViewModel : ViewModel() {

    // Expose screen UI state
    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    init {
        loadNext()
    }

    fun loadNext() {
        viewModelScope.launch {
            val personagem = ApiFactory.api.getCharacter((1..100).random())
            _uiState.value = GameState(loading=false, personagem, uiState.value.score)
        }
    }

    fun resposta(resp:Boolean) {

        if (resp) { // jogador acha que está vivo
            if (uiState.value.character?.status == "Alive") {
                //Acertou
                // score + 1
                _uiState.value = _uiState.value.copy(score = uiState.value.score  + 1 )
            } else {
                //Errou
                // score -1
                _uiState.value = _uiState.value.copy(score = uiState.value.score  -1 )
            }
        } else { //jogador acha que está morto
            if (uiState.value.character?.status == "Alive") {
                //Errou
                // score -1
                _uiState.value = _uiState.value.copy(score = uiState.value.score  - 1 )
            } else {
                //Acertou
                // score + 1
                _uiState.value = _uiState.value.copy(score = uiState.value.score  + 1 )
            }
        }

        loadNext()

    }
}