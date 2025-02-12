package tn.iptv.nextplayer.dashboard.screens.comingSoon


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coloredShadow
import tn.iptv.nextplayer.domain.models.series.MediaType
import tn.iptv.nextplayer.listchannels.ui.theme.backCardMovie


@Composable
fun ItemShowAll(mediaType: MediaType, onClickToShowAll: () -> Unit) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(210.dp)
            .height(if (mediaType == MediaType.LIVE_TV) 220.dp else 260.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backCardMovie)
            .border(
                width = 2.dp,
                color = if (isFocused) Color(0xFFB4A1FB) else Color.Gray,
                shape = RoundedCornerShape(8.dp),
            )
            .shadow(1.dp)
            .padding(2.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .clickable {
                onClickToShowAll()
            },
        contentAlignment = Alignment.Center,
    )

    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GridIcon(
                modifier = Modifier.size(48.dp).coloredShadow(
                    color = if (isFocused) Color(0xFFB4A1FB) else Color.Gray,
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Afficher tout",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }


}

@Composable
fun GridIcon(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        repeat(2) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(),
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF9C89E0)),
                    )
                }
            }
        }
    }
}