// ChatInitializer.kt
package com.teedee.firetv

import android.content.Context
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory

object ChatInitializer {

    private var initialized = false

    fun init(context: Context, apiKey: String) {
        if (initialized) return

        val statePlugin = StreamStatePluginFactory(StatePluginConfig(), context)
        val offlinePlugin = StreamOfflinePluginFactory(context)

        ChatClient.Builder(apiKey, context)
            .withPlugins(statePlugin, offlinePlugin) // ✅ Order doesn't matter
            .build()

        initialized = true
    }
}
