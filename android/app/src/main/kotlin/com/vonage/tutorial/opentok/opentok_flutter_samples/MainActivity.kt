package com.pcnc2000.flutter_open_tok_video_chat

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import com.pcnc2000.flutter_open_tok_video_chat.archiving.Archiving
import com.pcnc2000.flutter_open_tok_video_chat.archiving.ArchivingFactory
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import com.pcnc2000.flutter_open_tok_video_chat.config.SdkState
import com.pcnc2000.flutter_open_tok_video_chat.multi_video.MultiVideo
import com.pcnc2000.flutter_open_tok_video_chat.multi_video.MultiVideoFactory
import com.pcnc2000.flutter_open_tok_video_chat.one_to_one_video.OneToOneVideo
import com.pcnc2000.flutter_open_tok_video_chat.one_to_one_video.OpentokVideoFactory
import com.pcnc2000.flutter_open_tok_video_chat.screen_sharing.ScreenSharing
import com.pcnc2000.flutter_open_tok_video_chat.screen_sharing.ScreenSharingFactory
import com.pcnc2000.flutter_open_tok_video_chat.singaling.Signalling
import io.flutter.plugin.common.BinaryMessenger

class MainActivity : FlutterActivity() {

    val oneToOneVideoMethodChannel = "com.vonage.one_to_one_video"
    val signallingMethodChannel = "com.vonage.signalling"
    val screenSharingMethodChannel = "com.vonage.screen_sharing"
    val archivingMethodChannel = "com.vonage.archiving"
    val multiVideoMethodChannel = "com.vonage.multi_video"

    private var oneToOneVideo: OneToOneVideo? = null
    private var signalling: Signalling? = null
    private var screenSharing: ScreenSharing? = null
    private var archiving: Archiving? = null
    private var multiVideo: MultiVideo? = null

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        oneToOneVideo = OneToOneVideo(this)
        signalling = Signalling(this)
        screenSharing = ScreenSharing(this)
        archiving = Archiving(this)
        multiVideo = MultiVideo(this)

        flutterEngine
            .platformViewsController
            .registry
            .registerViewFactory("opentok-video-container", OpentokVideoFactory())

        flutterEngine
            .platformViewsController
            .registry
            .registerViewFactory("opentok-screenshare-container", ScreenSharingFactory())

        flutterEngine
            .platformViewsController
            .registry
            .registerViewFactory("opentok-archiving-container", ArchivingFactory())

        flutterEngine
            .platformViewsController
            .registry
            .registerViewFactory("opentok-multi-video-container", MultiVideoFactory())

        addFlutterChannelListener()
    }

    private fun addFlutterChannelListener() {
        flutterEngine?.dartExecutor?.binaryMessenger?.let {
            setOneToOneVideoMethodChannel(it)
            setSignallingMethodChannel(it)
            setScreenSharingMethodChannel(it)
            setArchivingMethodChannel(it)
            setMultiVideoMethodChannel(it)
        }
    }
    private fun setMultiVideoMethodChannel(it: BinaryMessenger) {
        MethodChannel(it, multiVideoMethodChannel).setMethodCallHandler { call, result ->

            when (call.method) {
                "initSession" -> {
                    val apiKey = requireNotNull(call.argument<String>("apiKey"))
                    val sessionId = requireNotNull(call.argument<String>("sessionId"))
                    val token = requireNotNull(call.argument<String>("token"))

                    updateFlutterState(SdkState.wait, multiVideoMethodChannel)
                    multiVideo?.initSession(apiKey, sessionId, token)
                    result.success("")
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }


    private fun setArchivingMethodChannel(it: BinaryMessenger) {
        MethodChannel(it, archivingMethodChannel).setMethodCallHandler { call, result ->

            when (call.method) {
                "initSession" -> {
                    updateFlutterState(SdkState.wait, archivingMethodChannel)
                    archiving?.getSession()
                    result.success("")
                }
                "startArchive" -> {
                    archiving?.startArchive()
                    result.success("")
                }
                "stopArchive" -> {
                    archiving?.stopArchive()
                    result.success("")
                }
                "playArchive" -> {
                    archiving?.playArchive()
                    result.success("")
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }

    private fun setOneToOneVideoMethodChannel(it: BinaryMessenger) {
        MethodChannel(it, oneToOneVideoMethodChannel).setMethodCallHandler { call, result ->

            when (call.method) {
                "initSession" -> {
                    val apiKey = requireNotNull(call.argument<String>("apiKey"))
                    val sessionId = requireNotNull(call.argument<String>("sessionId"))
                    val token = requireNotNull(call.argument<String>("token"))

                    updateFlutterState(SdkState.wait, oneToOneVideoMethodChannel)
                    oneToOneVideo?.initSession(apiKey, sessionId, token)
                    result.success("")
                }
                "swapCamera" -> {
                    oneToOneVideo?.swapCamera()
                    result.success("")
                }
                "toggleAudio" -> {
                    val publishAudio = requireNotNull(call.argument<Boolean>("publishAudio"))
                    oneToOneVideo?.toggleAudio(publishAudio)
                    result.success("")
                }
                "toggleVideo" -> {
                    val publishVideo = requireNotNull(call.argument<Boolean>("publishVideo"))
                    oneToOneVideo?.toggleVideo(publishVideo)
                    result.success("")
                }
                "sendMessage" -> {
                    val message = requireNotNull(call.argument<String>("message"))
                    oneToOneVideo?.sendMessage(message)
                    result.success("")
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }

    private fun setSignallingMethodChannel(it: BinaryMessenger) {
        MethodChannel(it, signallingMethodChannel).setMethodCallHandler { call, result ->

            when (call.method) {
                "initSession" -> {
                    val apiKey = requireNotNull(call.argument<String>("apiKey"))
                    val sessionId = requireNotNull(call.argument<String>("sessionId"))
                    val token = requireNotNull(call.argument<String>("token"))

                    signalling?.initSession(apiKey, sessionId, token)

                    result.success("")
                }
                "sendMessage" -> {
                    val message = requireNotNull(call.argument<String>("message"))
                    signalling?.sendMessage(message)
                    result.success("")
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }

    private fun setScreenSharingMethodChannel(it: BinaryMessenger) {
        MethodChannel(it, screenSharingMethodChannel).setMethodCallHandler { call, result ->

            when (call.method) {
                "initSession" -> {
                    val apiKey = requireNotNull(call.argument<String>("apiKey"))
                    val sessionId = requireNotNull(call.argument<String>("sessionId"))
                    val token = requireNotNull(call.argument<String>("token"))

                    updateFlutterState(SdkState.wait, screenSharingMethodChannel)
                    screenSharing?.initSession(apiKey, sessionId, token)
                    result.success("")
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }

    fun updateFlutterState(state: SdkState, channel: String) {
        Handler(Looper.getMainLooper()).post {
            flutterEngine?.dartExecutor?.binaryMessenger?.let {
                MethodChannel(it, channel)
                    .invokeMethod("updateState", state.toString())
            }
        }
    }

    fun updateFlutterMessages(arguments: HashMap<String, Any>, channel: String){
        Handler(Looper.getMainLooper()).post {
            flutterEngine?.dartExecutor?.binaryMessenger?.let {
                MethodChannel(it, channel)
                    .invokeMethod("updateMessages", arguments)
            }
        }
    }

    fun updateFlutterArchiving(isArchiving: Boolean, channel: String){
        Handler(Looper.getMainLooper()).post {
            flutterEngine?.dartExecutor?.binaryMessenger?.let {
                MethodChannel(it, channel)
                    .invokeMethod("updateArchiving", isArchiving)
            }
        }
    }
}