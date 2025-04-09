package hse.diploma.cybersecplatform.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import hse.diploma.cybersecplatform.extensions.animatedComposable
import hse.diploma.cybersecplatform.model.VulnerabilityType
import hse.diploma.cybersecplatform.ui.components.systemBars.AppScaffold
import hse.diploma.cybersecplatform.ui.screens.favorites.FavoritesScreen
import hse.diploma.cybersecplatform.ui.screens.home.HomeScreen
import hse.diploma.cybersecplatform.ui.screens.home.TaskScreen
import hse.diploma.cybersecplatform.ui.screens.profile.ProfileScreen
import hse.diploma.cybersecplatform.ui.screens.statistics.StatisticsScreen

@Composable
fun MainNavigationGraph() {
    val mainNavController = rememberNavController()

    AppScaffold(
        navController = mainNavController, content = {
            NavHost(
                navController = mainNavController,
                startDestination = Screen.HomeScreen.route,
            ) {
                animatedComposable(Screen.HomeScreen.route) {
                    HomeScreen(mainNavController)
                }
                animatedComposable(Screen.Favorites.route) {
                    FavoritesScreen(mainNavController)
                }
                animatedComposable(Screen.Statistics.route) {
                    StatisticsScreen(mainNavController)
                }
                animatedComposable(Screen.Profile.route) {
                    ProfileScreen(mainNavController)
                }

                animatedComposable(Screen.TaskScreen.route) { backStackEntry ->
                    val typeString = backStackEntry.arguments?.getString("vulnerabilityType")
                    val type = VulnerabilityType.valueOf(typeString ?: "XSS")
                    TaskScreen(type)
                }
            }
        },
    )
}
