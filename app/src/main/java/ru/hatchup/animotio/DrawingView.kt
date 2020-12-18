package ru.hatchup.animotio

import android.content.Context
import android.graphics.*
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.graphics.get
import androidx.core.graphics.set
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.math.abs


/**
 * TODO: document your custom view class.
 */
class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var drawPath: Path = Path()
    private var canvasPaint: Paint = Paint(Paint.DITHER_FLAG)
    private var paintColor = Color.rgb(0,0,0)
    private var strokeWidthV = 10f
    private lateinit var drawCanvas: Canvas
    private lateinit var canvasBitmap: Bitmap
    private var touchX = 0f
    private var touchY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private var drawPaint = Paint().apply {
        color = paintColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = strokeWidthV // default: Hairline-width (really thin)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::canvasBitmap.isInitialized) canvasBitmap.recycle()
        canvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap)
        drawCanvas.drawColor(Color.rgb(255,255,255))
    }

    override fun onDraw(canvas: Canvas) {

        canvas.drawBitmap(canvasBitmap,0f,0f,canvasPaint)

            canvas.drawPath(drawPath,drawPaint)

    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchX = event.x
        touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> drawPath.reset()

            else -> return false
        }
        invalidate()
        return true

    }
    public fun setColor(color : Int){
        drawPaint.color = color;
    }
    public fun setStroke(width : Float){
        drawPaint.strokeWidth = width;
    }
    public fun getBitmap() : Bitmap{
        invalidate()
        return canvasBitmap
    }
    public fun settBitmap(bmp: Bitmap?){
        if (bmp != null) {
            drawCanvas.drawBitmap(bmp,0f,0f,canvasPaint)
        }


    }
    public fun whiteList(){
        invalidate()
        drawCanvas.drawColor(Color.rgb(255,255,255))
    }


    private fun touchStart() {
        drawPath.reset()
        drawPath.moveTo(touchX, touchY)
        currentX = touchX
        currentY = touchY
    }
    private fun touchMove() {
        val dx = abs(touchX - currentX)
        val dy = abs(touchY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {

            drawPath.quadTo(currentX, currentY, (touchX + currentX) / 2, (touchY + currentY) / 2)
            currentX = touchX
            currentY = touchY
            drawCanvas.drawPath(drawPath, drawPaint)
        }
        invalidate()
    }
}


