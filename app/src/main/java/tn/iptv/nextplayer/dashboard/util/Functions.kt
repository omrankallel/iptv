import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity


@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.coloredShadow(
    color: Color,
    alpha: Float = 0.2f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = 20.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
) = composed {
    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparent = color.copy(alpha = 0f).toArgb()
    this.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparent
            frameworkPaint.setShadowLayer(
                shadowRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor,
            )
            it.drawRoundRect(
                0f,
                0f,
                this.size.width,
                this.size.height,
                borderRadius.toPx(),
                borderRadius.toPx(),
                paint,
            )
        }
    }
}


fun  fetchString  (context  : Context, id : Int) :String {
    return  context.getString(id)
}



fun openUrlWithVLC(context: Context, url: String) {
    val vlcIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
       // setPackage("org.videolan.vlc") // VLC package name
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      //  type = "video/*" // Explicit MIME type
    }


    // Check if VLC is installed
    if (vlcIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(vlcIntent)
    } else {
     //   Toast.makeText(context, "VLC Player is not installed.", Toast.LENGTH_SHORT).show()
        // Optionally open the URL in a browser as a fallback
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Add this flag for fallback too
        }
        context.startActivity(browserIntent)
    }
}

