package com.teedee.firetv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.teedee.firetv.ui.theme.FireTVTheme
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User as StreamUser

class UserSelectActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove the ChatInitializer.init() call - ChatClient is already initialized in FireTVApp

        setContent {
            FireTVTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    UserSelectionScreen { userId, otherUserId ->
                        connectAndNavigateToParty(userId, otherUserId)
                    }
                }
            }
        }
    }

    private fun connectAndNavigateToParty(userId: String, otherUserId: String) {
        // Get the already initialized ChatClient instance
        val chatClient = ChatClient.instance()
        val devToken = chatClient.devToken(userId)

        val user = StreamUser(
            id = userId,
            extraData = mapOf("name" to "FireTV $userId")
        )

        chatClient.connectUser(user, devToken).enqueue { result ->
            if (result.isSuccess) {
                val intent = Intent(this, PartyKeyActivity::class.java).apply {
                    putExtra("userId", userId)
                    putExtra("token", devToken)
                    putExtra("otherUserId", otherUserId)
                }
                startActivity(intent)
            } else {
                val error = result.errorOrNull()?.message ?: "Connection failed"
                Toast.makeText(this, "Chat connection failed: $error", Toast.LENGTH_LONG).show()
                Log.e("UserSelectActivity", "Chat connection failed: $error")
            }
        }
    }
}

@Composable
fun UserSelectionScreen(onUserSelected: (String, String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .background(Color.White),
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