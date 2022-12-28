package com.mikeschvedov.pokecarddex.ui.cards_list_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.Coil
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.mikeschvedov.pokecarddex.R
import com.mikeschvedov.pokecarddex.data.models.PokemonCardData
import com.mikeschvedov.pokecarddex.ui.theme.*
import com.mikeschvedov.pokecarddex.utils.Constants.CARD_DETAILS_SCREEN
import com.mikeschvedov.ultimate_utility_box.logger.Logger
import com.mikeschvedov.ultimate_utility_box.logger.LoggerService

@Composable
fun CardsListScreen(
    navController: NavController,
    viewModel: CardsListScreenViewModel = hiltViewModel()
) {

    val defaultDominantColor = Color.White
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.cardexlogo),
                contentDescription = "Pokemon Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(CenterHorizontally)
            )
            SearchBar(
                hint = "Enter a Pokemon Name...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) { inputString ->
                viewModel.queryText.value = inputString
            }
            SearchButton {
                viewModel.clearList()
                viewModel.loadPokemonPaginated()
            }
            CardsList(navController = navController){ clickedCard->
                viewModel.fetchColors(clickedCard.cardImage) {
                    LoggerService.info("====This is the Clicked Card====")
                    LoggerService.info("====This is the Clicked Card====")
                    LoggerService.info("====This is the Clicked Card====")
                    LoggerService.info("$clickedCard.")
                    dominantColor = it
                    navController.navigate(
                        "${CARD_DETAILS_SCREEN}/${dominantColor.toArgb()}/${clickedCard.cardId}"
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var displayedText by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = displayedText,
            onValueChange = { newText ->
                displayedText = newText
                onSearch(newText) // Trigger the onSearch function
            },
            maxLines = 1,
            singleLine = true,

            cursorBrush = SolidColor(statusBarBlue),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 20.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged { currentFocus ->
                    isHintDisplayed = !currentFocus.isFocused
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun CardsList(
    navController: NavController,
    viewModel: CardsListScreenViewModel = hiltViewModel(),
    onCardClicked: (PokemonCardData) -> Unit
) {
    // States
    val cardsList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val totalCount by remember { viewModel.totalResultCount }

    Spacer(modifier = Modifier.height(16.dp))
    if (totalCount != 0) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Center,
        ) {
            Text(
                text = "$totalCount Results",
                color = TypeElectric,
                fontSize = 18.sp
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        // Because each row has 2 cards
        val itemCount = if (cardsList.size % 2 == 0) {
            cardsList.size / 2
        } else {
            cardsList.size / 2 + 1
        }

        items(itemCount) {
            if (it >= itemCount - 1 && !endReached && !isLoading) {
                viewModel.loadPokemonPaginated()
            }
            ListRow(
                rowIndex = it,
                entries = cardsList,
                navController = navController,
                onCardClicked = onCardClicked
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
            )
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadPokemonPaginated()
            }
        }
    }
}


@Composable
fun ListRow(
    rowIndex: Int,
    entries: List<PokemonCardData>,
    navController: NavController,
    onCardClicked: (PokemonCardData) -> Unit
) {
    Column {
        Row {
            ListEntry(
                cardData = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f),
                onCardClicked = onCardClicked
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                ListEntry(
                    cardData = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    onCardClicked = onCardClicked
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ListEntry(
    cardData: PokemonCardData,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CardsListScreenViewModel = hiltViewModel(),
    onCardClicked: (PokemonCardData) -> Unit
) {


    Box(
        contentAlignment = Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .height(250.dp)
            .width(80.dp)
            .background(
                Color.Black
            )
            .clickable {
                onCardClicked(cardData)
            }
    ) {
        Column {
            val painter = rememberImagePainter(
                data = cardData.cardImage,
                builder = {
                    ImageRequest.Builder(LocalContext.current)
                        .data(cardData.cardImage)
                        .build()
                },
            )

            if (viewModel.isLoading.value) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(1f)
                )
            } else {
                Image(
                    painter = painter,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    contentDescription = cardData.cardId
                )

            }
        }
    }
}


@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(100.dp))
        Button(
            onClick = onRetry,
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}


@Composable
fun SearchButton(
    onSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = onSearch,
            colors = ButtonDefaults.buttonColors(backgroundColor = TypeElectric),
            modifier = Modifier
                .align(CenterHorizontally)
                .clip(RoundedCornerShape(10.dp))
                .width(220.dp)
                .shadow(5.dp, RoundedCornerShape(10.dp))
                .border(
                    width = 5.dp,
                    color = logoBlue
                )
        ) {
            Text(text = "Search", fontSize = 20.sp, color = logoBlue)
        }
    }
}