package id.derysudrajat.geminitest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.ai.client.generativeai.GenerativeModel
import id.derysudrajat.geminitest.ui.theme.GeminishowcaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeminishowcaseTheme {
                val generativeMultiModal = GenerativeModel(
                    modelName = "gemini-pro-vision",
                    apiKey = BuildConfig.apiKey
                )
                val generativeText = GenerativeModel(
                    modelName = "gemini-pro",
                    apiKey = BuildConfig.apiKey
                )
                val viewModel = SummarizeViewModel(generativeMultiModal, generativeText)
                SummarizeRoute(viewModel)
            }
        }
    }

    @Composable
    internal fun SummarizeRoute(
        summarizeViewModel: SummarizeViewModel = viewModel()
    ) {
        val summarizeUiState by summarizeViewModel.uiState.collectAsState()
        SummarizeScreen(
            lifecycleScope,
            summarizeUiState, onSummarizeClicked = { inputText, listUri ->
            summarizeViewModel.summarize(inputText, listUri)
        })
    }
}


