package com.pcnc2000.flutter_open_tok_video_chat.screen_sharing

import android.content.Context
import android.view.View
import io.flutter.plugin.platform.PlatformView

class ScreenSharingPlatformView(context: Context) : PlatformView {
    private val screenSharingContainer: ScreenSharingContainer = ScreenSharingContainer(context)

    val publisherContainer get() = screenSharingContainer.publisherContainer
    val webView get() = screenSharingContainer.webview

    override fun getView(): View {
        return screenSharingContainer
    }

    override fun dispose() {}
}