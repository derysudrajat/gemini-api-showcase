package id.derysudrajat.geminitest.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun PromptChat(
    prompt: String,
    images: List<Bitmap>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.widthIn(150.dp, 300.dp),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp,
            topEnd = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = prompt)
            if (images.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    images.forEach { bitmap ->
                        Image(
                            modifier = Modifier
                                .height(150.dp)
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp)),
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}