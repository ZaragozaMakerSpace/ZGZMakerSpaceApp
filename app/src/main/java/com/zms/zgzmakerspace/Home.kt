package com.zms.zgzmakerspace

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.zms.zgzmakerspace.ui.theme.ZMSCloseColor
import com.zms.zgzmakerspace.ui.theme.ZMSOpenColor
import com.zms.zgzmakerspace.viewmodel.MqttViewModel
import kotlinx.coroutines.launch


@Composable
fun Home(paddingValues: PaddingValues) {
    val context = LocalContext.current
    val viewModel: MqttViewModel = viewModel()
    val makerspaceIsOpen = viewModel.makerspaceIsOpen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        Row {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable { navigateToActivity(context, ZMS_Activity) }
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.maker_space_logo),
                    contentDescription = "ZMS Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Row {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "El makerspace de Zaragoza",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Row {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "El makerspace esta ${if (makerspaceIsOpen.value) "abierto" else "cerrado"}",
                    color = if (makerspaceIsOpen.value) ZMSOpenColor else ZMSCloseColor,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        MapViewContainer()
        Box(
            contentAlignment = Alignment.BottomCenter,

            ) {
            Icons(icon_Ids.zip(icon_URLs).toMap())

        }
    }
}

@Composable
fun Icons(iconIDs: Map<Int, String>) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        iconIDs.forEach { (imageId, url) ->
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clickable {
                    scope.launch {
                        openUrl(context, url)
                    }
                }
            ) {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = "$imageId",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun MapViewContainer() {
    val context = LocalContext.current
    var apiKey = getMapsApiKey(context)

    if (!apiKey.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            GoogleMapComposable()
        }
    } else {
        /* Text(
            "API Key no disponible o incorrecta, mapa no cargado.",
            modifier = Modifier.fillMaxSize()
        )
         */
    }
}

@Composable
fun GoogleMapComposable() {
    val zmsMarker = LatLng(41.65003, -0.89227)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(zmsMarker, 19f)
    }
    val mapSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = mapSettings
    ) {
        Marker(
            state = MarkerState(position = zmsMarker),
            title = "ZGZMakerSpace",
            snippet = "Hazte socio",
        )
    }
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

fun navigateToActivity(context: Context, destinationActivity: Class<out ComponentActivity>) {
    val intent = Intent(context, destinationActivity)
    context.startActivity(intent)
}

fun getMapsApiKey(context: Context): String? {
    return try {
        val applicationInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        return applicationInfo.metaData?.getString("com.google.android.geo.API_KEY")
    } catch (e: PackageManager.NameNotFoundException) {

        null
    } catch (e: NullPointerException) {

        null
    }
}

@Preview
@Preview(device = "id:pixel_4")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(device = "spec:width=1080px,height=2340px,dpi=480", name = "El pequeño")
@Composable
fun PreviewHome() {
    Home(paddingValues = PaddingValues(20.dp))
}