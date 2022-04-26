package com.tarasovms.application.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.palette.BitmapPalette
import com.tarasovms.application.R
import com.tarasovms.application.ui.theme.shimmerHighLight

@Composable
fun ImageNetworkCoil(
    url: String,
    modifier: Modifier = Modifier,
    circularRevealEnabled: Boolean = false,
    contentScale: ContentScale = ContentScale.Crop,
    bitmapPalette: BitmapPalette? = null
) {
    CoilImage(
        imageModel = url,
        modifier = modifier,
        contentScale = contentScale,
        circularReveal = CircularReveal(duration = 300).takeIf { circularRevealEnabled },
        bitmapPalette = bitmapPalette,
        previewPlaceholder = R.drawable.poster,
        shimmerParams = ShimmerParams(
            baseColor = MaterialTheme.colors.background,
            highlightColor = shimmerHighLight,
            dropOff = 0.65f
        ),
        failure = {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "image request failed.",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    )
}
