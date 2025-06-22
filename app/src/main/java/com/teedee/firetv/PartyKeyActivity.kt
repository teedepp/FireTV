package com.teedee.firetv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.models.User
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory

class PartyKeyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Stream Chat
        val offlinePluginFactory = StreamOfflinePluginFactory(applicationContext)
        val statePluginFactory = StreamStatePluginFactory(StatePluginConfig(), this)

        val client = ChatClient.Builder("n6j2nbkgkxt2", applicationContext)
            .withPlugins(offlinePluginFactory, statePluginFactory)
            .logLevel(ChatLogLevel.ALL)
            .build()

        val userId = "user1"
        val user = User(id = userId, name = "FireTV User $userId")
        val token = client.devToken(userId)
        client.connectUser(user, token).enqueue()

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "party") {
                    composable("party") { PartyKeyScreen(navController) }
                    composable("watchparty/{partyKey}") { backStackEntry ->
                        val partyKey = backStackEntry.arguments?.getString("partyKey") ?: "UNKNOWN"
                        WatchPartyScreen(partyKey = partyKey)
                    }
                }
            }
        }
    }
}
