package tn.iptv.nextplayer.feature.player

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import tn.iptv.nextplayer.feature.player.model.ServerData

class ServiceAdapter(private val serviceList: List<ServerData>) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.serviceName)
        val serviceIcon: ImageView = itemView.findViewById(R.id.serviceIcon)
    }

    @SuppressLint("CutPasteId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_server, parent, false)

        val displayMetrics = DisplayMetrics()
        val windowManager = parent.context.getSystemService(Context.WINDOW_SERVICE) as android.view.WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val itemWidth = screenWidth / 3
        val itemHeight = (screenHeight * 0.5).toInt()
        val layoutParams = view.findViewById<LinearLayout>(R.id.service_item_layout).layoutParams
        layoutParams.width = itemWidth
        view.findViewById<LinearLayout>(R.id.service_item_layout).layoutParams = layoutParams

        val imageViewParams = view.findViewById<ImageView>(R.id.serviceIcon).layoutParams
        imageViewParams.height = itemHeight
        view.findViewById<ImageView>(R.id.serviceIcon).layoutParams = imageViewParams

        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = serviceList[position]
        holder.serviceName.text = service.name

        Glide.with(holder.itemView.context)
            .load(service.icon)
            .timeout(60000)
            .skipMemoryCache(true) // Ne pas utiliser le cache en mémoire
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .override(holder.itemView.width, holder.serviceIcon.height)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("GlideError", "Erreur lors du chargement de l'image", e)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Image chargée avec succès
                    return false
                }
            })
            .into(holder.serviceIcon)

        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(service.url))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }
}

fun applyReflection(originalImage: Bitmap): Bitmap {
    // Espace entre l'image originale et la réflexion
    val reflectionGap = 4
    // Obtenir la taille de l'image
    val width = originalImage.width
    val height = originalImage.height

    // Flip sur l'axe Y
    val matrix = Matrix().apply {
        preScale(1f, -1f)
    }

    // Créer un Bitmap avec le flip appliqué
    val reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false)

    // Créer un nouveau bitmap avec la réflexion
    val bitmapWithReflection = Bitmap.createBitmap(width, height + height / 2, Bitmap.Config.ARGB_8888)

    // Dessiner sur le Canvas
    val canvas = Canvas(bitmapWithReflection)
    canvas.drawBitmap(originalImage, 0f, 0f, null)
    val defaultPaint = Paint()
    canvas.drawRect(0f, height.toFloat(), width.toFloat(), (height + reflectionGap).toFloat(), defaultPaint)
    canvas.drawBitmap(reflectionImage, 0f, (height + reflectionGap).toFloat(), null)

    // Ajouter un shader de gradient linéaire
    val paint = Paint()
    val shader = LinearGradient(0f, originalImage.height.toFloat(), 0f, (bitmapWithReflection.height + reflectionGap).toFloat(),
        0x70FFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP)
    paint.shader = shader
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    // Dessiner le gradient
    canvas.drawRect(0f, height.toFloat(), width.toFloat(), (bitmapWithReflection.height + reflectionGap).toFloat(), paint)

    return bitmapWithReflection
}
