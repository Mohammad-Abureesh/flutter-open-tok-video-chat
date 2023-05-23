package com.pcnc2000.flutter_open_tok_video_chat.one_to_one_video

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.pcnc2000.flutter_open_tok_video_chat.R

class OpenTokVideoContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    var subscriberContainer: FrameLayout
        private set

    var publisherContainer: FrameLayout
        private set

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.video, this, true)
        subscriberContainer = view.findViewById(R.id.subscriber_container)
        publisherContainer = view.findViewById(R.id.publisher_container)
    }
}