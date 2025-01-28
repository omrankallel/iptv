package tn.iptv.nextplayer.MVVM

import android.os.Bundle
import androidx.activity.ComponentActivity


abstract class MVVMBaseActivity<VM : BaseViewModel> : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        // define our activity viewModel
        viewModel = setUpUseCase()
        // save current activity
        viewModel.activity = this
        // init data binding
        viewModel.initBindingData()


        super.onCreate(savedInstanceState)

    }


    lateinit var viewModel: VM

    abstract fun setUpUseCase(): VM
}