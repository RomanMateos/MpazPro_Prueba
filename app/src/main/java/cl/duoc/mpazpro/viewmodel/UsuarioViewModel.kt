package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // Banderas para controlar si el usuario ya interactuó con los campos (evita errores prematuros)
    private var rutTocado = false
    private var nombreTocado = false
    private var correoTocado = false

    init {
        // Cargar datos persistidos desde DataStore si existen
        viewModelScope.launch {
            val prefs = repository.userPreferencesFlow.first()
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

    fun guardarSesionYContinuar(onExito: () -> Unit) {
        viewModelScope.launch {
            val estado = _uiState.value
            repository.guardarSesion(estado.rut, estado.nombre, estado.correo, estado.rolSeleccionado)

            // Mostrar animación del banner antes de navegar
            _mostrarBanner.value = true
            delay(1200) // Pausa de 1.2 segundos para apreciar la animación
            _mostrarBanner.value = false
            onExito()
        }
    }

    private fun validarFormulario() {
        val erroresTemp = mutableMapOf<String, String>()
        val estadoActual = _uiState.value

        // Validación RUT Ficticio
        if (estadoActual.rut.isBlank()) {
            if (rutTocado) erroresTemp["rut"] = "El RUT / ID es obligatorio"
        } else if (estadoActual.rut.length < 8) {
            if (rutTocado) erroresTemp["rut"] = "Ingrese un RUT válido de prueba (mínimo 8 dígitos)"
        }

        // Validación Nombre
        if (estadoActual.nombre.isBlank()) {
            if (nombreTocado) erroresTemp["nombre"] = "El nombre del usuario es obligatorio"
        } else if (estadoActual.nombre.trim().length < 3) {
            if (nombreTocado) erroresTemp["nombre"] = "El nombre debe tener al menos 3 letras"
        }

        // Validación Correo Institucional
        if (estadoActual.correo.isBlank()) {
            if (correoTocado) erroresTemp["correo"] = "El correo es obligatorio"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(estadoActual.correo).matches()) {
            if (correoTocado) erroresTemp["correo"] = "Formato de correo inválido"
        }

        val esValido = estadoActual.rut.length >= 8 &&
                estadoActual.nombre.trim().length >= 3 &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(estadoActual.correo).matches()

        _uiState.update { currentState ->
            currentState.copy(
                errores = erroresTemp,
                esValido = esValido
            )
        }
    }
}