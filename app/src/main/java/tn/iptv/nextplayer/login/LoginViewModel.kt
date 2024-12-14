package tn.iptv.nextplayer.login


import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings.Secure
import tn.iptv.nextplayer.MVVM.BaseViewModel
import java.net.NetworkInterface
import java.util.Collections

class LoginViewModel : BaseViewModel() {


    lateinit var bindingModel: LoginBindingModel

    @SuppressLint("LogNotTimber", "HardwareIds", "MissingPermission")
    override fun initBindingData() {

        bindingModel = LoginBindingModel()

        val androidId = Secure.getString(app.contentResolver, Secure.ANDROID_ID)

        bindingModel.deviceId = androidId

        val deviceModel = Build.MODEL
        bindingModel.modelDevice = deviceModel


        try {
            bindingModel.serialNumber = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Build.getSerial()
            } else {
                Build.SERIAL
            }
        } catch (e: SecurityException) {
            bindingModel.serialNumber = androidId
        }


        val macAddress = getMacAddress()

        if (macAddress == null)
            bindingModel.addressMac = ""
        else
            bindingModel.addressMac = macAddress


    }


    private fun getMacAddress(): String? {
        try {
            val all: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue
                val macBytes = nif.hardwareAddress ?: return null
                val res1 = StringBuilder()
                for (b in macBytes) {
                    res1.append(String.format("%02X:", b))
                }
                if (res1.isNotEmpty()) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }


}