package id.derysudrajat.geminitest.ui.component

import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import id.derysudrajat.geminitest.BitmapUtils
import id.derysudrajat.geminitest.R

@Composable
fun PromptInput(
    lifecycleCoroutineScope: LifecycleCoroutineScope,
    modifier: Modifier = Modifier,
    onSendPrompt: (String, List<Bitmap>) -> Unit
) {
    var prompt by remember { mutableStateOf("") }
    var listImage by remember { mutableStateOf(emptyList<Bitmap>()) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(2)
    ) { uris ->
        Log.d("PhotoPicker", "PromptInput: result = $uris")
        if (uris.isNotEmpty()) listImage = BitmapUtils.convertUriToBitmap(
            uris, context, lifecycleCoroutineScope
        ) else Log.d("PhotoPicker", "No media selected")
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.weight(8f),
            shape = RoundedCornerShape(if (listImage.isNotEmpty()) 8.dp else 32.dp)
        ) {
            Column {
                TextField(
                    leadingIcon = if (listImage.size < 2) {
                        {
                            IconButton(onClick = {
                                pickMedia.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_add_photo),
                                    contentDescription = "Add"
                                )
                            }
                        }
                    } else null,
                    value = prompt,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                    ),
                    placeholder = { Text("Ask Anything!") },
                    onValueChange = { text ->
                        prompt = text
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(32.dp))
                )
                if (listImage.isNotEmpty()) LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    items(listImage) { image ->
                        ImageChat(image = image) { removedBitmap ->
                            listImage = listImage.toMutableList().apply {
                                remove(removedBitmap)
                            }
                        }
                    }
                }
            }
        }
        IconButton(
            enabled = prompt.isNotBlank(),
            onClick = {
                onSendPrompt(prompt, listImage)
                listImage = listOf()
                prompt = ""
            }
        ) {
            Icon(imageVector = Icons.Rounded.Send, contentDescription = "Send")
        }
    }
}