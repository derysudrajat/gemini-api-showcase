package id.derysudrajat.geminitest

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.launch

object BitmapUtils {

    fun convertUriToBitmap(uris: List<Uri>, context: Context, scope: LifecycleCoroutineScope): List<Bitmap> {
        val listBitmap = mutableListOf<Bitmap>()
        scope.launch {
            uris.forEach { uri ->
                uri.uriToBitmap(context)?.let { bitmap ->
                    listBitmap.add(bitmap)
                }
            }
        }
        return listBitmap.toList()
    }

    private val Bitmap.ratioBitmap: Pair<Int, Int> get() {
        var width: Int = this.getWidth()
        var height: Int = this.getHeight()
        val defaultSize = 200

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = defaultSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = defaultSize
            width = (height * bitmapRatio).toInt()
        }
        return Pair(width, height)
    }

    private fun Uri.uriToBitmap(context: Context): Bitmap? {
        // Obtain the content resolver from the context
        val contentResolver: ContentResolver = context.contentResolver

        // Check the API level to use the appropriate method for decoding the Bitmap
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // For Android P (API level 28) and higher, use ImageDecoder to decode the Bitmap
            val source = ImageDecoder.createSource(contentResolver, this)
            val original = ImageDecoder.decodeBitmap(source)
            val ratio = original.ratioBitmap
            Bitmap.createScaledBitmap(original, ratio.first, ratio.second, false)
        } else {
            // For versions prior to Android P, use BitmapFactory to decode the Bitmap
            val bitmap = context.contentResolver.openInputStream(this)?.use { stream ->
                val original  = BitmapFactory.decodeStream(stream)
                val ratio = original.ratioBitmap
                Bitmap.createScaledBitmap(original, ratio.first, ratio.second, false)
            }
            bitmap
        }
    }
}