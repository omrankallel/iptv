package tn.iptv.nextplayer.listchannels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.MVVM.BaseViewModel
import tn.iptv.nextplayer.domain.channelManager.ChannelManager


class ListChannelViewModel : BaseViewModel() {

    private val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)

    lateinit var bindingModel: ListChannelBindingModel


    @SuppressLint("LogNotTimber")
    override fun initBindingData(context: Context) {

        Log.d("channel_vm", " initBindingData")
        bindingModel = ListChannelBindingModel()

        channelManager.fetchListChannels()


        channelManager.channelIsLoading.observeForever {
            bindingModel.isLoadingChannel.value = it
        }

        channelManager.listOfChannels.observeForever { listChannel ->
            bindingModel.listChannels.value = listChannel
            listChannel.forEach { channel ->
                Log.d("channel_vm", "--->  ${channel.toString()}")
            }

        }

    }
}