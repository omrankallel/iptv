package tn.iptv.nextplayer.feature.player.adpter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.feature.player.R
import tn.iptv.nextplayer.feature.player.model.grouped_media.MediaItem


class ChannelAdapter2(
    val context: Context,
    private val mList: List<MediaItem>,
    private val indexCurrentChannel: Int = 0,
    private val favoriteViewModel: FavoriteViewModel,
    private val listener: OnChannelClickListener,

    ) : RecyclerView.Adapter<ChannelAdapter2.ViewHolder>() {

    private var selectedPosition: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val currentLiveTVChannel = mList[position]
        var isFavorite = true

        holder.txtIndexChannel.text = "${position + 1}"
        holder.txtTitleChannel.text = currentLiveTVChannel.name


        when (position) {
            selectedPosition -> {
                holder.cardChannel.setBackgroundResource(R.drawable.back_selected_channel)
                holder.imgFavorite.visibility = View.VISIBLE
            }

            indexCurrentChannel -> {
                holder.cardChannel.setBackgroundResource(R.drawable.back_current_channel)
                holder.imgFavorite.visibility = View.GONE
            }

            else -> {
                holder.cardChannel.setBackgroundResource(R.drawable.back_transparent)
                holder.imgFavorite.visibility = View.VISIBLE
            }
        }

        holder.imgFavorite.setOnClickListener {
            if (isFavorite) {
                holder.imgFavorite.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_not_star))
                favoriteViewModel.deleteFavoriteById(currentLiveTVChannel.id)
            } else {
                holder.imgFavorite.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_star))

                favoriteViewModel.addFavorite(currentLiveTVChannel.id, "channel")
            }
            isFavorite = !isFavorite
        }


        holder.cardChannel.setOnClickListener {
            listener.onChannelClick(position, currentLiveTVChannel)

            val previousPosition = selectedPosition
            selectedPosition = position

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

        }


        Glide.with(context).load(currentLiveTVChannel.icon).into(holder.imgImageChannel)

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtTitleChannel: TextView = this.itemView.findViewById(R.id.txt_title_channel)
        val txtIndexChannel: TextView = this.itemView.findViewById(R.id.txt_index_channel)
        val imgImageChannel: AppCompatImageView = this.itemView.findViewById(R.id.img_channel)
        val imgFavorite: ImageView = this.itemView.findViewById(R.id.img_favorite)
        val cardChannel: RelativeLayout = this.itemView.findViewById(R.id.card_channel)


    }
}