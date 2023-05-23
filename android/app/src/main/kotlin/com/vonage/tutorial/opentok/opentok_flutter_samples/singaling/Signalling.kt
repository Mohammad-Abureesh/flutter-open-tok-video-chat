package com.pcnc2000.flutter_open_tok_video_chat.singaling

import android.util.Log
import com.opentok.android.OpentokError
import com.opentok.android.Session
import com.opentok.android.Session.SessionListener
import com.opentok.android.Session.SignalListener
import com.opentok.android.Stream
import com.pcnc2000.flutter_open_tok_video_chat.MainActivity


class Signalling(mainActivity: MainActivity) {
    val tag = "Signalling"
    private val signal_type = "textMessage"

    private var activity: MainActivity? = null
    private var session: Session? = null

    private val sessionListener: SessionListener = object : SessionListener {
        override fun onConnected(session: Session) {
            Log.i(tag, "Session Connected")
        }

        override fun onDisconnected(session: Session) {
            Log.i(tag, "Session Disconnected")
        }

        override fun onStreamReceived(session: Session?, stream: Stream?) {
            Log.i(tag, "Stream Received")
        }

        override fun onStreamDropped(session: Session?, stream: Stream?) {
            Log.i(tag, "Stream Dropped")
        }

        override fun onError(session: Session, opentokError: OpentokError) {
            Log.e(tag, "Session error: " + opentokError.message)
        }
    }

    private val signalListener =
        SignalListener { session, type, data, connection ->
            val remote = !connection.equals(session.connection)
            if (type != null && type == signal_type) {
                showMessage(data, remote)
            }
        }

    init {
        activity = mainActivity
    }

    fun initSession(apiKey: String, sessionId: String, token: String) {
        session = Session.Builder(activity, apiKey, sessionId).build()
        session?.setSessionListener(sessionListener)
        session?.setSignalListener(signalListener);
        session?.connect(token)
    }


    fun sendMessage(message: String) {
        Log.d(tag, "Send Message")
        session!!.sendSignal(signal_type, message)
    }

    private fun showMessage(messageData: String, remote: Boolean) {
        Log.d(tag, "Show Message")
        val arguments: HashMap<String, Any> = HashMap()
        arguments["message"] = messageData
        arguments["remote"] = remote
        activity?.updateFlutterMessages(arguments, activity!!.signallingMethodChannel)
    }
}