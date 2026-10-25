package cl.duoc.mpazpro.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.mpazpro.model.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    fun onRutCambiado(nuevoRut: String) {
        _uiState.update { it.copy(rut = nuevoRut) }
        validarFormulario()
    }

    fun onNombreCambiado(nuevoNombre: String) {
        _uiState.update { it.copy(nombre = nuevoNombre) }
        validarFormulario()
    }

    fun onCorreoCambiado(nuevoCorreo: String) {
        _uiState.update { it.copy(correo = nuevoCorreo) }
        validarFormulario()
    }

    fun onRolCambiado(nuevoRol: String) {
        _uiState.update { it.copy(rolSeleccionado = nuevoRol) }
        validarFormulario()
    }

    private fun validarFormulario() {
        val erroresTemp = mutableMapOf<String, String>()
        val estadoActual = _uiState.value

        // Validación RUT Ficticio (No vacío y mínimo 8 caracteres)
        if (estadoActual.rut.isBlank()) {
            erroresTemp["rut"] = "El RUT / ID es obligatorio"
        } else if (estadoActual.rut.length < 8) {
            erroresTemp["rut"] = "Ingrese un RUT válido de prueba"
        }

        // Validación Nombre
        if (estadoActual.nombre.isBlank()) {
            erroresTemp["nombre"] = "El nombre del usuario es obligatorio"
        }

        // Validación Correo Institucional
        if (estadoActual.correo.isBlank()) {
            erroresTemp["correo"] = "El correo es obligatorio"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(estadoActual.correo).matches()) {
            erroresTemp["correo"] = "Formato de correo inválido"
        }

        _uiState.update { currentState ->
            currentState.copy(
                errores = erroresTemp,
                esValido = erroresTemp.isEmpty()
            )
        }
    }
}