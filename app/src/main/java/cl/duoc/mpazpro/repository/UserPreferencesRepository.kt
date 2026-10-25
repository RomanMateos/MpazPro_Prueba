package cl.duoc.mpazpro.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para instanciar DataStore
private val Context.dataStore by preferencesDataStore(name = "mpazpro_user_prefs")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        val KEY_RUT = stringPreferencesKey("user_rut")
        val KEY_NOMBRE = stringPreferencesKey("user_nombre")
        val KEY_ROL = stringPreferencesKey("user_rol")
        val KEY_SESION_ACTIVA = booleanPreferencesKey("sesion_activa")
    }

    // Flujo para leer el rol guardado
    val userRol: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_ROL] ?: "Estudiante"
    }

    // Guardar datos de sesión localmente
    suspend fun guardarSesion(rut: String, nombre: String, rol: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_RUT] = rut
            preferences[KEY_NOMBRE] = nombre
            preferences[KEY_ROL] = rol
            preferences[KEY_SESION_ACTIVA] = true
        }
    }

    // Cerrar sesión
    suspend fun cerrarSesion() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}