package tn.iptv.nextplayer.feature.player.adpter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.feature.player.PlayerActivity
import tn.iptv.nextplayer.feature.player.R
import tn.iptv.nextplayer.feature.player.model.grouped_media.MediaItem
import tn.iptv.nextplayer.feature.player.utils.AppHelper


class ChannelAdapter2(
    val context: Context,
    private val mList: List<MediaItem>,
    private var indexCurrentChannel: Int = 0,
    private val favoriteViewModel: FavoriteViewModel,
    private val listener: OnChannelClickListener,
    private val menuContainer: LinearLayout,

    ) : RecyclerView.Adapter<ChannelAdapter2.ViewHolder>() {

    private var selectedPosition: Int = -1

    fun toggleFavoriteUp(recyclerView: RecyclerView) {

        if (selectedPosition < mList.size - 1) {
            if (selectedPosition == -1) selectedPosition = 0
            val previousPosition = selectedPosition
            selectedPosition++;
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            recyclerView.scrollToPosition(selectedPosition)
        }
    }

    fun toggleFavoriteDown(recyclerView: RecyclerView) {

        if (selectedPosition > 0) {
            val previousPosition = selectedPosition
            selectedPosition--;
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            recyclerView.scrollToPosition(selectedPosition)
        }
    }


    fun selectedChannel(app: PlayerActivity, recyclerView: RecyclerView) {
        val position: Int = if (selectedPosition == -1) 0 else selectedPosition
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) as? ViewHolder
        val isVisible: Boolean = viewHolder?.favoriteLayout?.visibility == View.VISIBLE
        if (isVisible) {
            favoriteViewModel.isFavoriteExists(mList[position].id) { exists ->
                if (exists) {
                    viewHolder?.imgFavorite?.visibility = View.GONE
                    favoriteViewModel.deleteFavoriteById(mList[position].id)
                    viewHolder?.txtFavorite?.text = "Ajouter aux Favoris"
                } else {
                    viewHolder?.imgFavorite?.visibility = View.VISIBLE
                    favoriteViewModel.addFavorite(mList[position].id, mList[position].name, mList[position].icon, mList[position].url, "", "", "", "", "", "", "", "Live TV")
                    viewHolder?.txtFavorite?.text = "Supprimer des favoris"
                }
                viewHolder?.favoriteLayout?.visibility = View.GONE
                notifyItemChanged(position)
            }
        } else if (indexCurrentChannel == selectedPosition || selectedPosition == -1) {
            app.showHidePlayerGesture()
        } else {
            val previousIndex = indexCurrentChannel
            indexCurrentChannel = selectedPosition

            notifyItemChanged(previousIndex)
            notifyItemChanged(indexCurrentChannel)
            app.playVideo(Uri.parse(mList[indexCurrentChannel].url))
        }
    }

    fun openFavorite(recyclerView: RecyclerView) {
        val position: Int = if (selectedPosition == -1) 0 else selectedPosition
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) as? ViewHolder
        val isVisible: Boolean = viewHolder?.favoriteLayout?.visibility == View.VISIBLE
        if (isVisible) {
            viewHolder?.favoriteLayout?.visibility = View.GONE
        } else {
            viewHolder?.favoriteLayout?.visibility = View.VISIBLE
            if(position < mList.size) {
                recyclerView.smoothScrollToPosition(position+1)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val currentLiveTVChannel = mList[position]
        var isFavorite = false

        favoriteViewModel.isFavoriteExists(currentLiveTVChannel.id) { exists ->
            isFavorite = exists

            if (isFavorite) {
                holder.imgFavorite.visibility = View.VISIBLE
                holder.txtFavorite.text = "Supprimer des favoris"
            } else {
                holder.imgFavorite.visibility = View.GONE
                holder.txtFavorite.text = "Ajouter aux Favoris"
            }

        }





        holder.txtIndexChannel.text = "${position + 1}"
        holder.txtTitleChannel.text = AppHelper.cleanChannelName(currentLiveTVChannel.name)


        if ((position == selectedPosition && position == indexCurrentChannel) || position == indexCurrentChannel) {
            holder.cardChannel.setBackgroundResource(R.drawable.back_current_channel)
        } else if (position == selectedPosition) {
            holder.cardChannel.setBackgroundResource(R.drawable.back_selected_channel)
        } else {
            holder.cardChannel.setBackgroundResource(R.drawable.back_transparent)
        }


        holder.imgFavorite.setOnClickListener {
            menuContainer.visibility = View.GONE
        }


        holder.cardChannel.setOnClickListener {
            listener.onChannelClick(position, currentLiveTVChannel)

            val previousCurrentPosition = indexCurrentChannel
            val previousPosition = selectedPosition
            selectedPosition = position
            indexCurrentChannel = selectedPosition

            notifyItemChanged(previousCurrentPosition)
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            notifyItemChanged(indexCurrentChannel)

        }

        holder.cardChannel.setOnLongClickListener {
            if (holder.favoriteLayout.visibility == View.VISIBLE) {
                holder.favoriteLayout.visibility = View.GONE
            } else {
                holder.favoriteLayout.visibility = View.VISIBLE
            }
            true
        }
        holder.favoriteLayout.setOnClickListener {
            if (isFavorite) {
                holder.imgFavorite.visibility = View.GONE
                favoriteViewModel.deleteFavoriteById(currentLiveTVChannel.id)
                holder.txtFavorite.text = "Ajouter aux Favoris"
            } else {
                holder.imgFavorite.visibility = View.VISIBLE
                favoriteViewModel.addFavorite(currentLiveTVChannel.id, currentLiveTVChannel.name, currentLiveTVChannel.icon, currentLiveTVChannel.url, "", "", "", "", "", "", "", "Live TV")
                holder.txtFavorite.text = "Supprimer des favoris"
            }
            isFavorite = !isFavorite



            holder.favoriteLayout.visibility = View.GONE
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
        val favoriteLayout: LinearLayout = this.itemView.findViewById(R.id.favorite_layout)
        val txtFavorite: TextView = this.itemView.findViewById(R.id.txtFavorite)

    }
}