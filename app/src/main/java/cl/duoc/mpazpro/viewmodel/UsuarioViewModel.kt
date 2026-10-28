package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.model.UserPreferences
import cl.duoc.mpazpro.model.UsuarioUiState
import cl.duoc.mpazpro.repository.UserPreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    private val _mostrarBanner = MutableStateFlow(false)
    val mostrarBanner: StateFlow<Boolean> = _mostrarBanner.asStateFlow()

    private var rutTocado = false
    private var nombreTocado = false
    private var correoTocado = false

    init {
        viewModelScope.launch {
            val prefs: UserPreferences = repository.userPreferencesFlow.first()
            if (prefs.rut.isNotBlank() || prefs.nombre.isNotBlank()) {
                _uiState.update {
                    it.copy(
                        rut = prefs.rut,
                        nombre = prefs.nombre,
                        correo = prefs.correo,
                        rolSeleccionado = prefs.rol
                    )
                }
                rutTocado = true
                nombreTocado = true
                correoTocado = true
                validarFormulario()
            }
        }
    }

    fun onRutCambiado(nuevoRut: String) {
        rutTocado = true
        _uiState.update { it.copy(rut = nuevoRut) }
        validarFormulario()
    }

    fun onNombreCambiado(nuevoNombre: String) {
        nombreTocado = true
        _uiState.update { it.copy(nombre = nuevoNombre) }
        validarFormulario()
    }

    fun onCorreoCambiado(nuevoCorreo: String) {
        correoTocado = true
        _uiState.update { it.copy(correo = nuevoCorreo) }
        validarFormulario()
    }

    fun onRolCambiado(nuevoRol: String) {
        _uiState.update { it.copy(rolSeleccionado = nuevoRol) }
        validarFormulario()
    }

    fun onFotoSeleccionada(uriString: String) {
        _uiState.update { it.copy(fotoUri = uriString) }
    }

    fun guardarSesionYContinuar(onExito: () -> Unit) {
        if (_uiState.value.guardando) return // Freno anti doble clic

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true) }
            val estado = _uiState.value

            repository.guardarSesion(
                rut = estado.rut,
                nombre = estado.nombre,
                correo = estado.correo,
                rol = estado.rolSeleccionado
            )

            _mostrarBanner.value = true
            delay(1200)
            _mostrarBanner.value = false
            _uiState.update { it.copy(guardando = false) }
            onExito()
        }
    }

    private fun validarFormulario() {
        val estadoActual = _uiState.value
        val todosLosErrores = mutableMapOf<String, String>()

        if (estadoActual.rut.isBlank()) {
            todosLosErrores["rut"] = "El RUT / ID es obligatorio"
        } else if (estadoActual.rut.length < 8) {
            todosLosErrores["rut"] = "Ingrese un RUT válido de prueba (mínimo 8 dígitos)"
        }

        if (estadoActual.nombre.isBlank()) {
            todosLosErrores["nombre"] = "El nombre del usuario es obligatorio"
        } else if (estadoActual.nombre.trim().length < 3) {
            todosLosErrores["nombre"] = "El nombre debe tener al menos 3 letras"
        }

        if (estadoActual.correo.isBlank()) {
            todosLosErrores["correo"] = "El correo es obligatorio"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(estadoActual.correo).matches()) {
            todosLosErrores["correo"] = "Formato de correo inválido"
        }

        val esFormularioValido = todosLosErrores.isEmpty()

        val erroresVisibles = todosLosErrores.filterKeys { campo ->
            when (campo) {
                "rut" -> rutTocado
                "nombre" -> nombreTocado
                "correo" -> correoTocado
                else -> true
            }
        }

        _uiState.update { currentState ->
            currentState.copy(
                errores = erroresVisibles,
                esValido = esFormularioValido
            )
        }
    }
}