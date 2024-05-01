package com.example.zgzmakerspace

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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zgzmakerspace.core.URLImage
import com.example.zgzmakerspace.ui.theme.ZGZMakerSpaceTheme

val imagesList = listOf(
    URLImage(R.mipmap.maker_space_logo, "https://zaragozamakerspace.com"),
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

@Preview(showBackground = true)
@Composable
fun ZMSCategories() {
    ImageGrid(2)
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