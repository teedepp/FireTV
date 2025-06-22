package com.teedee.firetv

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class UserSelectActivity : ComponentActivity() {

    private val streamApiKey = "n6j2nbkgkxt2" // 🔑 Replace this with your actual API key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                UserSelectionScreen { userId, otherUserId ->
                    val intent = ChannelActivity.getIntent(
                        context = this,
                        channelId = "party-room",
                        userId = userId,
                        otherUserId = otherUserId,
                        callId = "party-room",
                        apiKey = streamApiKey
                    )
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun UserSelectionScreen(onUserSelected: (String, String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Join Watch Party as:", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { onUserSelected("user1", "user2") }) {
            Text("Join as user1")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onUserSelected("user2", "user1") }) {
            Text("Join as user2")
        }
    }
}
