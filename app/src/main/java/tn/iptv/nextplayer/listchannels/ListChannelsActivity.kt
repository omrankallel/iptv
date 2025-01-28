package tn.iptv.nextplayer.listchannels

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.MVVM.MVVMBaseActivity
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.listchannels.layouts.ListChannelWithCarousel
import tn.iptv.nextplayer.listchannels.ui.theme.IptvTheme
import tn.iptv.nextplayer.listchannels.ui.theme.back_application_end2_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_application_end_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_application_start_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_custom_drawer
import tn.iptv.nextplayer.login.LoginActivity


class ListChannelsActivity : MVVMBaseActivity<ListChannelViewModel>() {
    private val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)


    override fun setUpUseCase(): ListChannelViewModel {
        val listChannelViewModel: ListChannelViewModel by viewModel()
        return listChannelViewModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 1)
        }


        enableEdgeToEdge()
        setContent {
            IgnoreStatusBarScreen()
            IptvTheme {
                val activity = LocalView.current.context as Activity
                activity.window.statusBarColor = back_application_start_color.toArgb()

                val wic = WindowCompat.getInsetsController(window, window.decorView)
                wic.isAppearanceLightStatusBars = !isSystemInDarkTheme()

                Scaffold(
                    containerColor = back_custom_drawer,
                    topBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(back_application_start_color),
                        )
                    },
                    bottomBar = {

                    },
                    content = { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            back_application_start_color,
                                            back_application_end_color,
                                            back_application_end2_color,
                                        ),
                                    ),
                                ),
                        ) {

                            ListChannelWithCarousel(
                                viewModel,
                                onSelectChannel = { channelSelect ->
                                    viewModel.bindingModel.channelSelected.value = channelSelect
                                    channelManager.channelSelected.postValue(channelSelect)
                                    startActivity(Intent(this@ListChannelsActivity, LoginActivity::class.java))

                                },
                            )

                            /*  ListChannelScreen(viewModel, onSelectChannel = { channelSelect->
                                  viewModel.bindingModel.channelSelected.value = channelSelect
                                  channelManager.channelSelected.postValue(channelSelect)
                                  startActivity(Intent(this@ListChannelsActivity , LoginActivity::class.java))

                              })*/
                        }
                    },


                    )
            }

            hideSystemUI()
        }
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


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Composable
fun IgnoreStatusBarScreen() {
    val systemUiController = rememberSystemUiController()

    // Hide or ignore the status bar
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = true, // Change this depending on your app theme
    )

    // Optional: to draw content behind the status bar, use this:
    systemUiController.isStatusBarVisible = false

    /*   Box(
           modifier = Modifier
               .fillMaxSize()
               .background(back_application_start_color)
       ) {
           // Your content here
       }*/
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IptvTheme {

        Greeting("Android")
    }
}