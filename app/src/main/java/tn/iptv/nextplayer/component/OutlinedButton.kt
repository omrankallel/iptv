package tn.iptv.nextplayer.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tn.iptv.nextplayer.listchannels.ui.theme.backButton
import tn.iptv.nextplayer.listchannels.ui.theme.transparent


@Composable
fun OutlinedButtonIPTV(
    labelButton: String, onClick: () -> Unit,
    modifier: Modifier = Modifier
        .height(40.dp)
        .padding(horizontal = 10.dp),
    modifierText: Modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.Transparent),
) {


    OutlinedButton(
        modifier = modifier,
        onClick = {
            onClick()
        },
        border = BorderStroke(1.dp, backButton),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colors.primary,
            backgroundColor = transparent,
        ),
        shape = RoundedCornerShape(5.dp),
    ) {
        Text(
            text = labelButton,
            modifier = modifierText,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.W600,
        )
    }

}