package tn.iptv.nextplayer.login.layout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.listchannels.ui.theme.colorHint


@Composable
fun TextFieldItem(
    textValue: String,
    hint: String = "this is the default hint",
    leadingIcon: Int = R.drawable.ic_activation_check,
    textSize: TextUnit = 17.sp,
    textAlign: TextAlign = TextAlign.Start,
    colorTextError: Color = Color.Red,
    colorBorder: Color,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .scale(scaleY = 0.9F, scaleX = 1F)
        .padding(horizontal = 100.dp),
    isReadOnly: Boolean = false,
    customKeyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
    errorMessage: String? = null,
    updateValue: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        enabled = !isReadOnly,
        readOnly = isReadOnly,
        value = textValue,
        onValueChange = {
            updateValue(it)
        },

        singleLine = true,
        keyboardOptions = customKeyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() },
        ),
        modifier = modifier.border(
            width = 1.dp, // Thickness of the border
            color = colorBorder, // Border color
            shape = RoundedCornerShape(10.dp), // Rounded corner shape
        ),

        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = "Search",
                tint = colorBorder,
                modifier = Modifier.size(17.dp),
            )

        },
        placeholder = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = hint,
                color = colorHint,
                textAlign = textAlign,
                fontWeight = FontWeight.W400,
                fontSize = textSize,
            )
        },
        textStyle = LocalTextStyle.current.copy(
            textAlign = textAlign,
            color = Color.White,
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        ),
        //shape = RoundedCornerShape(10.dp)
    )
    if (!errorMessage.isNullOrEmpty()) {
        Text(
            text = errorMessage,
            color = colorTextError,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp), // Aligns with text field padding
        )
    }

}