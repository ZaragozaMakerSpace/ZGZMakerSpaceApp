package com.example.zgzmakerspace


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.zgzmakerspace.model.Events
import com.example.zgzmakerspace.ui.theme.ZGZMakerSpaceTheme
import com.example.zgzmakerspace.viewmodel.EventsViewModel

val icon_Ids = listOf(
    R.drawable.instagram_icon,
    R.drawable.patreon_icon,
    R.drawable.x_icon,
    R.drawable.facebook_icon
)
val icon_URLs = listOf(
    "https://www.instagram.com/zgzmakerspace",
    "https://www.patreon.com/ZGZMakerSpace",
    "https://twitter.com/ZGZMakerSpace",
    "https://www.facebook.com/zgzmakerspace/"
)
val ZMS_Activity = ZMSActivity::class.java

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: EventsViewModel by viewModels()
        val screens = listOf("Home", "CodeBeers", "Talleres")
        setContent {

            var selectedScreen by rememberSaveable { mutableStateOf(screens.first()) }
            ZGZMakerSpaceTheme {
                // A surface container using the 'background' color from the theme
                val loading = viewModel.loading.collectAsState()
                val list = viewModel.listEvent.collectAsState()

                Scaffold(
                    bottomBar = {
                        BottomBar(screens, selectedScreen, onSelected = {
                            selectedScreen = it

                        }, viewModel)

                    },
                    content = {
                        val context = LocalContext.current
                        if (selectedScreen == "Home") {
                            Box(modifier = Modifier.padding(10.dp)) {
                                Home(it)
                            }
                        } else {
                            ListEvents(
                                events = list.value,
                                loading.value,
                                paddingValues = it,
                                context
                            )
                            if (loading.value) {
                                DialogLoading(show = loading.value)
                            }
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun ListEvents(
    events: List<Events>,
    loading: Boolean,
    paddingValues: PaddingValues,
    context: Context
) {
    if (events.isEmpty() && !loading) {
        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No hay nada!!", fontSize = 20.sp, color = Color.Red)
        }

    } else {
        LazyColumn(Modifier.padding(paddingValues = paddingValues)) {
            items(events) {
                ItemEvent(event = it, context)
            }

        }
    }

}

@Composable
fun BottomBar(
    screens: List<String>,
    now: String,
    onSelected: (String) -> Unit,
    viewModel: EventsViewModel
) {
    NavigationBar {
        NavigationBarItem(
            selected = now == screens[0],
            label = { Text(text = screens[0]) },
            onClick = {
                onSelected("Home")
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") })

        NavigationBarItem(
            selected = now == screens[1],
            label = { Text(text = screens[1]) },
            onClick = {
                viewModel.readWeb()
                onSelected("CodeBeers")
            },
            icon = { Icon(Icons.Filled.Build, contentDescription = "CodeBeers") })

        NavigationBarItem(
            selected = now == screens[2],
            label = { Text(text = screens[2]) },
            onClick = {
                viewModel.readWebLab()
                onSelected("Talleres")
            },
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "Talleres") })
    }


}

@Composable
fun ItemEvent(event: Events, context: Context) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(10.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = event.image,
                contentDescription = "CodeBeers",
                modifier = Modifier
                    .padding(8.dp)
                    .clip(
                        CircleShape
                    )
                    .size(100.dp),

                )
            //Icon(Icons.Rounded.AccountCircle, contentDescription = null, Modifier.size(50.dp))
            Column(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth()) {
                    Text(text = event.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Row(Modifier.fillMaxWidth()) {
                    Text(text = event.date)
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = event.weekDay)
                    Spacer(modifier = Modifier.size(10.dp))

                }
                Row(Modifier.fillMaxWidth()) {
                    Text(text = event.timeStart, color = Color.Blue)
                    Text(text = " - ", color = Color.Blue)
                    Text(text = event.timeEnd, color = Color.Blue)
                }

            }
        }
    }


}

@Composable
fun DialogLoading(show: Boolean) {

    if (show) {
        Dialog(
            onDismissRequest = { /*TODO*/ },
            properties = DialogProperties(dismissOnBackPress = true)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        //.background(color = Color.White)
                        .fillMaxWidth(), Arrangement.Center
                ) {
                    Image(
                        painterResource(id = R.mipmap.maker_space_logo),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth(),
                    //.background(color = Color.White),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator()
                }
            }


        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZGZMakerSpaceTheme {
        DialogLoading(true)
    }
}