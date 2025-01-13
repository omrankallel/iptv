package tn.iptv.nextplayer.dashboard.screens


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.google.accompanist.pager.ExperimentalPagerApi
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.core.data.models.Favorite
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.screens.serieDetails.Chip
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.listchannels.ui.theme.backCardMovie
import tn.iptv.nextplayer.listchannels.ui.theme.borderFrame


@OptIn(ExperimentalPagerApi::class)
@Composable
fun FavoriteScreen(viewModel: DashBoardViewModel, onSelectFavorite: (Favorite) -> Unit) {

    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    val listOfFavorites = channelManager.listOfFavorites.value
    val isLoading = viewModel.bindingModel.isLoadingFavorite
    val showAllState = rememberSaveable { mutableStateOf<List<Favorite>?>(null) }
    val itemShowAllState = rememberSaveable { mutableStateOf<String?>(null) }

    if (showAllState.value != null) {
        AllItemsFavorite(
            type = itemShowAllState.value!!,
            items = showAllState.value!!,
            onSelectFavorite = onSelectFavorite,
            onBack = { showAllState.value = null },
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {

            if (isLoading.value)
                CircularProgressIndicator(color = borderFrame)
            else {
                val groupedFavorites = listOfFavorites?.groupBy { it.type }
                if (groupedFavorites != null) {
                    FavoritesList(
                        groupedFavorites = groupedFavorites, onSelectFavorite,
                        onShowAll = { type, favorites ->
                            itemShowAllState.value = type
                            showAllState.value = favorites
                        },
                    )
                }
            }

        }
    }
}

@Composable
fun FavoritesList(groupedFavorites: Map<String, List<Favorite>>, onSelectFavorite: (Favorite) -> Unit, onShowAll: (String, List<Favorite>) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        groupedFavorites.forEach { (type, favorites) ->
            item {

                Text(
                    text = type,
                    fontSize = androidx.compose.material3.MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.height(5.dp))
            }

            item {

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(favorites.take(4)) { favorite ->
                        FavoriteItem(favorite = favorite, onSelectFavorite)
                    }

                    if (favorites.size > 4) item {
                        ShowAllCard {
                            onShowAll(type, favorites)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AllItemsFavorite(
    type: String,
    items: List<Favorite>,
    onSelectFavorite: (Favorite) -> Unit,
    onBack: () -> Unit,
) {

    BackHandler(onBack = { onBack() })



    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items.chunked(6)) { rowItems ->
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(rowItems.take(6)) { favorite ->
                    FavoriteItem(favorite, onSelectFavorite = { onSelectFavorite(it) })
                }

            }
        }
    }

}

@Composable
fun ShowAllCard(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(210.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Gray.copy(alpha = 0.2f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GridIcon(
                modifier = Modifier.size(48.dp),
            )
            Spacer(modifier = Modifier.height(10.dp))
            androidx.compose.material3.Text(
                text = "Afficher tout",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun FavoriteItem(favorite: Favorite, onSelectFavorite: (Favorite) -> Unit) {
    Box(
        modifier = Modifier
            .width(210.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(backCardMovie)
            .shadow(1.dp)
            .padding(2.dp)
            .clickable {
                onSelectFavorite.invoke(favorite)
            },

        )

    {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
        )

        {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {

                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth(),
                ) {

                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(3.dp))
                            .background(backCardMovie),
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(favorite.icon)
                            .crossfade(true)
                            .scale(Scale.FILL)
                            .listener(
                                onError = { request, throwable ->
                                    Log.e("ImageLoadError", "Failed to load image: ${throwable.throwable.message}")
                                },
                            )
                            .build(),
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.placeholder_image),
                        error = painterResource(R.drawable.error_image),
                        contentScale = ContentScale.Crop,
                    )

                    if (favorite.genre.isNotEmpty()) {

                        val castList = favorite.genre.split(",").map { it.trim() }
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(5.dp)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            castList.take(2).forEach { cast ->
                                Chip(cast)
                            }
                        }
                    }

                }


                Spacer(modifier = Modifier.height(7.dp))

                Row(Modifier.fillMaxSize()) {

                    androidx.compose.material3.Text(
                        text = favorite.name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        color = Color.White,
                        lineHeight = 15.sp,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                }

            }

        }

    }
}

@Composable
fun GridIcon(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        repeat(2) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(),
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF9C89E0)),
                    )
                }
            }
        }
    }
}
