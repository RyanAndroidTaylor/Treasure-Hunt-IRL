package com.dtprogramming.treasurehuntirl.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.dtprogramming.treasurehuntirl.R
import java.util.*

/**
 * Created by ryantaylor on 8/8/16.
 */
class BottomTab(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    private val LAYOUT_HEIGHT = 160

    private val tabs = ArrayList<Tab>()
    private var selectedTab: Tab? = null

    private val paint: Paint

    var tabSelectedListener: ((containerUri: String) -> Unit)? = null

    constructor(context: Context): this(context, null)

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = context.resources.getColor(R.color.icon_grey)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)

        setMeasuredDimension(width, LAYOUT_HEIGHT)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(0.0f, 0.0f, width.toFloat(), 1.0f, paint)

        for (tab  in tabs) {
            tab.drawable.draw(canvas)

            canvas.save()

            canvas.translate(tab.textLeft, tab.textTop)

            tab.textLayout.draw(canvas)

            canvas.restore()
        }
    }

    fun addTab(tab: Tab) {
        DrawableCompat.setTint(tab.drawable, context.resources.getColor(R.color.icon_grey))

        if (selectedTab == null)
            tabSelected(tab)

        tabs.add(tab)

        layoutTabs()
    }

    fun layoutTabs() {
        val sectionWidth = (width / tabs.size).toInt()

        val textBounds = Rect()

        for (i in 0..tabs.size - 1) {
            val tab = tabs[i]

            tab.textPaint.getTextBounds("A", 0, 1, textBounds)

            val sectionYCenter = LAYOUT_HEIGHT / 2
            val sectionXCenter = (sectionWidth * i) + (sectionWidth / 2)

            val textWidth = tab.textPaint.measureText(tab.text)
            val textHeight = textBounds.height()

            val overallHeight = textHeight + tab.drawable.intrinsicHeight
            val overallTop = sectionYCenter - (overallHeight / 2) - 10

            val textTop = (overallTop + overallHeight - textHeight).toFloat() - 5
            val textLeft = sectionXCenter - (textWidth / 2)

            tab.textTop = textTop
            tab.textLeft = textLeft

            tab.textLayout = StaticLayout(tab.text, tab.textPaint, textWidth.toInt(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, true)

            val drawableTop = overallTop
            val drawableLeft = sectionXCenter - (tab.drawable.intrinsicWidth / 2)

            tab.drawable.setBounds(drawableLeft, drawableTop, drawableLeft + tab.drawable.intrinsicWidth, drawableTop + tab.drawable.intrinsicHeight)

            tab.top = overallTop
            tab.height = overallHeight
            tab.left = Math.min(textLeft.toInt(), drawableLeft)
            tab.width = Math.max(textWidth.toInt(), tab.drawable.intrinsicWidth)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw || h != oldh)
            layoutTabs()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            for (tab in tabs) {
                if (tab.inBounds(event.x, event.y)) {
                    tabSelected(tab)
                    return true
                }
            }
        }

        return false
    }

    private fun tabSelected(tab: Tab) {
        selectedTab?.let {
            it.textPaint.color = context.resources.getColor(R.color.icon_grey)
            DrawableCompat.setTint(it.drawable, context.resources.getColor(R.color.icon_grey))
        }

        tab.textPaint.color = context.resources.getColor(R.color.colorAccent)
        DrawableCompat.setTint(tab.drawable, context.resources.getColor(R.color.colorAccent))

        selectedTab = tab

        tabSelectedListener?.let { it.invoke(tab.containerUri) }

        invalidate()
    }

    data class Tab(val drawable: Drawable, val text: String, val containerUri: String) {
        val textPaint: TextPaint

        // Must be set in the layoutTabs() method
        lateinit var textLayout: StaticLayout

        var left = 0
        var top = 0
        var width = 0
        var height = 0

        var textLeft = 0.0f
        var textTop = 0.0f

        init {
            textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
            textPaint.textSize = 40f
        }

        fun inBounds(x: Float, y: Float): Boolean {
            return (x >= left && x <= left + width && y >= top && y <= top + height)
        }
    }
}