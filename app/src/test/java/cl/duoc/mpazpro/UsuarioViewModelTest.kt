package cl.duoc.mpazpro

import cl.duoc.mpazpro.data.remote.BackendApiService
import cl.duoc.mpazpro.data.remote.ExternalApiService
import cl.duoc.mpazpro.data.remote.PostDto
import cl.duoc.mpazpro.data.remote.UsuarioDto
import cl.duoc.mpazpro.model.UserPreferences
import cl.duoc.mpazpro.repository.UserPreferencesRepository
import cl.duoc.mpazpro.viewmodel.UsuarioViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    fun crearViewModel(
        mockBackend: BackendApiService = mockk(relaxed = true),
        mockExternal: ExternalApiService = mockk(relaxed = true)
    ): UsuarioViewModel {
        val mockRepo = mockk<UserPreferencesRepository>(relaxed = true)
        every { mockRepo.userPreferencesFlow } returns flowOf(
            UserPreferences("", "", "", "Usuario")
        )
        return UsuarioViewModel(mockRepo, mockBackend, mockExternal)
    }

    "validarFormulario debe marcar como válido cuando RUT, nombre y correo son correctos" {
        runTest {
            val vm = crearViewModel()
            advanceUntilIdle()

            vm.onRutCambiado("12345678-9")
            vm.onNombreCambiado("Román Pérez")
            vm.onCorreoCambiado("roman@duoc.cl")

            vm.uiState.value.esValido shouldBe true
            vm.uiState.value.errores.isEmpty() shouldBe true
        }
    }

    "validarFormulario debe mostrar error si el correo es inválido" {
        runTest {
            val vm = crearViewModel()
            advanceUntilIdle()

            vm.onCorreoCambiado("correo-malo")

            vm.uiState.value.esValido shouldBe false
            vm.uiState.value.errores["correo"] shouldBe "Formato de correo inválido"
        }
    }

    "cargarUsuariosRemotos debe obtener lista desde el microservicio Spring Boot" {
        runTest {
            val fakeUsers = listOf(UsuarioDto(1L, "Ana", "ana@duoc.cl"))
            val mockBackend = mockk<BackendApiService>()
            coEvery { mockBackend.getUsuarios() } returns fakeUsers

            val vm = crearViewModel(mockBackend = mockBackend)
            vm.cargarUsuariosRemotos()
            advanceUntilIdle()

            vm.usuariosRemotos.value shouldContainExactly fakeUsers
        }
    }

    "eliminarUsuarioRemoto debe llamar a DELETE en el microservicio" {
        runTest {
            val mockBackend = mockk<BackendApiService>(relaxed = true)
            coEvery { mockBackend.getUsuarios() } returns emptyList()

            val vm = crearViewModel(mockBackend = mockBackend)
            vm.eliminarUsuarioRemoto(1L)
            advanceUntilIdle()

            coVerify { mockBackend.eliminarUsuario(1L) }
        }
    }

    "cargarPostsExternos debe obtener datos desde la API externa JSONPlaceholder" {
        runTest {
            val fakePosts = listOf(PostDto(1, 1, "Título de prueba", "Cuerpo del post"))
            val mockExternal = mockk<ExternalApiService>()
            coEvery { mockExternal.getPosts() } returns fakePosts

            val vm = crearViewModel(mockExternal = mockExternal)
            vm.cargarPostsExternos()
            advanceUntilIdle()

            vm.postsExternos.value shouldContainExactly fakePosts
        }
    }
})