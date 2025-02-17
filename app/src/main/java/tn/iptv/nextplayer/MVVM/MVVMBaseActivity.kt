package tn.iptv.nextplayer.MVVM

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


abstract class MVVMBaseActivity<VM : BaseViewModel> : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        // define our activity viewModel
        viewModel = setUpUseCase()
        // save current activity
        viewModel.activity = this
        // init data binding
        viewModel.initBindingData(this)


        super.onCreate(savedInstanceState)

    }




    lateinit var viewModel: VM

    abstract fun setUpUseCase(): VM
}