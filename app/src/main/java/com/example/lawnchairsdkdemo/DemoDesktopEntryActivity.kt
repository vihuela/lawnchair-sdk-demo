package com.example.lawnchairsdkdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class DemoDesktopEntryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val entryTitle = intent.getStringExtra(EXTRA_ENTRY_TITLE)
            ?: getString(R.string.demo_desktop_entry_1_title)
        val entryMessage = intent.getStringExtra(EXTRA_ENTRY_MESSAGE)
            ?: getString(R.string.demo_desktop_entry_1_message)
        setContent {
            MaterialTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFF6F3FF),
                                    Color(0xFFE8E0FF),
                                ),
                            ),
                        ),
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = entryTitle,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C1B54),
                        )
                        Text(
                            text = entryMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF54427A),
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val EXTRA_ENTRY_TITLE = "entry_title"
        private const val EXTRA_ENTRY_MESSAGE = "entry_message"

        fun createIntent(
            context: Context,
            entryTitle: String,
            entryMessage: String,
        ): Intent {
            return Intent(context, DemoDesktopEntryActivity::class.java).apply {
                putExtra(EXTRA_ENTRY_TITLE, entryTitle)
                putExtra(EXTRA_ENTRY_MESSAGE, entryMessage)
            }
        }
    }
}
