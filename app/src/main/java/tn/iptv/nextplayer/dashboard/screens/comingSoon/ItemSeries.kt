package tn.iptv.nextplayer.dashboard.screens.comingSoon

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.dashboard.screens.comingSoon.UIUtils.HEIGHT_CARD
import tn.iptv.nextplayer.dashboard.screens.comingSoon.UIUtils.HEIGHT_IMAGE
import tn.iptv.nextplayer.dashboard.screens.comingSoon.UIUtils.WIDHT_CARD
import tn.iptv.nextplayer.dashboard.screens.serieDetails.Chip
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.domain.models.series.MediaType
import tn.iptv.nextplayer.listchannels.ui.theme.backCardMovie



@Composable
fun  ItemSeries (mediaType : MediaType,  seriesItem : MediaItem, onSelectSerie : (MediaItem) -> Unit){

Box (
    modifier = Modifier
        .width(WIDHT_CARD)
        .height(HEIGHT_CARD)

        .clip(RoundedCornerShape(5.dp))
        .background(backCardMovie)
        .shadow(1.dp)
        .padding(2.dp)
        .clickable {
            onSelectSerie(seriesItem)
        }

)

{

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(6.dp))

    {
        Column(modifier = Modifier
            .fillMaxSize()) {

            Box  (  modifier = Modifier

                .height(HEIGHT_IMAGE)
                .fillMaxWidth()

            ){

                AsyncImage(
                    modifier = Modifier.fillMaxSize()
                        .clip(RoundedCornerShape(3.dp))
                        .background(backCardMovie),
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(seriesItem.icon)
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
                    contentScale = ContentScale.FillBounds)

                // Tags Row
                if (seriesItem.genre.isNotEmpty() ){
                    // Split the string by comma and trim spaces
                    val castList = seriesItem.genre.split(",").map { it.trim() }
                    Row(
                        modifier = Modifier.align(Alignment.BottomStart).padding(5.dp).horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        castList.take(2) .forEach { cast ->
                                Chip(cast)
                            }
                    }
                }

            }


            Spacer(modifier = Modifier.height(5.dp))

            Column  (Modifier.fillMaxSize()){

                Text(
                    text = seriesItem.name,
                    fontSize =10.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    color = Color.White,
                    lineHeight = 15.sp
                )


                Spacer(modifier = Modifier.weight(1f))

                Row (Modifier.fillMaxWidth() ) {

                    Text(
                        text = seriesItem.date,
                        fontSize =10.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines =1,
                        color = Color.White,

                    )

                    Spacer(modifier = Modifier.weight(1f))


                    Row  (verticalAlignment =  Alignment.CenterVertically){

                        Image(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "Example Image",
                            modifier = Modifier
                                .size(12.dp)
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        Text(
                            text =  "8.9" ,// movieModel.rating   ,
                            fontSize =10.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            color = Color.White,
                        )

                    }

                }


            }





        }

    }

}


}