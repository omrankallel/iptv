package tn.iptv.nextplayer.login.layout


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tn.iptv.nextplayer.listchannels.ui.theme.backButton

@Composable
fun RestoreItem(
    label: String = "Restore",
    sizeText: TextUnit = 18.sp,
    paddingHorizontal: Dp = 100.dp,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = paddingHorizontal)
        .height(30.dp),
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
    ) {
        Text(
            text = label,
            style = TextStyle(
                color = backButton,
                fontSize = sizeText,
                textDecoration = TextDecoration.Underline,
            ),
            fontWeight = FontWeight.W600,
            modifier = Modifier
                .padding(end = 10.dp)
                .clickable { onClick() },
        )
    }
}