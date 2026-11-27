package cl.duoc.mpazpro.viewmodel

import android.util.Log
import androidx.lifecycle.*
import cl.duoc.mpazpro.model.Unidad
import cl.duoc.mpazpro.repository.UnidadRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class UnidadUiState {
    data object Cargando : UnidadUiState()
    data class Exito(val unidades: List<Unidad>) : UnidadUiState()
    data class Error(val mensaje: String) : UnidadUiState()
}

class UnidadViewModel(private val repo: UnidadRepository = UnidadRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<UnidadUiState>(UnidadUiState.Cargando)
    val uiState: StateFlow<UnidadUiState> = _uiState.asStateFlow()
    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _uiState.value = UnidadUiState.Cargando
            repo.listar()
                .onSuccess { Log.d("UnidadViewModel", "Cargadas: ${it.size}"); _uiState.value = UnidadUiState.Exito(it) }
                .onFailure { _uiState.value = UnidadUiState.Error(it.message ?: "Error de conexión") }
        }
    }

    fun crear(titulo: String, asignatura: String, oa: String, onExito: () -> Unit = {}) {
        viewModelScope.launch {
            repo.crear(Unidad(titulo = titulo.trim(), asignatura = asignatura, oa = oa.trim().uppercase()))
                .onSuccess { cargar(); onExito() }
                .onFailure { _uiState.value = UnidadUiState.Error(it.message ?: "Error al crear") }
        }
    }

    fun actualizar(id: Long, titulo: String, asignatura: String, oa: String, onExito: () -> Unit = {}) {
        viewModelScope.launch {
            repo.actualizar(id, Unidad(id, titulo.trim(), asignatura, oa.trim().uppercase()))
                .onSuccess { cargar(); onExito() }
                .onFailure { _uiState.value = UnidadUiState.Error(it.message ?: "Error al actualizar") }
        }
    }

    fun eliminar(id: Long) {
        viewModelScope.launch {
            repo.eliminar(id).onSuccess { cargar() }
                .onFailure { _uiState.value = UnidadUiState.Error(it.message ?: "Error al eliminar") }
        }
    }

    companion object {
        val ASIGNATURAS_VALIDAS = listOf("Lenguaje y Comunicación", "Matemática", "Ciencias Naturales", "Historia y Geografía")
        fun validarTitulo(t: String) = when {
            t.trim().isEmpty() -> "El título es obligatorio"
            t.trim().length < 3 -> "Mínimo 3 caracteres"
            t.trim().length > 80 -> "Máximo 80 caracteres"
            else -> null
        }
        fun validarAsignatura(a: String) = if (a in ASIGNATURAS_VALIDAS) null else "Selecciona una asignatura"
        fun validarOa(oa: String) = if (Regex("^OA\\d{2}$").matches(oa.trim().uppercase())) null else "Formato inválido (ej. OA03)"
        fun esFormularioValido(t: String, a: String, oa: String) =
            validarTitulo(t) == null && validarAsignatura(a) == null && validarOa(oa) == null
    }
}

class UnidadViewModelFactory(private val repo: UnidadRepository = UnidadRepository()) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = UnidadViewModel(repo) as T
}
