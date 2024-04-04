package id.derysudrajat.geminitest.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun ImageChat(image: Bitmap, onClose: (Bitmap) -> Unit) {
    Box {
        Image(
            bitmap = image.asImageBitmap(),
            modifier = Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .size(68.dp)
                .aspectRatio((image.width.toFloat() / image.height.toFloat())),
            contentDescription = "",
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp),
            colors = IconButtonDefaults.filledIconButtonColors(),
            onClick = { onClose.invoke(image) }
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = ""
            )
        }
    }
}