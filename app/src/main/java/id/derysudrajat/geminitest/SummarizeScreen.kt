package id.derysudrajat.geminitest

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.LifecycleCoroutineScope
import dev.jeziellago.compose.markdowntext.MarkdownText
import id.derysudrajat.geminitest.ui.component.ImageGif
import id.derysudrajat.geminitest.ui.component.LoadingComponent
import id.derysudrajat.geminitest.ui.component.PromptChat
import id.derysudrajat.geminitest.ui.component.PromptInput

@Composable
fun SummarizeScreen(
    lifecycleCoroutineScope: LifecycleCoroutineScope,
    uiState: SummarizeUiState = SummarizeUiState.Initial,
    onSummarizeClicked: (String, List<Bitmap>) -> Unit = { _, _ -> }
) {
    var submittedPrompt by remember { mutableStateOf(Pair("", emptyList<Bitmap>())) }
    Scaffold {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(it)
        ) {
            val (inputRef, resultRef) = createRefs()
            Column(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .verticalScroll(rememberScrollState())
                    .constrainAs(resultRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(inputRef.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            ) {
                if (submittedPrompt.first.isNotBlank()) PromptChat(
                    prompt = submittedPrompt.first,
                    images = submittedPrompt.second,
                    modifier = Modifier.align(Alignment.End)
                )
                when (uiState) {
                    is SummarizeUiState.Initial -> {
                        // Nothing is shown
                    }

                    is SummarizeUiState.Success, SummarizeUiState.Loading -> {
                        Column(modifier = Modifier.padding(all = 8.dp)) {
                            if (uiState is SummarizeUiState.Loading) ImageGif(
                                imageRes = R.drawable.anim_gemini,
                                modifier = Modifier.size(24.dp)
                            )
                            else Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.ic_gemini),
                                contentDescription = "Icon Gemini"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            when (uiState) {
                                is SummarizeUiState.Loading -> LoadingComponent()

                                is SummarizeUiState.Success -> MarkdownText(
                                    modifier = Modifier.fillMaxWidth(),
                                    markdown = uiState.outputText,
                                    style = TextStyle(color = MaterialTheme.colorScheme.onSurface)
                                )

                                else -> {}
                            }
                        }
                    }

                    is SummarizeUiState.Error -> {
                        Text(
                            text = uiState.errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(all = 8.dp)
                        )
                    }
                }

            }
            PromptInput(
                lifecycleCoroutineScope = lifecycleCoroutineScope,
                modifier = Modifier.constrainAs(inputRef) {
                    bottom.linkTo(parent.bottom, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 8.dp)
                    width = Dimension.fillToConstraints
                },
                onSendPrompt = { prompt, listImage ->
                    submittedPrompt = Pair(prompt, listImage)
                    onSummarizeClicked(prompt, listImage)
                }
            )
        }
    }
}