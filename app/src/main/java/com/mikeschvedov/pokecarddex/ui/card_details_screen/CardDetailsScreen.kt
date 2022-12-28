package com.mikeschvedov.pokecarddex.ui.card_details_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.mikeschvedov.pokecarddex.data.remote.response.CardData
import com.mikeschvedov.pokecarddex.data.remote.response.SingleCardResponse
import com.mikeschvedov.pokecarddex.ui.theme.TypeElectric
import com.mikeschvedov.pokecarddex.ui.theme.statusBarBlue
import com.mikeschvedov.pokecarddex.utils.NetworkWrapper


@Composable
fun CardDetailsScreen(
    dominantColor: Color,
    cardId: String,
    navController: NavController,
    topPadding: Dp = 80.dp,
    cardImageSize: Dp = 400.dp,
    viewModel: CardDetailsViewModel = hiltViewModel()
) {
    // -- Getting Card Data from Api Response -- //
    val cardDetails =
        produceState<NetworkWrapper<SingleCardResponse>>(initialValue = NetworkWrapper.Loading()) {
            value = viewModel.getCardById(cardId)
        }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {

        // -- TOP SECTION -- //
        DetailsTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )

        // -- STATE WRAPPER -- //
        CardDetailsStateWrapper(
            viewModel = viewModel,
            cardDetails = cardDetails,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + cardImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + cardImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
        )

        // -- IMAGE -- //
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {

            if (cardDetails is NetworkWrapper.Success) {

                val cardImage = cardDetails.data?.data?.images?.large
                val painter = rememberImagePainter(
                    data = cardImage,
                    builder = {
                        ImageRequest.Builder(LocalContext.current)
                            .data(cardImage)
                            .build()
                    },
                )

                Image(
                    painter = painter,
                    modifier = Modifier
                        .size(cardImageSize)
                        .offset(y = topPadding),
                    contentDescription = "Pokemon"
                )
            }
        }
    }
}

@Composable
fun DetailsTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier.background(
            Brush.verticalGradient(
                listOf(
                    Color.Black,
                    Color.Transparent
                )
            )
        )
    ) {
        Icon(imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                })
    }
}

@Composable
fun CardDetailsStateWrapper(
    cardDetails: NetworkWrapper<SingleCardResponse>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,
    viewModel: CardDetailsViewModel = hiltViewModel()
) {
    when (cardDetails) {
        is NetworkWrapper.Success -> {
            val details = cardDetails.data?.data
            MainDetailsSection(
                viewModel = viewModel,
                cardDetails = details,
                modifier = modifier
                    .offset(y = (-20).dp)
                    .fillMaxWidth()
            )
        }
        is NetworkWrapper.Error -> {
            Text(
                text = cardDetails.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is NetworkWrapper.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
    }
}

@Composable
fun MainDetailsSection(
    cardDetails: CardData?,
    modifier: Modifier = Modifier,
    viewModel: CardDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .offset(y = 210.dp)
            .verticalScroll(scrollState)
    ) {
        CardSetDetails(
            cardDetails = cardDetails,
            modifier = Modifier
                .padding(16.dp)
        )
        ExternalLinkSection(
            cardDetails = cardDetails,
            viewModel = viewModel
        )
    }
}


@Composable
fun CardSetDetails(
    cardDetails: CardData?,
    modifier: Modifier = Modifier,
) {

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
    ) {

        Column(
            horizontalAlignment = CenterHorizontally,
        ) {
            DetailsText(
                text = "- Set Name -",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            DetailsText(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "${cardDetails?.set?.name}",
                fontSize = 16.sp
            )
        }

        Column(
            horizontalAlignment = CenterHorizontally,
        ) {
            DetailsText(
                text = "- Release Date -",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            DetailsText(
                text = "${cardDetails?.set?.releaseDate}",
                modifier = Modifier.padding(bottom = 16.dp),
                fontSize = 16.sp
            )
        }
    }
}


@Composable
fun DetailsText(
    modifier: Modifier = Modifier,
    text: String = "",
    color: Color = TypeElectric,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontWeight = fontWeight,
        fontSize = fontSize
    )
}

@Composable
fun ExternalLinkSection(
    cardDetails: CardData?,
    viewModel: CardDetailsViewModel = hiltViewModel()
){
    DetailsText(
        text = "- External Links -",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
    Spacer(modifier = Modifier.height(12.dp))
    HyperlinkText(
        fullText = "Link to PriceCharting",
        linkText = listOf("Link to PriceCharting"),
        hyperlinks = listOf(viewModel.calculatePriceChartingUrl(cardDetails)),
        linkTextColor = Color.White,
        fontSize = 16.sp
    )
    Spacer(modifier = Modifier.height(12.dp))
    HyperlinkText(
        fullText = "Link to eBay",
        linkText = listOf("Link to eBay"),
        hyperlinks = listOf(viewModel.calculateEBayUrl(cardDetails)),
        linkTextColor = Color.White,
        fontSize = 16.sp
    )
}

@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier,
    fullText: String,
    linkText: List<String>,
    linkTextColor: Color = Color.Blue,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    hyperlinks: List<String>,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)
        linkText.forEachIndexed { index, link ->
            val startIndex = fullText.indexOf(link)
            val endIndex = startIndex + link.length
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontSize = fontSize,
                    fontWeight = linkTextFontWeight,
                    textDecoration = linkTextDecoration
                ),
                start = startIndex,
                end = endIndex
            )
            addStringAnnotation(
                tag = "URL",
                annotation = hyperlinks[index],
                start = startIndex,
                end = endIndex,
            )
        }
        addStyle(
            style = SpanStyle(
                fontSize = fontSize
            ),
            start = 0,
            end = fullText.length
        )
    }

    val uriHandler = LocalUriHandler.current

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick = {
                annotatedString
                    .getStringAnnotations("URL", it, it)
                    .firstOrNull()?.let { stringAnnotation ->
                        uriHandler.openUri(stringAnnotation.item)
                    }
    })
}