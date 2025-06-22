package com.teedee.firetv

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    }
}
