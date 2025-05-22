package h.ideas4life.ui.navigation

object Routes {
    const val Home = "HomeScreen"
    const val Detail = "detail/{ideaId}"
}

// Función para construir la ruta con un ID concreto
fun detailRouteWithId(id: String) = "detail/$id"