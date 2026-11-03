# MPAZ PRO 2.º
 
Prototipo (MVP) de una aplicación Android de apoyo didáctico para Segundo Básico, desarrollado en **Kotlin** con **Jetpack Compose** para la asignatura **DSY1105 – Desarrollo de Aplicaciones Móviles** (Duoc UC).
 
El caso corresponde a la Escuela Escritora Marcela Paz (San Bernardo). Esta entrega cubre la **interfaz funcional** de la app: formulario de acceso validado, navegación, gestión de estado, almacenamiento local, un recurso nativo y animaciones.
 
> Todos los datos son **ficticios**. No se usan RUT, nombres ni credenciales reales.
 
## Qué incluye esta entrega
 
| Requisito | Implementación | Archivo principal |
|---|---|---|
| Diseño visual | Material Design 3 (Scaffold, TopAppBar, OutlinedTextField, FilterChip, Card, NavigationDrawer, NavigationBar) | `ui/theme/screen/` |
| Formulario validado | Validación por campo en tiempo real, mensaje de error e ícono de error | `viewmodel/UsuarioViewModel.kt`, `ui/theme/screen/UsuarioScreen.kt` |
| Navegación | Navigation Compose con rutas centralizadas y eventos de navegación desde el ViewModel | `MainActivity.kt`, `navigation/` |
| Gestión de estado | `StateFlow` + `UiState` inmutable; `SharedFlow` para eventos únicos de navegación | `viewmodel/`, `model/UsuarioUiState.kt` |
| Almacenamiento local | DataStore Preferences (RUT/ID, nombre, correo y rol) | `repository/UserPreferencesRepository.kt` |
| Recurso nativo | Selector de fotos del sistema (Photo Picker), sin permisos | `ui/theme/screen/ProfileScreen.kt` |
| Animaciones | `AnimatedVisibility` (slide + fade) e indicador de carga | `ui/theme/screen/components/AnimatedBanner.kt` |
| Adaptabilidad | `BoxWithConstraints` (columna bajo 600 dp, fila sobre 600 dp) y formulario con scroll | `ui/theme/screen/HomeScreen.kt` |
 
## Flujo de la aplicación
 
1. **Acceso de usuarios**: formulario con RUT/ID ficticio, nombre, correo y rol (Estudiante, Docente o UTP).
2. **Resumen de registro**: muestra los datos ingresados.
3. **Home**: pantalla principal con menú lateral y barra inferior.
4. **Perfil**: muestra los datos y permite elegir una foto de la galería.
5. **Configuración**: pantalla reservada (aún sin funcionalidad).
Al volver a abrir la app, el formulario se rellena con los datos guardados.
 
## Arquitectura (MVVM)
 
```
cl.duoc.mpazpro
├── MainActivity.kt                 Punto de entrada, NavHost y creación de ViewModels
├── model/                          UsuarioUiState, UserPreferences
├── navigation/                     Screen (rutas), NavigationEvent
├── repository/                     UserPreferencesRepository (único acceso a DataStore)
├── viewmodel/                      UsuarioViewModel, MainViewModel
└── ui/theme/
    ├── Color.kt, Theme.kt, Type.kt
    └── screen/
        ├── UsuarioScreen, ResumenScreen, HomeScreen, ProfileScreen, SettingsScreen
        └── components/AnimatedBanner.kt
```
 
| Capa | Responsabilidad |
|---|---|
| **Model** | Datos inmutables: estado de la pantalla y datos guardados |
| **ViewModel** | Mantiene el estado, valida el formulario y coordina el guardado |
| **Repository** | Lee y escribe en DataStore; la UI y el ViewModel no conocen su detalle |
| **Screens / Composables** | Dibujan el estado y envían las acciones del usuario al ViewModel |
 
## Tecnologías
 
- Kotlin 2.2.10 y Android Gradle Plugin 9.4.1
- Jetpack Compose (BOM 2026.02.01) y Material 3
- Navigation Compose 2.7.7
- DataStore Preferences 1.1.1
- Coil 2.6.0 (carga de imágenes)
- `compileSdk` 37, `minSdk` 24, `targetSdk` 35
## Cómo ejecutar
 
1. Abrir el proyecto con una versión reciente de Android Studio (compatible con AGP 9.4.1).
2. Esperar a que termine **Sync Project with Gradle Files**.
3. Crear o usar un emulador (probado en Pixel 7 Pro, API 35) o un dispositivo con Android 7.0 o superior.
4. Ejecutar la configuración `app` (Run).
Datos de prueba sugeridos: RUT/ID `12345678`, nombre `Ana Perez`, correo `ana@ejemplo.cl`.
 
## Validaciones del formulario
 
| Campo | Regla |
|---|---|
| RUT / ID ficticio | Obligatorio, mínimo 8 caracteres |
| Nombre completo | Obligatorio, mínimo 3 caracteres |
| Correo institucional | Obligatorio, con formato de correo válido |
 
Los errores solo se muestran cuando el usuario ya interactuó con el campo. El botón de confirmar permanece deshabilitado mientras haya algún error y durante el guardado.
 
## Decisiones de diseño
 
- **DataStore en lugar de Room**: se guardan pocos datos simples (clave-valor), sin relaciones ni consultas; DataStore requiere menos código y encaja con el alcance de esta entrega.
- **La foto de perfil no se persiste**: el selector del sistema entrega una URI con permiso de lectura temporal, por eso solo vive en el estado de la sesión.
- **Sin permisos en el Manifest**: el Photo Picker lo opera el sistema y entrega únicamente la imagen elegida (principio de mínimo privilegio).
- **Eventos de navegación con `SharedFlow`**: son acciones de una sola vez; un `StateFlow` podría repetirlas al recomponer.
- **`BoxWithConstraints` para la adaptabilidad**: solo se necesita el ancho disponible, por lo que resulta más simple que clases de tamaño de ventana.
## Limitaciones conocidas
 
- El Home es una pantalla de bienvenida; los contenidos por asignatura del caso no forman parte de este alcance.
- Configuración es una pantalla reservada.
- Los datos de DataStore no están cifrados; al ser datos ficticios se priorizó la simplicidad.
- No incluye consumo de API ni pruebas unitarias en esta etapa.
## Flujo de trabajo con Git
 
Se trabajó con una rama por funcionalidad (`feature/...`, `chore/...`), mensajes de commit descriptivos (`feat:`, `fix:`, `chore:`) e integración en `master`.
 
## Autor
 
Román Suárez — Ingeniería en Informática, Duoc UC. Proyecto desarrollado de forma individual.
 
