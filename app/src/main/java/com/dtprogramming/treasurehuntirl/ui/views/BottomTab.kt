package com.dtprogramming.treasurehuntirl.ui.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.dtprogramming.treasurehuntirl.R
import java.util.*

/**
 * Created by ryantaylor on 8/8/16.
 */
class BottomTab(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    private val LAYOUT_HEIGHT = 210
    private val DROP_SHADOW_SIZE = 30f

    private val tabs = ArrayList<Tab>()
    private var selectedTab: Tab? = null


    private val gradientPaint: Paint
    private val paint: Paint

    private val lineGrey: Int
    private val background: Int

    var tabSelectedListener: ((containerUri: String) -> Unit)? = null

    constructor(context: Context): this(context, null)

    init {
        lineGrey = context.resources.getColor(R.color.line_grey)
        background = context.resources.getColor(R.color.white)

        paint = Paint(Paint.ANTI_ALIAS_FLAG)

        gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        gradientPaint.color = context.resources.getColor(R.color.shadow_grey)
        gradientPaint.shader = LinearGradient(0f, DROP_SHADOW_SIZE, width.toFloat(), 0f, gradientPaint.color, context.resources.getColor(R.color.transparent), Shader.TileMode.CLAMP)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)

        setMeasuredDimension(width, LAYOUT_HEIGHT)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), DROP_SHADOW_SIZE, gradientPaint)

        paint.color = lineGrey
        canvas.drawLine(0f, DROP_SHADOW_SIZE, width.toFloat(), DROP_SHADOW_SIZE - 2, paint)
        paint.color = background
        canvas.drawRect(0f, DROP_SHADOW_SIZE - 3, width.toFloat(), height.toFloat(), paint)

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

    fun moveToTab(containerUri: String) {
        for (tab in tabs) {
            if (tab.containerUri.equals(containerUri)) {
                tabSelected(tab)
                return
            }
        }

        Log.e("BottomTab", "No tab found with containerUri: $containerUri")
    }

    private fun layoutTabs() {
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
        // No reason to load tab if the user is selecting the tab that is already selected
        if (selectedTab != null && selectedTab == tab)
            return

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