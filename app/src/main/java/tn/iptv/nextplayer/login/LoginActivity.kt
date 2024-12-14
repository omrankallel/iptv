package tn.iptv.nextplayer.login

import PreferencesHelper
import android.app.Activity
import android.app.Application
import android.content.Intent
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.MVVM.MVVMBaseActivity
import tn.iptv.nextplayer.dashboard.DashBoardActivity
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.listchannels.IgnoreStatusBarScreen
import tn.iptv.nextplayer.listchannels.ui.theme.IptvTheme
import tn.iptv.nextplayer.listchannels.ui.theme.back_application_end2_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_application_end_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_application_start_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_custom_drawer
import tn.iptv.nextplayer.login.layout.LoginScreen

val app: Application by KoinJavaComponent.inject(Application::class.java)

class LoginActivity : MVVMBaseActivity<LoginViewModel>() {

    private val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)

    override fun setUpUseCase(): LoginViewModel {
        val loginViewModel: LoginViewModel by viewModel()
        return loginViewModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PreferencesHelper.isUserLoggedIn(this)) {
            startActivity(Intent(this, DashBoardActivity::class.java))
            finish()
            return
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

                            LoginScreen(
                                viewModel,
                                onSuccess = {
                                    startActivity(Intent(this@LoginActivity, DashBoardActivity::class.java))

                                },
                                onFailure = {

                                },
                            )


                        }
                    },


                    )
            }
        }

        hideSystemUI()
    }


    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }


}

