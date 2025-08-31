package com.naruto.world.app.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {
    object Home : Screen("home")
    object Characters : Screen("characters")
    object CharacterDetail : Screen(
        route = "character/{id}",
        arguments = listOf(navArgument("id") { type = NavType.LongType })
    ) {
        fun createRoute(id: Long) = "character/$id"
    }
    object Clans : Screen("clans")
    object ClanDetail : Screen(
        route = "clan/{id}",
        arguments = listOf(navArgument("id") { type = NavType.LongType })
    ) {
        fun createRoute(id: Long) = "clan/$id"
    }
    object Villages : Screen("villages")
    object VillageDetail : Screen(
        route = "village/{id}",
        arguments = listOf(navArgument("id") { type = NavType.LongType })
    ) {
        fun createRoute(id: Long) = "village/$id"
    }
    object KekkeiGenkai : Screen("kekkei-genkai")
    object TailedBeasts : Screen("tailed-beasts")
    object Teams : Screen("teams")
    object Akatsuki : Screen("akatsuki")
    object Kara : Screen("kara")
}