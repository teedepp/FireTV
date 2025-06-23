package com.teedee.firetv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User

class VideoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = intent.getStringExtra("userId") ?: return
        val userToken = intent.getStringExtra("token") ?: return
        val callId = intent.getStringExtra("callId") ?: return
        val apiKey = intent.getStringExtra("apiKey") ?: return

        val user = User(
            id = userId,
            name = "FireTV User $userId",
            image = "https://bit.ly/2TIt8NR"
        )

        val client = StreamVideoBuilder(
            context = applicationContext,
            apiKey = apiKey,
            geo = GEO.GlobalEdgeNetwork,
            user = user,
            token = userToken,
        ).build()

        setContent {
            VideoCallContent(client = client, callId = callId)
        }
    }

    companion object {
        fun getIntent(
            context: Context,
            userId: String,
            token: String,
            callId: String,
            apiKey: String
        ): Intent {
            return Intent(context, VideoActivity::class.java).apply {
                putExtra("userId", userId)
                putExtra("token", token)
                putExtra("callId", callId)
                putExtra("apiKey", apiKey)
            }
        }
    }
}
