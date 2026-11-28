package cl.duoc.mpazpro

import android.util.Log
import cl.duoc.mpazpro.model.PostExterno
import cl.duoc.mpazpro.model.Unidad
import cl.duoc.mpazpro.repository.NovedadesRepository
import cl.duoc.mpazpro.repository.UnidadRepository
import cl.duoc.mpazpro.viewmodel.NovedadesUiState
import cl.duoc.mpazpro.viewmodel.NovedadesViewModel
import cl.duoc.mpazpro.viewmodel.UnidadUiState
import cl.duoc.mpazpro.viewmodel.UnidadViewModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelsTest : FunSpec({

    val testDispatcher = UnconfinedTestDispatcher()

    beforeEach {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    afterEach {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    test("UnidadViewModel emite Exito con la lista de unidades cuando el repositorio responde OK") {
        val repoMock = mockk<UnidadRepository>()
        val listaPrueba = listOf(
            Unidad(1L, "Cuentos chilenos", "Lenguaje y Comunicación", "OA02"),
            Unidad(2L, "Sumas y restas", "Matemática", "OA05")
        )
        coEvery { repoMock.listar() } returns Result.success(listaPrueba)

        val viewModel = UnidadViewModel(repoMock)

        val estado = viewModel.uiState.value
        estado.shouldBeInstanceOf<UnidadUiState.Exito>()
        estado.unidades.size.shouldBe(2)
        estado.unidades.first().titulo.shouldBe("Cuentos chilenos")
    }

    test("UnidadViewModel emite Error sin lanzar excepción cuando falla la red") {
        val repoMock = mockk<UnidadRepository>()
        coEvery { repoMock.listar() } returns Result.failure(RuntimeException("Sin conexión"))

        val viewModel = UnidadViewModel(repoMock)

        val estado = viewModel.uiState.value
        estado.shouldBeInstanceOf<UnidadUiState.Error>()
        estado.mensaje.shouldBe("Sin conexión")
    }

    test("UnidadViewModel crear y eliminar invocan el repositorio y recargan la lista") {
        val repoMock = mockk<UnidadRepository>()
        val unidadCreada = Unidad(3L, "Los seres vivos", "Ciencias Naturales", "OA01")
        coEvery { repoMock.listar() } returns Result.success(listOf(unidadCreada))
        coEvery { repoMock.crear(any()) } returns Result.success(unidadCreada)
        coEvery { repoMock.eliminar(3L) } returns Result.success(Unit)

        val viewModel = UnidadViewModel(repoMock)
        var callbackEjecutado = false

        viewModel.crear("Los seres vivos", "Ciencias Naturales", "oa01") {
            callbackEjecutado = true
        }
        callbackEjecutado.shouldBe(true)
        coVerify(exactly = 1) { repoMock.crear(any()) }

        viewModel.eliminar(3L)
        coVerify(exactly = 1) { repoMock.eliminar(3L) }
    }

    test("NovedadesViewModel emite Exito al obtener publicaciones de la API externa") {
        val novedadesRepoMock = mockk<NovedadesRepository>()
        val posts = listOf(PostExterno(userId = 1, id = 10, title = "Noticia escolar", body = "Contenido"))
        coEvery { novedadesRepoMock.obtenerNovedades() } returns Result.success(posts)

        val viewModel = NovedadesViewModel(novedadesRepoMock)

        val estado = viewModel.uiState.value
        estado.shouldBeInstanceOf<NovedadesUiState.Exito>()
        estado.publicaciones.size.shouldBe(1)
        estado.publicaciones.first().title.shouldBe("Noticia escolar")
    }
})