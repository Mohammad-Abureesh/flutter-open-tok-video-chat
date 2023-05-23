package com.pcnc2000.flutter_open_tok_video_chat.multi_video

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pcnc2000.flutter_open_tok_video_chat.R

class MultiVideoContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyle, defStyleRes) {

    var mainContainer: ConstraintLayout
        private set

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.multi_video, this, true)
        mainContainer = view.findViewById(R.id.main_container)
    }
}