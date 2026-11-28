package cl.duoc.mpazpro

import cl.duoc.mpazpro.viewmodel.UnidadViewModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class UnidadValidacionTest : FunSpec({

    test("validarTitulo retorna null cuando el título tiene entre 3 y 80 caracteres") {
        UnidadViewModel.validarTitulo("Comprensión lectora").shouldBeNull()
        UnidadViewModel.validarTitulo("ABC").shouldBeNull()
    }

    test("validarTitulo retorna error cuando está vacío o tiene menos de 3 caracteres") {
        UnidadViewModel.validarTitulo("").shouldBe("El título es obligatorio")
        UnidadViewModel.validarTitulo("   ").shouldBe("El título es obligatorio")
        UnidadViewModel.validarTitulo("AB").shouldBe("Mínimo 3 caracteres")
    }

    test("validarTitulo retorna error cuando supera los 80 caracteres") {
        val tituloLargo = "A".repeat(81)
        UnidadViewModel.validarTitulo(tituloLargo).shouldBe("Máximo 80 caracteres")
    }

    test("validarAsignatura acepta únicamente las 4 asignaturas oficiales") {
        UnidadViewModel.validarAsignatura("Lenguaje y Comunicación").shouldBeNull()
        UnidadViewModel.validarAsignatura("Matemática").shouldBeNull()
        UnidadViewModel.validarAsignatura("Ciencias Naturales").shouldBeNull()
        UnidadViewModel.validarAsignatura("Historia y Geografía").shouldBeNull()
        UnidadViewModel.validarAsignatura("Educación Física").shouldNotBeNull()
    }

    test("validarOa exige el formato OA seguido de exactamente 2 dígitos") {
        UnidadViewModel.validarOa("OA01").shouldBeNull()
        UnidadViewModel.validarOa("oa12").shouldBeNull()
        UnidadViewModel.validarOa("OA1").shouldBe("Formato inválido (ej. OA03)")
        UnidadViewModel.validarOa("OA123").shouldBe("Formato inválido (ej. OA03)")
        UnidadViewModel.validarOa("OBJ03").shouldBe("Formato inválido (ej. OA03)")
    }

    test("esFormularioValido retorna true solo cuando los tres campos cumplen las reglas") {
        UnidadViewModel.esFormularioValido(
            t = "Fracciones básicas",
            a = "Matemática",
            oa = "OA04"
        ).shouldBe(true)

        UnidadViewModel.esFormularioValido(
            t = "Fr",
            a = "Matemática",
            oa = "OA04"
        ).shouldBe(false)
    }
})