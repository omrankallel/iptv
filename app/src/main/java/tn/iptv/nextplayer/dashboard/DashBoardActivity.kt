package tn.iptv.nextplayer.dashboard

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.MVVM.MVVMBaseActivity
import tn.iptv.nextplayer.dashboard.layout.DashBoardScreen
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.listchannels.IgnoreStatusBarScreen
import tn.iptv.nextplayer.listchannels.ListChannelViewModel
import tn.iptv.nextplayer.listchannels.ui.theme.IptvTheme
import tn.iptv.nextplayer.listchannels.ui.theme.back_application_end2_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_application_end_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_application_start_color


class DashBoardActivity: MVVMBaseActivity<DashBoardViewModel>()  {
    private val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)


    override fun setUpUseCase(): DashBoardViewModel {
        val dashBoardViewModel: DashBoardViewModel by viewModel()
        return dashBoardViewModel
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            IgnoreStatusBarScreen()
            IptvTheme {
                val activity = LocalView.current.context as Activity
                activity.window.statusBarColor = back_application_start_color.toArgb()

                val wic = WindowCompat.getInsetsController(window, window.decorView)
                wic.isAppearanceLightStatusBars = !isSystemInDarkTheme()

                Scaffold(
                    topBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(back_application_start_color),
                        )
                    }, bottomBar = {

                    }, content = {paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            back_application_start_color,
                                            back_application_end_color,
                                            back_application_end2_color
                                        )
                                    )
                                )
                        ){

                            Log.e("dashboard_vm","   DashBoardScreen(viewModel)  ")
                            DashBoardScreen(viewModel)

                        }
                    }


                )
            }
        }

        hideSystemUI()
    }




    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Use WindowInsetsController to hide system bars
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Fallback for lower API levels
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }
}
