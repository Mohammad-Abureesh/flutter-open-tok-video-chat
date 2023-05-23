package com.pcnc2000.flutter_open_tok_video_chat.screen_sharing

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.pcnc2000.flutter_open_tok_video_chat.R

class ScreenSharingContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    var webview: WebView
        private set

    var publisherContainer: FrameLayout
        private set

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.screen_sharing, this, true)
        publisherContainer = view.findViewById(R.id.publisher_container)
        webview = view.findViewById(R.id.webview)
    }
}