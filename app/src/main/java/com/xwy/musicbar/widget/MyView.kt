package com.xwy.musicbar.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.xwy.musicbar.util.DisplayUtil


/**
 * Author: xuweiyu
 * Date: 1/31/21
 * Description:
 */
class MyView : View {
    //控件宽度
    private var width = 0f
    //空间高度
    var height = 0f

    //柱状图的宽度
    private var rect_w = DisplayUtil.dip2px(context, 50f).toFloat()
    var voiceHeight = 0f

    private var random //动态变换的高度比例
            = 0

    private val spaceHeight = 20f
    private val lineHeight = 40f


    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //拿到控件的宽高度,并动态计算柱子的宽度
        height = View.MeasureSpec.getSize(heightMeasureSpec).toFloat()
        width = View.MeasureSpec.getSize(widthMeasureSpec).toFloat()
        // Log.i("dandy",""+height+"   "+width);
        //rect_w = (width / lineNumber).toFloat()
        random = (height / 1).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvasLine(canvas, voiceHeight, 0f)
        canvasLine(canvas, voiceHeight, rect_w + DisplayUtil.dip2px(context, 50f))
    }

    private fun canvasLine(canvas: Canvas, canvasHeight: Float, lineLeft: Float) {
        val paint = Paint()
        paint.color = Color.YELLOW
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true

        var mLineHeight = 0f
        while (mLineHeight < canvasHeight) {
            if (mLineHeight > height * 2 / 3) {
                paint.setColor(Color.RED)
            } else if (mLineHeight > height * 1 / 3) {
                paint.setColor(Color.YELLOW)
            } else {
                paint.setColor(Color.GREEN)
            }

            var fraction = mLineHeight / (height - DisplayUtil.dip2px(context, 200f))
            if (fraction > 1) fraction = 1f
            paint.color = getCurrentColor(fraction, Color.GREEN, Color.RED)
            val r = RectF(
                (lineLeft).toFloat(), height - mLineHeight + lineHeight,
                (lineLeft + rect_w).toFloat(), height - mLineHeight
            )
            mLineHeight += lineHeight + spaceHeight
            canvas.drawRect(r, paint)
        }
    }

    /**
     * 根据fraction值来计算当前的颜色。 fraction值范围  0f-1f
     */
    private fun getCurrentColor(fraction: Float, startColor: Int, endColor: Int): Int {
        val redCurrent: Int
        val blueCurrent: Int
        val greenCurrent: Int
        val alphaCurrent: Int
        val redStart = Color.red(startColor)
        val blueStart = Color.blue(startColor)
        val greenStart = Color.green(startColor)
        val alphaStart = Color.alpha(startColor)
        val redEnd = Color.red(endColor)
        val blueEnd = Color.blue(endColor)
        val greenEnd = Color.green(endColor)
        val alphaEnd = Color.alpha(endColor)
        val redDifference = redEnd - redStart
        val blueDifference = blueEnd - blueStart
        val greenDifference = greenEnd - greenStart
        val alphaDifference = alphaEnd - alphaStart
        redCurrent = (redStart + fraction * redDifference).toInt()
        blueCurrent = (blueStart + fraction * blueDifference).toInt()
        greenCurrent = (greenStart + fraction * greenDifference).toInt()
        alphaCurrent = (alphaStart + fraction * alphaDifference).toInt()
        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent)
    }
}
