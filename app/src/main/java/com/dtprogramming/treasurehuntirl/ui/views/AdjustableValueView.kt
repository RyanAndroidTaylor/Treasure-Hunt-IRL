package com.dtprogramming.treasurehuntirl.ui.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.dtprogramming.treasurehuntirl.R

/**
 * Created by ryantaylor on 7/9/16.
 */
class AdjustableValueView : View {

    var mLeftDrawable: Drawable? = null
    var mRightDrawable: Drawable? = null

    private var mLeftDrawableOnClickListener: ((v: View) -> Unit)? = null
    private var mRightDrawableOnClickListener: ((v: View) -> Unit)? = null

    var mText: CharSequence? = null
        set(value) {
            field = value

            updateContentBounds()
            invalidate()
        }
    var mTextSize = 40
        set(value) {
            field = value

            updateContentBounds()
            invalidate()
        }

    private var mTextLayout: StaticLayout? = null
    private var mTextPaint: TextPaint
    private var mTextOrigin: Point

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle) {
        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mTextOrigin = Point(0, 0)

        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.AdjustableValueView, 0, defStyle)

        var drawable = attributes.getDrawable(R.styleable.AdjustableValueView_android_drawableLeft)

        drawable?.let { setLeftDrawable(it) }

        drawable = attributes.getDrawable(R.styleable.AdjustableValueView_android_drawableRight)

        drawable?.let { setRightDrawable(it) }

        mTextSize = attributes.getDimensionPixelSize(R.styleable.AdjustableValueView_android_textSize, mTextSize)

        val text = attributes.getText(R.styleable.AdjustableValueView_android_text)

        text?.let { mText = text }

        attributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var finalWidth = 600
        var finalHeight = if (mTextSize > 90)
            mTextSize
        else
            90

        if (widthMode == MeasureSpec.EXACTLY)
            finalWidth = widthSize

        if (heightMode == MeasureSpec.EXACTLY)
            finalHeight = heightSize

        setMeasuredDimension(finalWidth, finalHeight)
    }

    fun updateContentBounds() {
        mTextPaint.textSize = mTextSize.toFloat()

        if (mText == null)
            mText = ""

        mLeftDrawable?.let {
            val top = y.toInt() + (height / 2) - (it.intrinsicHeight / 2)

            it.setBounds(x.toInt(), top, x.toInt() + it.intrinsicWidth, top + it.intrinsicHeight)
        }

        mRightDrawable?.let {
            val left = x.toInt() + width - it.intrinsicWidth
            val top = y.toInt() + (height / 2) - (it.intrinsicHeight / 2)

            it.setBounds(left, top, left + it.intrinsicWidth, top + it.intrinsicHeight)
        }

        // Used to get the height of the text. We use the same letter no matter what so we get the same height to center off of.
        // This makes is so the center does not changed based on the text we use. So "py" would center vertically the same as "Py"
        val textBounds = Rect()
        mTextPaint.getTextBounds("a", 0, 1, textBounds)

        val textWidth = mTextPaint.measureText(mText, 0, mText!!.length)
        val textHeight = textBounds.height()

        mTextLayout = StaticLayout(mText, mTextPaint, textWidth.toInt(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, true)

        val textLeft = x.toInt() + (width / 2) - (textWidth.toInt() / 2)
        val textTop = y.toInt() + (height / 2) - (textHeight / 2)
        mTextOrigin.set(textLeft, textTop)
    }

    override fun onDraw(canvas: Canvas?) {
        mLeftDrawable?.let { it.draw(canvas) }
        mRightDrawable?.let { it.draw(canvas) }

        mTextLayout?.let {
            canvas?.save()

            canvas?.translate(mTextOrigin.x.toFloat(), mTextOrigin.y.toFloat())

            it.draw(canvas)

            canvas?.restore()
        }
    }

    fun setLeftDrawable(drawable: Drawable) {
        mLeftDrawable = drawable
        updateContentBounds()
        invalidate()
    }

    fun setRightDrawable(drawable: Drawable) {
        mRightDrawable = drawable
        updateContentBounds()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldh || h != oldh)
            updateContentBounds()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            if (mLeftDrawableOnClickListener != null) {
                val leftDrawableBounds = mLeftDrawable?.bounds

                if (event != null && leftDrawableBounds != null) {
                    if (leftDrawableBounds.intersect(event.x.toInt(), event.y.toInt(), event.x.toInt(), event.y.toInt())) {
                        mLeftDrawableOnClickListener?.invoke(this)

                        return true
                    }
                }
            }
            if (mRightDrawableOnClickListener != null) {
                val rightDrawableBounds = mRightDrawable?.bounds

                if (event != null && rightDrawableBounds != null) {
                    if (rightDrawableBounds.intersect(event.x.toInt(), event.y.toInt(), event.x.toInt(), event.y.toInt())) {
                        mRightDrawableOnClickListener?.invoke(this)

                        return true
                    }
                }
            }
        }

        return false
    }

    fun setOnLeftDrawableClickListener(leftDrawableOnClickListener: (view: View) -> Unit) {
        mLeftDrawableOnClickListener = leftDrawableOnClickListener
    }

    fun setOnRightDrawableClickListener(rightDrawableOnClickListener: (view: View) -> Unit) {
        mRightDrawableOnClickListener = rightDrawableOnClickListener
    }
}