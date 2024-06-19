package com.zms.zgzmakerspace

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zms.zgzmakerspace.core.URLImage
import com.zms.zgzmakerspace.ui.theme.ZGZMakerSpaceTheme

val imagesList = listOf(
    URLImage(R.mipmap.print_zms, "https://learning.zaragozamakerspace.com/enrol/index.php?id=2"),
    URLImage(R.mipmap.laser_zms, "https://learning.zaragozamakerspace.com/enrol/index.php?id=4"),
    URLImage(R.mipmap.arduino_zms, "https://zaragozamakerspace.com/courses/arduino-basico/"),
    URLImage(R.mipmap.esp_zms, "https://learning.zaragozamakerspace.com/enrol/index.php?id=12"),
    URLImage(R.mipmap.android_zms, "https://github.com/ZaragozaMakerSpace/ZGZMakerSpaceApp"),
    URLImage(R.mipmap.cnc_zms, "https://zaragozamakerspace.com/kicad2cnc/"),
    URLImage(R.mipmap.raspberry_zms, "https://zaragozamakerspace.com/orange-pi-zero-jugar/"),
    URLImage(R.mipmap.git_zms, "https://github.com/ZaragozaMakerSpace"),
    URLImage(
        R.mipmap.linux_zms,
        "https://zaragozamakerspace.com/makers-y-la-filosofia-del-codigo-libre/"
    ),
)

class ZMSActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZGZMakerSpaceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ZMSCategories()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun ZMSCategories() {
    Scaffold(
        bottomBar = {
            /*BottomBar(screens, selectedScreen, onSelected = {
                selectedScreen = it

            }, viewModel)
            */
        },
        content = {
            ImageGrid(2)
        }
    )
}

@Composable
fun ImageGrid(columns: Int) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(imagesList) { imageItem ->
            ImageGridItem(imageItem = imageItem)
        }
    }
}

@Composable
fun ImageGridItem(imageItem: URLImage) {
    val context = LocalContext.current
    val image: Painter = painterResource(id = imageItem.imageResId)

    Image(
        painter = image,
        contentDescription = "Grid Image",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imageItem.url))
                context.startActivity(intent)
            }
    )
}