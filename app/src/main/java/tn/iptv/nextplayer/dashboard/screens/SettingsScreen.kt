package tn.iptv.nextplayer.dashboard.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem
import tn.iptv.nextplayer.dashboard.util.Page

@Composable
fun SettingsScreen(viewModel: DashBoardViewModel, ) {
    BackHandler(
        onBack = {
            viewModel.bindingModel.selectedNavigationItem.value = NavigationItem.Home
            viewModel.bindingModel.selectedPage = Page.HOME
        },
    )
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Grid Size", "Configuration Commande", "Compte" ,"A Propos")

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> GridSizeScreen()
            1 -> ConfigurationCommandeScreen()
            2 -> AboutScreen()
            3 -> Account()
        }
    }
}

@Composable
fun GridSizeScreen() {
    var movies by remember { mutableStateOf("") }
    var series by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Select Grid Size", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = movies,
            onValueChange = { movies = it },
            label = { Text("Movies") },
            modifier = Modifier.width(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = series,
            onValueChange = { series = it },
            label = { Text("Series") },
            modifier = Modifier.width(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Save action */ }) {
            Text(text = "Save")
        }
    }
}
@Composable
fun ConfigurationCommandeScreen() {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Configuration Commande", fontSize = 20.sp)
    }
}

@Composable
fun AboutScreen() {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "A Propos", fontSize = 20.sp)
    }
}
@Composable
fun Account() {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Compte", fontSize = 20.sp)
    }
}
