# MpazPro

Aplicación Android desarrollada en Kotlin que integra consumo de servicios mediante Retrofit hacia un microservicio en **Spring Boot** y una **API externa**, gestionando el estado desde `UsuarioViewModel`, con pruebas unitarias en **Kotest** y **MockK** y generación de APK firmado en modo *release*.

## Estructura del repositorio

* `app/` — Aplicación móvil Android (Kotlin, Retrofit, ViewModel, pruebas unitarias).
* `backend/backend/` — Microservicio REST en **Spring Boot** (Maven, Spring Web, Spring Data JPA y base de datos H2 en memoria).

## Arquitectura y componentes principales

### Aplicación Android (`app/`)
* **Capa de red (`data.remote`):** `BackendApiService`, `ExternalApiService` y `RetrofitInstance`.
* **Estado (`viewmodel`):** `UsuarioViewModel` coordina las llamadas remotas y expone el estado a la interfaz.
* **Pruebas unitarias (`src/test`):** `UsuarioViewModelTest` con **Kotest** y **MockK** (flags JVM: `-Dnet.bytebuddy.experimental=true`, `-XX:+EnableDynamicAgentLoading`).

### Microservicio Backend (`backend/backend/`)
* **Paquete `cl.duoc.backend`:** `UsuarioController` (endpoints REST), `Usuario` (entidad JPA) y `UsuarioRepository` (Spring Data JPA).
* **Configuración (`application.properties`):** Puerto `8080` y base de datos H2 en memoria (`jdbc:h2:mem:mpazdb`, consola en `/h2-console`).

## Ejecución del Backend (Spring Boot)

1. Entra a la carpeta del microservicio: `cd backend/backend`
2. Inicia el servidor con Maven Wrapper:
   * **Git Bash / Linux / macOS:** `./mvnw spring-boot:run`
   * **Windows CMD / PowerShell:** `mvnw.cmd spring-boot:run`
3. Disponible en `http://localhost:8080` (desde el emulador Android: `http://10.0.2.2:8080/`).

## Pruebas unitarias y generación de APK (`release`)

* **Pruebas:** Ejecuta `UsuarioViewModelTest` desde Android Studio.
* **APK firmado:** En Android Studio, ve a **Build > Generate Signed App Bundle / APK... > APK**, selecciona `mpazpro-key.jks` y compila la variante **release** (`app/build/outputs/apk/release/app-release.apk`).
