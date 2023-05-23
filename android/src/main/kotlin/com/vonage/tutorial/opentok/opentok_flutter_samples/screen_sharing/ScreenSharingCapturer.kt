package com.pcnc2000.flutter_open_tok_video_chat.screen_sharing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Handler
import android.view.View

import com.opentok.android.BaseVideoCapturer

class ScreenSharingCapturer(context: Context, view: View) : BaseVideoCapturer() {

    private var context: Context
    private var capturing = false
    private var contentView: View? = null
    private val fps = 15
    private var width = 0
    private var height = 0
    private var frame: IntArray? = null
    private var bmp: Bitmap? = null
    private var canvas: Canvas? = null
    private val handler: Handler = Handler()

    private val newFrame: Runnable = object : Runnable {
        override fun run() {
            if (capturing) {
                val width: Int = contentView!!.width
                val height: Int = contentView!!.height
                if (frame == null || this@ScreenSharingCapturer.width != width || this@ScreenSharingCapturer.height != height) {
                    this@ScreenSharingCapturer.width = width
                    this@ScreenSharingCapturer.height = height
                    if (bmp != null) {
                        bmp!!.recycle()
                        bmp = null
                    }
                    bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    if (bmp != null){
                        canvas = Canvas(bmp!!)
                        frame = IntArray(width * height)
                        canvas!!.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null)
                        canvas!!.translate((-contentView!!.scrollX).toFloat(),
                            (-contentView!!.scrollY).toFloat()
                        )
                        contentView!!.draw(canvas)
                        bmp!!.getPixels(frame, 0, width, 0, 0, width, height)
                        provideIntArrayFrame(frame, ARGB, width, height, 0, false)
                        canvas!!.restore()
                        handler.postDelayed(this, (1000 / fps).toLong())
                    }
                }
            }
        }
    }

    init {
        this.context = context
        this.contentView = view
    }

    override fun init() {}

    override fun startCapture(): Int {
        capturing = true
        handler.postDelayed(newFrame, (1000 / fps).toLong())
        return 0
    }

    override fun stopCapture(): Int {
        capturing = false
        handler.removeCallbacks(newFrame)
        return 0
    }

    override fun isCaptureStarted(): Boolean {
        return capturing
    }

    override fun getCaptureSettings(): CaptureSettings {
        val captureSettings = CaptureSettings()
        captureSettings.fps = fps
        captureSettings.width = width
        captureSettings.height = height
        captureSettings.format = ARGB
        return captureSettings
    }

    override fun destroy() {}
    override fun onPause() {}
    override fun onResume() {}

    init {
        this.context = context
        contentView = view
    }
}