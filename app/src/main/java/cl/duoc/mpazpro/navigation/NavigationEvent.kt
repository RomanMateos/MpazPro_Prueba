package cl.duoc.mpazpro.navigation

// Representa las acciones de navegación que pueden ser emitidas desde un ViewModel
sealed class NavigationEvent {
    // Evento para navegar a un destino específico
    data class NavigateTo(val route: String) : NavigationEvent()

    // Evento para retroceder en la pila de pantallas
    data object NavigateBack : NavigationEvent()
}