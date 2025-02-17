package tn.iptv.nextplayer.login


import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Build.UNKNOWN
import android.provider.Settings.Secure
import android.util.Log
import tn.iptv.nextplayer.MVVM.BaseViewModel
import java.lang.reflect.Method
import java.net.NetworkInterface
import java.util.Collections

class LoginViewModel : BaseViewModel() {


    lateinit var bindingModel: LoginBindingModel

    @SuppressLint("LogNotTimber", "HardwareIds", "MissingPermission")
    override fun initBindingData(context: Context) {

        bindingModel = LoginBindingModel()

        val androidId = Secure.getString(app.contentResolver, Secure.ANDROID_ID)

        bindingModel.deviceId = androidId

        val deviceModel = Build.MODEL
        bindingModel.modelDevice = deviceModel


        try {
            bindingModel.serialNumber = getSerialNumber()
        } catch (e: SecurityException) {
            bindingModel.serialNumber = androidId
        }
        if (bindingModel.serialNumber.isEmpty())
            bindingModel.serialNumber = androidId


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

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private fun getSerialNumber(): String {
        var serialNumber: String?

        try {
            val c = Class.forName("android.os.SystemProperties")
            val get: Method = c.getMethod("get", String::class.java)

            // (?) Lenovo Tab (https://stackoverflow.com/a/34819027/1276306)
            serialNumber = get.invoke(c, "gsm.sn1")?.toString()
            if (serialNumber == "" || serialNumber.equals(UNKNOWN)) // Samsung Galaxy S5 (SM-G900F) : 6.0.1
            // Samsung Galaxy S6 (SM-G920F) : 7.0
            // Samsung Galaxy Tab 4 (SM-T530) : 5.0.2
            // (?) Samsung Galaxy Tab 2 (https://gist.github.com/jgold6/f46b1c049a1ee94fdb52)
                serialNumber = get.invoke(c, "ril.serialnumber")?.toString()
            if (serialNumber == "" || serialNumber.equals(UNKNOWN)) // Archos 133 Oxygen : 6.0.1
            // Google Nexus 5 : 6.0.1
                serialNumber = get.invoke(c, "ro.serialno")?.toString()
            if (serialNumber == "" || serialNumber.equals(UNKNOWN)) // (?) Samsung Galaxy Tab 3 (https://stackoverflow.com/a/27274950/1276306)
                serialNumber = get.invoke(c, "sys.serialnumber")?.toString()
            if (serialNumber == "" || serialNumber.equals(UNKNOWN)) // Archos 133 Oxygen : 6.0.1

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    serialNumber = Build.getSerial()
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    serialNumber = Build.SERIAL
                }

            // If none of the methods above worked
//            if (serialNumber == UNKNOWN) serialNumber = null
        } catch (e: Exception) {
            e.printStackTrace()
            println("Cannot retrieve device serial number: " + e.message)

            serialNumber = ""
            print("We cant get Serial Number so this is the Android ID: $serialNumber")
        }
        if (serialNumber.equals(UNKNOWN) || serialNumber.isNullOrEmpty()) {
            serialNumber = ""
        }

        return serialNumber
    }


}