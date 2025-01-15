package tn.iptv.nextplayer.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tn.iptv.nextplayer.R

@Composable
fun SortByRow(
    isAscending: Boolean,
    modifier: Modifier = Modifier,
    onSortClick: () -> Unit,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            text = "Trier Par",
            modifier = Modifier.padding(bottom = 4.dp),
        )

        Spacer(modifier = Modifier.width(10.dp))


        IconButton(
            onClick = {
                onSortClick()
            },
        ) {
            Icon(
                modifier = modifier.padding(8.dp),
                painter = painterResource(
                    id = if (isAscending) R.drawable.sort_ascending else R.drawable.sort_descending,
                ),
                contentDescription = "Trier par",
            )
        }
    }
}