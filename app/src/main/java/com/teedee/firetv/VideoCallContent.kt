package com.teedee.firetv

import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.renderer.FloatingParticipantVideo
import io.getstream.video.android.compose.ui.components.call.renderer.ParticipantVideo
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.RealtimeConnection

@Composable
fun VideoCallContent(client: StreamVideo, callId: String) {
    val call = client.call(type = "default", id = callId)

    var hasPermissions by remember { mutableStateOf(false) }

    LaunchCallPermissions(
        call = call,
        onAllPermissionsGranted = {
            hasPermissions = true
        }
    )

    LaunchedEffect(hasPermissions) {
        if (hasPermissions) {
            call.join(create = true).onError {
                // TODO: Show an error snackbar/toast
            }
        }
    }

    VideoTheme {
        val remoteParticipants by call.state.remoteParticipants.collectAsState()
        val remoteParticipant = remoteParticipants.firstOrNull()
        val me by call.state.me.collectAsState()
        val connection by call.state.connection.collectAsState()
        var parentSize by remember { mutableStateOf(IntSize(0, 0)) }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(VideoTheme.colors.baseSenary)
                .onSizeChanged { parentSize = it }
        ) {
            if (remoteParticipant != null) {
                ParticipantVideo(
                    modifier = Modifier.fillMaxSize(),
                    call = call,
                    participant = remoteParticipant
                )
            } else {
                val message = if (connection != RealtimeConnection.Connected) {
                    "Connecting to call..."
                } else {
                    "Waiting for a remote participant to join..."
                }

                Text(
                    text = message,
                    modifier = Modifier.padding(30.dp),
                    fontSize = 24.sp,
                    color = VideoTheme.colors.basePrimary,
                    textAlign = TextAlign.Center
                )
            }

            me?.let { localVideo ->
                FloatingParticipantVideo(
                    modifier = Modifier.align(Alignment.TopEnd),
                    call = call,
                    participant = localVideo,
                    parentBounds = parentSize
                )
            }
        }
    }
}
