package com.naruto.world.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.naruto.world.app.ui.screens.CharacterDetailScreen
import com.naruto.world.app.ui.screens.CharacterListScreen
import com.naruto.world.app.ui.screens.ClanDetailScreen
import com.naruto.world.app.ui.screens.ClanListScreen
import com.naruto.world.app.ui.screens.HomeScreen

@Composable
fun NarutoNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCharacters = {
                    navController.navigate(Screen.Characters.route)
                },
                onNavigateToClans = {
                    navController.navigate(Screen.Clans.route)
                },
                onNavigateToVillages = {
                    navController.navigate(Screen.Villages.route)
                }
            )
        }

        composable(Screen.Characters.route) {
            CharacterListScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(Screen.CharacterDetail.createRoute(characterId))
                }
            )
        }

        composable(Screen.Clans.route) {
            ClanListScreen(navController = navController)
        }

        composable(Screen.Villages.route) {
            // TODO: Villages screen
        }

        composable(
            route = Screen.CharacterDetail.route,
            arguments = Screen.CharacterDetail.arguments
        ) { navBackStackEntry ->
            val characterId = navBackStackEntry.arguments?.getLong("id") ?: 0L
            CharacterDetailScreen(
                characterId = characterId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ClanDetail.route,
            arguments = Screen.ClanDetail.arguments
        ) { navBackStackEntry ->
            val clanId = navBackStackEntry.arguments?.getLong("id") ?: 0L
            ClanDetailScreen(
                navController = navController,
                clanId = clanId
            )
        }
    }
}