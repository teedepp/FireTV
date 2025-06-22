// FireTVApp.kt
package com.teedee.firetv

import android.app.Application
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory

class FireTVApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val apiKey = "n6j2nbkgkxt2"

        // 🔌 Required plugin instances
        val statePlugin = StreamStatePluginFactory(
            config = StatePluginConfig(), // 👈 default config is fine
            appContext = applicationContext
        )
        val offlinePlugin = StreamOfflinePluginFactory(
            appContext = applicationContext
        )

        ChatClient.Builder(apiKey, applicationContext)
            .withPlugins(statePlugin, offlinePlugin)
            .logLevel(ChatLogLevel.ALL)
            .build()
    }
}
