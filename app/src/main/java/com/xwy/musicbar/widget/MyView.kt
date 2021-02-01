package com.xwy.musicbar.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View


/**
 * Author: xuweiyu
 * Date: 1/31/21
 * Description:
 */
class MyView : View {
    private var width //控件宽度
            = 0f
    var height //空间高度
            = 0f
    private var rect_w //柱状图的宽度
            = 100f
    var voiceHeight = 0f

    private var random //动态变换的高度比例
            = 0
    private var status = true //控件的状态

    private val spaceHeight = 20f
    private val lineHeight = 40f

    private val lineNumber = 2


    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    /**这里是为了实现点击一下控件就停止
     * 控制控件是否跳动  true为跳动，false为停止
     * @param b
     */
    fun change_Status(b: Boolean) {
        status = b
    }

    /**这里是为了实现点击一下控件就停止
     * 拿到控件的状态
     * @return
     */
    fun get_Status(): Boolean {
        // Log.i("smile","状态改变了吗？");
        return status
    }

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
        if (status == true) {

        } else get_Status()
        super.onDraw(canvas)

        /*canvas.drawLine(
            (width * 0.1).toFloat(),
            (height * 0.9).toFloat(), (width * 0.9).toFloat(), (height * 0.9).toFloat(), paint
        )*/
        //画柱状；动态图，就要改变柱状的top值
        /*val r1 = RectF(
            (width * 0.1).toFloat(), (rect_t1 * 5).toFloat(),
            (width * 0.1 + rect_w).toFloat(), (height * 0.9).toFloat()
        )*/
        /*val r2 = RectF(
            (width * 0.1 + rect_w).toFloat(), (rect_t2 * 5).toFloat(),
            (width * 0.1 + rect_w * 2).toFloat(), (height * 0.9).toFloat()
        )
        val r3 = RectF(
            (width * 0.1 + rect_w * 2).toFloat(), (rect_t3 * 5).toFloat(),
            (width * 0.1 + rect_w * 3).toFloat(), (height * 0.9).toFloat()
        )
        val r4 = RectF(
            (width * 0.1 + rect_w * 3).toFloat(), (rect_t4 * 5).toFloat(),
            (width * 0.1 + rect_w * 4).toFloat(), (height * 0.9).toFloat()
        )
        val r5 = RectF(
            (width * 0.1 + rect_w * 4).toFloat(), (rect_t5 * 5).toFloat(),
            (width * 0.1 + rect_w * 5).toFloat(), (height * 0.9).toFloat()
        )*/
        /*val paint1 = Paint()
        paint1.setColor(Color.YELLOW)
        paint1.setStyle(Paint.Style.FILL)
        paint1.setAntiAlias(true)

        var mLineHeight = 0f
        while (mLineHeight < rect_t1) {
            if (mLineHeight > height / 3) {
                paint1.setColor(Color.GREEN)
            } else {
                paint1.setColor(Color.YELLOW)
            }
            val r1 = RectF(
                (width * 0.1).toFloat(), (height - mLineHeight + lineHeight).toFloat(),
                (width * 0.1 + rect_w).toFloat(), (height - mLineHeight).toFloat()
            )
            mLineHeight += lineHeight + spaceHeight
            canvas.drawRect(r1, paint1)
        }*/

        canvasLine(canvas, voiceHeight, 0f)
        canvasLine(canvas, voiceHeight, rect_w + 250)
    }

    private fun canvasLine(canvas: Canvas, canvasHeight: Float, lineLeft: Float) {
        val paint = Paint()
        paint.color = Color.YELLOW
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true

        var mLineHeight = 0f
        while (mLineHeight < canvasHeight) {
            if (mLineHeight > height * 2/3) {
                paint.setColor(Color.RED)
            } else if (mLineHeight > height * 1/3) {
                paint.setColor(Color.YELLOW)
            } else {
                paint.setColor(Color.GREEN)
            }
            val r = RectF(
                (lineLeft).toFloat(), height - mLineHeight + lineHeight,
                (lineLeft + rect_w).toFloat(), height - mLineHeight
            )
            mLineHeight += lineHeight + spaceHeight
            canvas.drawRect(r, paint)
        }
    }
}
