package com.pcnc2000.flutter_open_tok_video_chat.archiving.networking

import com.squareup.moshi.Json

class StartArchiveRequest {
    @Json(name = "sessionId")
    var sessionId: String? = null
}