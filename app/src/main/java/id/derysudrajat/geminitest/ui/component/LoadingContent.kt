package id.derysudrajat.geminitest.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingComponent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.shimmer(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RoundedSkeleton(
            width = 150.dp, height = 20.dp, radius = 10.dp
        )
        RoundedSkeleton(
            width = 250.dp, height = 20.dp, radius = 10.dp
        )
        RoundedSkeleton(
            width = 100.dp, height = 20.dp, radius = 10.dp
        )
    }
}