package id.derysudrajat.geminitest

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SummarizeViewModel(
    private val generativeMultiModalModel: GenerativeModel,
    private val generativeTextModel: GenerativeModel,
) : ViewModel() {

    private val _uiState: MutableStateFlow<SummarizeUiState> =
        MutableStateFlow(SummarizeUiState.Initial)
    val uiState: StateFlow<SummarizeUiState> =
        _uiState.asStateFlow()

    fun summarize(inputText: String, images: List<Bitmap>) {
        _uiState.value = SummarizeUiState.Loading

        val prompt = content {
            if (images.isNotEmpty()) {
                images.forEach { bitmap -> image(bitmap) }
            }
            text(inputText)
        }

        viewModelScope.launch {
            try {
                var fulltext = ""
                (if (images.isNotEmpty()) generativeMultiModalModel else generativeTextModel).let { model ->
                    model.generateContentStream(prompt).collect { chuck ->
                        fulltext+= chuck.text?:""
                        _uiState.value = SummarizeUiState.Success(fulltext)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = SummarizeUiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}