package com.teedee.firetv

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

@Composable
fun WatchPartyScreen(partyKey: String, userId: String, otherUserId: String) {
    val context = LocalContext.current
    val chatClient = ChatClient.instance()
    val cid = "messaging:${partyKey.lowercase()}"
    var channelCreated by remember { mutableStateOf(false) }

    LaunchedEffect(partyKey, userId, otherUserId) {
        chatClient.createChannel(
            channelType = "messaging",
            channelId = partyKey.lowercase(),
            memberIds = listOf(userId, otherUserId),
            extraData = mapOf("name" to "Party $partyKey")
        ).enqueue { result ->
            channelCreated = result.isSuccess
        }
    }

    ChatTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            if (channelCreated) {
                MessagesScreen(
                    viewModelFactory = MessagesViewModelFactory(
                        context = context,
                        channelId = cid
                    ),
                    onBackPressed = {}
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // ✅ Floating video call button
            IconButton(
                onClick = {
                    context.startActivity(
                        VideoActivity.getIntent(
                            context = context,
                            userId = userId,
                            token = ChatClient.instance().devToken(userId),
                            callId = partyKey,
                            apiKey = "n6j2nbkgkxt2"
                        )
                    )
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = "Video Call",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
