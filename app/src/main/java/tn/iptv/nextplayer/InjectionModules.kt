package tn.iptv.nextplayer

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.domain.channelManager.ChannelImp
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.listchannels.ListChannelViewModel
import tn.iptv.nextplayer.login.LoginViewModel




val appModules = module {
    single<ChannelManager> { ChannelImp(androidApplication()) }
}

val viewModelModules = module {
    viewModel { ListChannelViewModel() }
    viewModel { LoginViewModel() }
    viewModel { DashBoardViewModel() }
}