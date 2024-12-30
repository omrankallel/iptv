package tn.iptv.nextplayer.login.layout

import PreferencesHelper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.component.ButtonItem
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.domain.models.LoginModel
import tn.iptv.nextplayer.listchannels.ui.theme.backTextFiledLight
import tn.iptv.nextplayer.login.LoginViewModel
import tn.iptv.nextplayer.login.app

@Composable
fun LoginScreen(viewModel: LoginViewModel, onSuccess: () -> Unit, onFailure: () -> Unit) {

    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)

    //val inputActivationCode = remember { mutableStateOf("617294811674") }
    val inputActivationCode = remember { mutableStateOf("933555612584") }
    val colorBorder = remember { mutableStateOf(backTextFiledLight) }
    val errorText = remember { mutableStateOf<String?>(null) }
    val colorTextError = remember { mutableStateOf<Color>(Color.Red) }
    val isRestoreMode = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        Spacer(modifier = Modifier.height(20.dp))
        LogoItem(channelManager.channelSelected.value!!)
        Spacer(modifier = Modifier.height(10.dp))

        TextFieldItem(
            textValue = inputActivationCode.value,
            colorBorder = colorBorder.value,
            leadingIcon = R.drawable.ic_activation_check,
            hint = if (isRestoreMode.value) "Click in button restore." else "Insert activation code.",
            errorMessage = errorText.value,
            colorTextError = colorTextError.value
        ) {
            inputActivationCode.value = it
        }


        Spacer(modifier = Modifier.height(10.dp))
        ButtonItem(label = if (isRestoreMode.value) "Restore" else "Activer") {
            if (isRestoreMode.value) {
                channelManager.restoreAccount(
                    viewModel.bindingModel.addressMac, viewModel.bindingModel.serialNumber, channelManager.channelSelected.value!!.url,
                    onSuccess = {
                        errorText.value = null
                        errorText.value = "Your code is $it"
                        colorTextError.value = Color.Green
                        colorBorder.value = backTextFiledLight
                        isRestoreMode.value = !isRestoreMode.value
                        inputActivationCode.value = it
                    },
                    onFailure = {
                        errorText.value = null
                        errorText.value = "Error Restore!"
                        colorBorder.value = Color.Red
                        colorTextError.value = Color.Red
                    },
                )


            } else {

                val channelLogin = LoginModel(
                    code = inputActivationCode.value,
                    mac = viewModel.bindingModel.addressMac,
                    model = viewModel.bindingModel.modelDevice,
                    sn = viewModel.bindingModel.serialNumber,
                )



                channelManager.newLoginToChannel(
                    channelLogin, channelManager.channelSelected.value!!.url,
                    onSuccess = {
                        colorBorder.value = backTextFiledLight
                        errorText.value = null
                        PreferencesHelper.saveLoginStatus(app, true, inputActivationCode.value)
                        onSuccess()

                    },
                    onFailure = {
                        errorText.value = "Code invalide"
                        colorBorder.value = Color.Red
                        colorTextError.value = Color.Red
                        onFailure()

                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        RestoreItem(
            label = if (isRestoreMode.value) "Activer" else "Restore",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
                .height(40.dp),
            onClick = {
                isRestoreMode.value = !isRestoreMode.value
            },
        )


    }


}



