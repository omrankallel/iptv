package tn.iptv.nextplayer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tn.iptv.nextplayer.listchannels.ui.theme.backButton
import tn.iptv.nextplayer.listchannels.ui.theme.colorTextButton


@Composable
fun ButtonItem(
    enabled: Boolean = true,
    label: String = "Next",
    sizeText: TextUnit = 18.sp,
    shape: Dp = 10.dp,
    paddingHorizontal: Dp = 100.dp,
    modifier: Modifier = Modifier
        .padding(horizontal = paddingHorizontal)
        .height(50.dp),
    modifierText: Modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.Transparent),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = backButton,
    ),
    onClick: () -> Unit,
) {


    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(shape),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
        ),
        colors = colors,
    ) {
        Text(
            text = label,
            modifier = modifierText,
            textAlign = TextAlign.Center,
            color = colorTextButton,
            fontSize = sizeText,
            fontWeight = FontWeight.W600,
        )
    }

}




