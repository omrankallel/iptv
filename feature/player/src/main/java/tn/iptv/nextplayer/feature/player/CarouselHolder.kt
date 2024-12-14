package tn.iptv.nextplayer.feature.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.iptv.nextplayer.feature.player.databinding.HolderCarouselBinding

class CarouselHolder<T : CarouselEntity>(
    private val binding: HolderCarouselBinding,
    private val clicked: (T) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: T) {
        Glide.with(binding.root)
            .load(data.imgUrl)
            .into(binding.iv) // Utilisez le binding pour accéder à l'image

        binding.root.setOnClickListener {
            clicked(data)
        }
    }

    companion object {
        fun <T : CarouselEntity> create(parent: ViewGroup, clicked: (T) -> Unit): CarouselHolder<T> {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HolderCarouselBinding.inflate(inflater, parent, false)
            return CarouselHolder(binding, clicked)
        }
    }
}
