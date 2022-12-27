package com.mikeschvedov.pokecarddex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mikeschvedov.pokecarddex.ui.card_details_screen.CardDetailsScreen
import com.mikeschvedov.pokecarddex.ui.cards_list_screen.CardsListScreen
import com.mikeschvedov.pokecarddex.ui.theme.PokeCardDexTheme
import com.mikeschvedov.pokecarddex.utils.Constants.CARDS_LIST_SCREEN
import com.mikeschvedov.pokecarddex.utils.Constants.CARD_DETAILS_SCREEN
import com.mikeschvedov.ultimate_utility_box.logger.LoggerService
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeCardDexTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = CARDS_LIST_SCREEN) {

                    // -------------------------------- Pokemon List Screen -------------------------------- //
                    composable(CARDS_LIST_SCREEN) {
                        CardsListScreen(navController = navController)
                    }

                    // -------------------------------- Pokemon Details Screen ----------------------------- //
                    composable("${CARD_DETAILS_SCREEN}/{dominantColor}/{cardId}",
                        arguments = listOf(
                            navArgument("dominantColor") {
                                type = NavType.IntType
                            },
                            navArgument("cardId") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White
                        }
                        val cardId = remember {
                            it.arguments?.getString("cardId")
                        }

                        LoggerService.info("Getting this cardId: $cardId")

                        CardDetailsScreen(
                            dominantColor = dominantColor,
                            cardId = cardId ?: "",
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

