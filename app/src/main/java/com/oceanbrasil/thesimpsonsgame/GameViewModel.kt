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
//    val loading: Boolean = true,
    val character: CharacterDto?= null
)

class GameViewModel : ViewModel() {

    // Expose screen UI state
    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val personagem = ApiFactory.api.getCharacter((1..100).random())
            _uiState.value = GameState(personagem)
        }
    }
}