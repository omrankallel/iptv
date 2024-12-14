package tn.iptv.nextplayer.MVVM

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import org.koin.java.KoinJavaComponent


abstract class  BaseViewModel : ViewModel() {

    val app: Application by KoinJavaComponent.inject(Application::class.java)

    @SuppressLint("StaticFieldLeak")
    var activity: MVVMBaseActivity<*>? = null

    abstract fun initBindingData()
}