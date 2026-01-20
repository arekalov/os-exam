package com.arekalov.osexam.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arekalov.osexam.presentation.blocks.BlocksEffect
import com.arekalov.osexam.presentation.blocks.BlocksViewModel
import com.arekalov.osexam.presentation.blocktickets.BlockTicketsEffect
import com.arekalov.osexam.presentation.blocktickets.BlockTicketsViewModel
import com.arekalov.osexam.presentation.detail.TicketDetailEffect
import com.arekalov.osexam.presentation.detail.TicketDetailViewModel
import com.arekalov.osexam.presentation.list.TicketListEffect
import com.arekalov.osexam.presentation.list.TicketListViewModel
import com.arekalov.osexam.ui.screens.BlocksScreen
import com.arekalov.osexam.ui.screens.BlockTicketsScreen
import com.arekalov.osexam.ui.screens.ImageViewerScreen
import com.arekalov.osexam.ui.screens.TicketDetailScreen
import com.arekalov.osexam.ui.screens.TicketListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LIST
    ) {
        composable(Routes.LIST) {
            val viewModel: TicketListViewModel = hiltViewModel()
            LaunchedEffect(viewModel) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is TicketListEffect.NavigateToTicket -> {
                            navController.navigate(Routes.detail(effect.number))
                        }
                        is TicketListEffect.NavigateToBlocks -> {
                            navController.navigate(Routes.BLOCKS)
                        }
                    }
                }
            }
            TicketListScreen(viewModel = viewModel)
        }
        
        composable(Routes.BLOCKS) {
            val viewModel: BlocksViewModel = hiltViewModel()
            LaunchedEffect(viewModel) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is BlocksEffect.NavigateToBlock -> {
                            navController.navigate(Routes.blockTickets(effect.blockId))
                        }
                    }
                }
            }
            BlocksScreen(viewModel = viewModel)
        }
        
        composable(
            route = Routes.BLOCK_TICKETS,
            arguments = listOf(navArgument("blockId") { type = NavType.StringType })
        ) {
            val viewModel: BlockTicketsViewModel = hiltViewModel()
            LaunchedEffect(viewModel) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is BlockTicketsEffect.NavigateToTicket -> {
                            navController.navigate(Routes.detail(effect.number))
                        }
                    }
                }
            }
            BlockTicketsScreen(viewModel = viewModel)
        }
        
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("number") { type = NavType.StringType })
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
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val path = backStackEntry.arguments?.getString("path").orEmpty()
            ImageViewerScreen(
                path = path,
                onClose = { navController.popBackStack() }
            )
        }
    }
}
