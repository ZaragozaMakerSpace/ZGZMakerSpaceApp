package com.example.zgzmakerspace

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zgzmakerspace.core.Const


@Composable
fun Home(context: Context?) {
    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.mipmap.maker_space_logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Zaragoza Maker Space",
            fontSize = 26.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),

            )

        Spacer(modifier = Modifier.size(40.dp))

        Text(text = "Un MakerSpace formado por personas que hacen cosas que hacen cosas.",
            modifier = Modifier.padding(20.dp))


        Row(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.mipmap.instagram),
                contentDescription = "instagram",
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp)
                    .align(Alignment.Bottom)
            )
            Image(
                painter = painterResource(id = R.mipmap.patreon),
                contentDescription = "instagram",
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp)
                    .align(Alignment.Bottom)
                    .clickable {
                        var intent = Intent(Intent.ACTION_VIEW, Uri.parse(Const.PATREON))
                        context?.startActivity(intent)
                    }
            )
            Image(
                painter = painterResource(id = R.mipmap.facebook),
                contentDescription = "instagram",
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp)
                    .align(Alignment.Bottom)
            )
            Image(
                painter = painterResource(id = R.mipmap.x),
                contentDescription = "instagram",
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp)
                    .align(Alignment.Bottom)
            )
        }

    }
}

@Preview
@Preview(device = "id:pixel_4")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(device = "spec:width=1080px,height=2340px,dpi=480",name="El pequeño")
@Composable
fun PreviewHome(){
    Home(context = null)
}
