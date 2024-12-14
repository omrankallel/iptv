package tn.iptv.nextplayer.dashboard.screens.serieDetails


import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil3.request.ImageRequest

import coil3.video.VideoFrameDecoder
import coil3.video.videoFrameMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tn.iptv.nextplayer.R

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun VideoThumbnail(videoPath: String) {
    val context = LocalContext.current
    val  video  = "http://glplus.me:8000/movie/BqIAkUeS2joJuv6/dLV46jxpWnQ/329908.mp4"
     var bitmap by remember { mutableStateOf<Bitmap?>(null) }

   LaunchedEffect(videoPath) {
        bitmap = loadThumbnailFromVideoUrl( videoPath)
    }

    Box( modifier = Modifier
        .width(100.dp)
        .height(50.dp))
    {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Video Thumbnail",
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
        }

    }

}


@RequiresApi(Build.VERSION_CODES.P)
suspend fun loadThumbnailFromVideoUrl(videoUrl: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val retriever = MediaMetadataRetriever()
            // Set the video URL
            retriever.setDataSource(videoUrl, HashMap<String, String>())
            // Retrieve a frame at the first second
            val bitmap = retriever.getFrameAtTime(1 * 1000 * 1000 * 60 * 15) // 1 second into the video
            retriever.release()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}




suspend fun loadThumbnailAsync(context: Context, videoPath: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(videoPath)
            retriever.getFrameAtTime(100 * 60 *30) // Adjust the frame time as needed 1000000
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }
}

fun generateThumbnailFromVideo(context: Context, videoPath: String): Bitmap? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(videoPath)
        retriever.getFrameAtTime(100 * 60 *30) // Adjust time as needed
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        retriever.release()
    }
}