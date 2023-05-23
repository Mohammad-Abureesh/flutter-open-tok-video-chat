package com.pcnc2000.flutter_open_tok_video_chat.one_to_one_video

import android.content.Context
import android.view.View
import io.flutter.plugin.platform.PlatformView

class OpentokVideoPlatformView(context: Context) : PlatformView {
    private val videoContainer: OpenTokVideoContainer = OpenTokVideoContainer(context)

    val subscriberContainer get() = videoContainer.subscriberContainer
    val publisherContainer get() = videoContainer.publisherContainer

    override fun getView(): View {
        return videoContainer
    }

    override fun dispose() {}
}