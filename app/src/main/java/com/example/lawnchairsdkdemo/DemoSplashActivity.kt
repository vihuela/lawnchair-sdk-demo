package com.example.lawnchairsdkdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.lawnchair.sdk.LawnchairOperations
import kotlin.math.roundToInt

class DemoSplashActivity : ComponentActivity() {
    private var waitForHomeSettingsResult = false
    private var hasPausedForHomeSettings = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DemoSplashScreen(
                    onRequestSetDefaultLauncher = {
                        waitForHomeSettingsResult = true
                        LawnchairOperations.requestSetDefaultLauncher(this)
                    },
                    onLaunchDesktop = ::launchDesktopAndFinish,
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (waitForHomeSettingsResult) {
            hasPausedForHomeSettings = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (waitForHomeSettingsResult && hasPausedForHomeSettings) {
            launchDesktopAndFinish()
        }
    }

    private fun launchDesktopAndFinish() {
        LawnchairOperations.launchDesktop(this)
        finish()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DemoSplashScreen(
    onRequestSetDefaultLauncher: () -> Unit,
    onLaunchDesktop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val progress = remember { Animatable(0f) }
    var showGuideDialog by remember { mutableStateOf(false) }
    var hasHandledCompletion by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500, easing = LinearEasing),
        )

        if (!hasHandledCompletion) {
            hasHandledCompletion = true
            if (LawnchairOperations.isDefaultLauncher(context)) {
                onLaunchDesktop()
            } else {
                showGuideDialog = true
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF7F0FF), Color(0xFFE7D8FF)),
                ),
            ),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = com.android.launcher3.R.drawable.ic_launcher_home_comp),
                contentDescription = null,
                modifier = Modifier.size(96.dp),
                contentScale = ContentScale.Fit,
            )
            Text(
                text = stringResource(id = R.string.derived_app_name),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2E1065),
                modifier = Modifier.padding(top = 20.dp),
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LinearProgressIndicator(
                progress = { progress.value },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF7C3AED),
                trackColor = Color.White.copy(alpha = 0.75f),
                gapSize = 0.dp,
                drawStopIndicator = {},
            )
            Text(
                text = "${(progress.value * 100).roundToInt().coerceIn(0, 100)}%",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5B21B6),
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }

    if (showGuideDialog) {
        DefaultLauncherGuideDialog(
            onSetLauncher = onRequestSetDefaultLauncher,
            onSkip = onLaunchDesktop,
        )
    }
}
