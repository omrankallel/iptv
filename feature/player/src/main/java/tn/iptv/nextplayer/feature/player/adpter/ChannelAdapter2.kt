package tn.iptv.nextplayer.feature.player.adpter

import android.content.Context
import tn.iptv.nextplayer.feature.player.model.grouped_media.MediaItem
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
import tn.iptv.nextplayer.feature.player.R


class ChannelAdapter2 (val  context  : Context,
                       private val mList: List<MediaItem>,
                       private val indexCurrentChannel : Int  = 0  ,
                       private val listener: OnChannelClickListener

)  : RecyclerView.Adapter<ChannelAdapter2.ViewHolder>(){

    private var selectedPosition: Int = -1 // Initially, no item is selected
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentLiveTVChannel =  mList[position]

        // sets the image to the imageview from our itemHolder class
       // holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.txtIndexChannel.text =  "${position+1}"
        holder.txtTitleChannel.text = currentLiveTVChannel.name

        //setUpFavorite
        if (position == 3)
            holder. imgFavorite.setImageDrawable(AppCompatResources.getDrawable(context,  R.drawable.ic_favorite) )
        else
            //holder. imgFavorite.setImageResource(R.drawable.ic_not_favorite)
         holder. imgFavorite.setImageDrawable(AppCompatResources.getDrawable(context,  R.drawable.ic_not_favorite) )



        // Set the background based on the selection
        if (position == selectedPosition) {
            holder.cardChannel.setBackgroundResource(R.drawable.back_selected_channel)
            holder. imgFavorite.visibility = View.VISIBLE
        } else if (position == indexCurrentChannel) {
            holder.cardChannel.setBackgroundResource(R.drawable.back_current_channel)
            holder. imgFavorite.visibility = View.GONE
        } else {
            holder.cardChannel.setBackgroundResource(R.drawable.back_transparent)
            holder. imgFavorite.visibility = View.VISIBLE
        }


        // Set up click listener
        holder.cardChannel.setOnClickListener {
            listener.onChannelClick(position, currentLiveTVChannel)

            val previousPosition = selectedPosition
            selectedPosition = position

            // Refresh the previous and current selected positions
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

        }


        if (currentLiveTVChannel.icon != null)
            Glide.with(context).load(currentLiveTVChannel.icon).into(holder.imgImageChannel  )

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val txtTitleChannel: TextView = itemView.findViewById(R.id.txt_title_channel)
        val  txtIndexChannel: TextView = itemView.findViewById(R.id.txt_index_channel)
        val  imgImageChannel: AppCompatImageView = itemView.findViewById(R.id.img_channel)
        val  imgFavorite: ImageView = itemView.findViewById(R.id.img_favorite)
        val  cardChannel: RelativeLayout = itemView.findViewById(R.id.card_channel)


    }
}