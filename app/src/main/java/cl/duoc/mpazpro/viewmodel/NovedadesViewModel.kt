package cl.duoc.mpazpro.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.model.PostExterno
import cl.duoc.mpazpro.repository.NovedadesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NovedadesUiState {
    data object Cargando : NovedadesUiState()
    data class Exito(val publicaciones: List<PostExterno>) : NovedadesUiState()
    data class Error(val mensaje: String) : NovedadesUiState()
}

class NovedadesViewModel(
    private val repository: NovedadesRepository = NovedadesRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<NovedadesUiState>(NovedadesUiState.Cargando)
    val uiState: StateFlow<NovedadesUiState> = _uiState.asStateFlow()

    init {
        cargarNovedades()
    }

    fun cargarNovedades() {
        viewModelScope.launch {
            _uiState.value = NovedadesUiState.Cargando
            repository.obtenerNovedades()
                .onSuccess { lista ->
                    Log.d("NovedadesViewModel", "Publicaciones externas cargadas: ${lista.size}")
                    _uiState.value = NovedadesUiState.Exito(lista)
                }
                .onFailure { error ->
                    Log.e("NovedadesViewModel", "Error API externa: ${error.message}")
                    _uiState.value = NovedadesUiState.Error(
                        error.message ?: "No se pudieron cargar las novedades"
                    )
                }
        }
    }
}

class NovedadesViewModelFactory(
    private val repository: NovedadesRepository = NovedadesRepository()
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NovedadesViewModel::class.java)) {
            return NovedadesViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}