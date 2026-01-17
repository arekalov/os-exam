package com.arekalov.osexam.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.arekalov.osexam.presentation.detail.TicketDetailEffect
import com.arekalov.osexam.presentation.detail.TicketDetailViewModel
import com.arekalov.osexam.presentation.list.TicketListEffect
import com.arekalov.osexam.presentation.list.TicketListViewModel
import com.arekalov.osexam.ui.screens.ImageViewerScreen
import com.arekalov.osexam.ui.screens.TicketDetailScreen
import com.arekalov.osexam.ui.screens.TicketListScreen

private const val ANIMATION_DURATION = 150

@Composable
fun AppNavHost() {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Routes.LIST
    ) {
        composable(
            route = Routes.LIST,
            enterTransition = { fadeIn(animationSpec = tween(ANIMATION_DURATION)) },
            exitTransition = { fadeOut(animationSpec = tween(ANIMATION_DURATION)) }
        ) {
            val viewModel: TicketListViewModel = hiltViewModel()
            LaunchedEffect(viewModel) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is TicketListEffect.NavigateToTicket -> {
                            navController.navigate(Routes.detail(effect.number))
                        }
                    }
                }
            }
            TicketListScreen(viewModel = viewModel)
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("number") { type = NavType.StringType }),
            enterTransition = { fadeIn(animationSpec = tween(ANIMATION_DURATION)) },
            exitTransition = { fadeOut(animationSpec = tween(ANIMATION_DURATION)) }
        ) {
            val viewModel: TicketDetailViewModel = hiltViewModel()
            LaunchedEffect(viewModel) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is TicketDetailEffect.NavigateToImage -> {
                            navController.navigate(Routes.image(effect.path))
                        }
                    }
                }
            }
            TicketDetailScreen(viewModel = viewModel)
        }
        composable(
            route = Routes.IMAGE,
            arguments = listOf(navArgument("path") { type = NavType.StringType }),
            enterTransition = { fadeIn(animationSpec = tween(ANIMATION_DURATION)) },
            exitTransition = { fadeOut(animationSpec = tween(ANIMATION_DURATION)) }
        ) { backStackEntry ->
            val path = backStackEntry.arguments?.getString("path").orEmpty()
            ImageViewerScreen(
                path = path,
                onClose = { navController.popBackStack() }
            )
        }
    }
}
