package com.example.mylist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mylist.data.repository.BookRepository
import com.example.mylist.ui.screens.BookDetailScreen
import com.example.mylist.ui.screens.FavoritesScreen
import com.example.mylist.ui.screens.HomeScreen
import com.example.mylist.ui.viewmodel.BookDetailViewModel
import com.example.mylist.ui.viewmodel.BookDetailViewModelFactory
import com.example.mylist.ui.viewmodel.FavoritesViewModel
import com.example.mylist.ui.viewmodel.FavoritesViewModelFactory
import com.example.mylist.ui.viewmodel.HomeViewModel
import com.example.mylist.ui.viewmodel.HomeViewModelFactory
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun BookApp(repository: BookRepository) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository)
            )
            HomeScreen(
                viewModel = viewModel,
                onBookClick = { id, author ->
                    val encodedId = URLEncoder.encode(id, StandardCharsets.UTF_8.toString())
                    val encodedAuthor = URLEncoder.encode(author, StandardCharsets.UTF_8.toString())
                    navController.navigate("detail/$encodedId/$encodedAuthor")
                },
                onFavoritesClick = {
                    navController.navigate("favorites")
                }
            )
        }

        composable("favorites") {
            val viewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModelFactory(repository)
            )
            FavoritesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onBookClick = { id, author ->
                    val encodedId = URLEncoder.encode(id, StandardCharsets.UTF_8.toString())
                    val encodedAuthor = URLEncoder.encode(author, StandardCharsets.UTF_8.toString())
                    navController.navigate("detail/$encodedId/$encodedAuthor")
                }
            )
        }

        composable(
            route = "detail/{workId}/{authorName}",
            arguments = listOf(
                navArgument("workId") { type = NavType.StringType },
                navArgument("authorName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedId = backStackEntry.arguments?.getString("workId") ?: ""
            val encodedAuthor = backStackEntry.arguments?.getString("authorName") ?: ""
            
            val workId = URLDecoder.decode(encodedId, StandardCharsets.UTF_8.toString())
            val authorName = URLDecoder.decode(encodedAuthor, StandardCharsets.UTF_8.toString())

            val viewModel: BookDetailViewModel = viewModel(
                factory = BookDetailViewModelFactory(repository)
            )

            BookDetailScreen(
                viewModel = viewModel,
                workId = workId,
                authorName = authorName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
