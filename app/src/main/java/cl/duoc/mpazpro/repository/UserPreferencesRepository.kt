package cl.duoc.mpazpro.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cl.duoc.mpazpro.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "mpazpro_user_prefs")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        val KEY_RUT = stringPreferencesKey("user_rut")
        val KEY_NOMBRE = stringPreferencesKey("user_nombre")
        val KEY_CORREO = stringPreferencesKey("user_correo")
        val KEY_ROL = stringPreferencesKey("user_rol")
        val KEY_SESION_ACTIVA = booleanPreferencesKey("sesion_activa")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        UserPreferences(
            rut = preferences[KEY_RUT] ?: "",
            nombre = preferences[KEY_NOMBRE] ?: "",
            correo = preferences[KEY_CORREO] ?: "",
            rol = preferences[KEY_ROL] ?: "Estudiante",
            sesionActiva = preferences[KEY_SESION_ACTIVA] ?: false
        )
    }

    suspend fun guardarSesion(
        rut: String,
        nombre: String,
        correo: String,
        rol: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[KEY_RUT] = rut
            preferences[KEY_NOMBRE] = nombre
            preferences[KEY_CORREO] = correo
            preferences[KEY_ROL] = rol
            preferences[KEY_SESION_ACTIVA] = true
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}