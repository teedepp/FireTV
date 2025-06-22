package com.teedee.firetv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User

class ChannelActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelId = intent.getStringExtra(KEY_CHANNEL_ID) ?: run {
            finish()
            return
        }

        val userId = intent.getStringExtra("userId") ?: "user1"
        val otherUserId = intent.getStringExtra("otherUserId") ?: "user2"
        val callId = intent.getStringExtra("callId") ?: "default-call"
        val apiKey = intent.getStringExtra("apiKey") ?: ""

        val chatClient = ChatClient.instance()

        // Connect chat user with dev token
        chatClient.connectUser(
            user = io.getstream.chat.android.models.User(id = userId),
            token = chatClient.devToken(userId)
        ).enqueue { result ->
            if (result.isSuccess) {
                val fullChannelId = "messaging:$channelId"

                // Create or join the chat channel
                val channelClient = chatClient.channel("messaging", channelId)
                channelClient.create(
                    memberIds = listOf(userId, otherUserId),
                    extraData = mapOf("name" to "FireTV Party")
                ).enqueue {
                    // Launch UI
                    launchUI(userId, apiKey, callId, fullChannelId)
                }
            } else {
                finish() // Exit if failed
            }
        }
    }

    private fun launchUI(userId: String, apiKey: String, callId: String, channelId: String) {
        setContent {
            Row(modifier = Modifier.fillMaxSize()) {

                // 🔹 Video column
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    val user = User(
                        id = userId,
                        name = "FireTV User $userId",
                        image = "https://bit.ly/2TIt8NR"
                    )

                    val videoClient = StreamVideoBuilder(
                        context = applicationContext,
                        apiKey = apiKey,
                        geo = GEO.GlobalEdgeNetwork,
                        user = user,
                        token = ChatClient.instance().devToken(userId) // 🔐 Dev token
                    ).build()

                    VideoCallContent(client = videoClient, callId = callId)
                }

                // 🔹 Chat column
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    ChatTheme(
                        shapes = StreamShapes.defaultShapes().copy(
                            avatar = RoundedCornerShape(8.dp),
                            attachment = RoundedCornerShape(12.dp),
                            myMessageBubble = RoundedCornerShape(16.dp),
                            otherMessageBubble = RoundedCornerShape(16.dp)
                        )
                    ) {
                        MessagesScreen(
                            viewModelFactory = MessagesViewModelFactory(
                                context = this@ChannelActivity,
                                channelId = channelId,
                                messageLimit = 30
                            ),
                            onBackPressed = { finish() }
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val KEY_CHANNEL_ID = "channelId"

        fun getIntent(
            context: Context,
            channelId: String,
            userId: String,
            otherUserId: String,
            callId: String,
            apiKey: String
        ): Intent {
            return Intent(context, ChannelActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
                putExtra("userId", userId)
                putExtra("otherUserId", otherUserId)
                putExtra("callId", callId)
                putExtra("apiKey", apiKey)
            }
        }
    }
}
