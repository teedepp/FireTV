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

        val otherUserId = intent.getStringExtra("otherUserId") ?: "user2"

        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "party") {
                    composable("party") {
                        PartyKeyScreen(navController = navController, otherUserId = otherUserId)
                    }

                    composable(
                        route = "watchparty/{partyKey}/{otherUserId}",
                        arguments = listOf(
                            navArgument("partyKey") { type = NavType.StringType },
                            navArgument("otherUserId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val partyKey = backStackEntry.arguments?.getString("partyKey") ?: "UNKNOWN"
                        val otherUserId = backStackEntry.arguments?.getString("otherUserId") ?: "user2"
                        val userId = ChatClient.instance().getCurrentUser()?.id ?: return@composable

                        WatchPartyScreen(
                            userId = userId,
                            otherUserId = otherUserId,
                            partyKey = partyKey
                        )
                    }
                }
            }
        }
    }
}

