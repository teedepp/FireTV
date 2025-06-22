package com.teedee.firetv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.getstream.chat.android.client.ChatClient

class PartyKeyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = intent.getStringExtra("token") ?: ""

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val userId = ChatClient.instance().getCurrentUser()?.id

                NavHost(navController = navController, startDestination = "party") {
                    composable("party") {
                        PartyKeyScreen(navController)
                    }

                    composable(
                        route = "watchparty/{partyKey}",
                        arguments = listOf(navArgument("partyKey") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val partyKey = backStackEntry.arguments?.getString("partyKey") ?: "UNKNOWN"
                        if (userId != null) {
                            WatchPartyScreen(userId = userId, partyKey = partyKey)
                        }
                    }
                }
            }
        }
    }
}
