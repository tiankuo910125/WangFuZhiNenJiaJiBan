package com.demo.smarthome.ui.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.demo.smarthome.R;
import com.demo.smarthome.base.utils.MyLogger;

/**
 * TODO: document your custom view class.
 */
public class CircleSeekbar extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private int mRadius = 0;
    private int mMaxY = 0;
    private double mMaxDegree = 0;
    private double mMinDegree = 0;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    // default value is [0,100]
    private int mMaxValue = 100;
    private int mMinValue = 0;
    private int mCurrentValue = 0;

    private double mRatio = 1;

    public CircleSeekbar(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleSeekbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CircleSeekbar, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.CircleSeekbar_exampleString);
        mExampleColor = a.getColor(
                R.styleable.CircleSeekbar_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.CircleSeekbar_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.CircleSeekbar_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.CircleSeekbar_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        mRadius = a.getDimensionPixelSize(R.styleable.CircleSeekbar_arc_sb_radius, 0);
        mMaxY = a.getDimensionPixelSize(R.styleable.CircleSeekbar_arc_sb_maxY, 0);

        double x = getXFromY(mMaxY, mRadius);
        mMaxDegree = getDegree(x, mMaxY);
        mMinDegree = getDegree(-x, mMaxY);

        try {
            mMaxValue = a.getInteger(R.styleable.CircleSeekbar_arc_sb_max_value, 100);
        } catch (NumberFormatException e) {
            // nothing to do with the exception, but the maxvalue remains to be the default value
        }


        try {
            mMinValue = a.getInteger(R.styleable.CircleSeekbar_arc_sb_min_value, 100);
        } catch (NumberFormatException e) {
            // nothing to do with the exception, but the minvalue remains to be the default value
        }


        try {
            mCurrentValue = a.getInteger(R.styleable.CircleSeekbar_arc_sb_init_value, 50);
        } catch (NumberFormatException e) {
            // nothing to do with the exception, but the initvalue remains to be the default value
        }

        mRatio = (mMaxDegree - mMinDegree) / (mMaxValue - mMinValue);

        // degree = (value - minValue) * ratio + minValue;
        // value = (degree - minDegree) / ratio + minDegree;

        mCentre.x = a.getDimensionPixelSize(R.styleable.CircleSeekbar_arc_sb_centre_X, 0);
        mCentre.y = a.getDimensionPixelSize(R.styleable.CircleSeekbar_arc_sb_centre_Y, 0);

        a.recycle();

        // calculate current position
        double currentDegree = (mCurrentValue - mMinValue) * mRatio + mMinDegree;
        mThumbCentre = getPointFromDegree(mRadius, currentDegree);


        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    public void setCurrentValue(int currentValue) {
        mCurrentValue = currentValue;

        double currentDegree = (mCurrentValue - mMinValue) * mRatio + mMinDegree;
        mThumbCentre = getPointFromDegree(mRadius, currentDegree);

        invalidate();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        /*

        // Draw the text.
        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);
        */

        int thumbWidth = mExampleDrawable.getMinimumWidth();
        int thumbHeight = mExampleDrawable.getMinimumHeight();

        Rect rect = mExampleDrawable.getBounds();

        /*
        thumbHeight = mExampleDrawable.getIntrinsicHeight();
        thumbWidth = mExampleDrawable.getIntrinsicWidth();
        */


        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            Rect bounds = new Rect(mThumbCentre.x - thumbWidth/2 + mCentre.x, mThumbCentre.y - thumbHeight/2 + mCentre.y,
                    mThumbCentre.x + thumbWidth/2 + mCentre.x, mThumbCentre.y + thumbHeight/2 + mCentre.y);

            sLogger.v("bounds: " + bounds);

            mExampleDrawable.setBounds(bounds);
            mExampleDrawable.draw(canvas);
        }
    }

    private Point mCentre = new Point();
    private Point mThumbCentre = new Point();
    private static final int TOUCH_PADDING = 10;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sLogger.v("onTouchEvent...start: " + event);

        Rect rect = mExampleDrawable.getBounds();
        Rect rect2 = new Rect(rect.left - TOUCH_PADDING, rect.top - TOUCH_PADDING, rect.right + TOUCH_PADDING, rect.bottom + TOUCH_PADDING);

        sLogger.v("rect1:" + rect);
        sLogger.v("rect2: " + rect2);

        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (rect2.contains((int)x, (int)y)) {

                sLogger.v("get motion");

                mThumbCentre = calcNewCentralPoint(mRadius, mMaxY, x - mCentre.x, y - mCentre.y);
                sLogger.v("new center: " + mThumbCentre);

                double currentDegree = getDegree(mThumbCentre.x, mThumbCentre.y);

                // value = (degree - minDegree) / ratio + minDegree;
                int value = (int)((currentDegree - mMinDegree) / mRatio + mMinValue);

                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.OnValueChanged(value);
                }
                invalidate();
            }
        }

        return true;
    }

    public interface OnValueChangedListener {
        void OnValueChanged(int value);
    }

    public void setOnSeekbarChangeListener(OnValueChangedListener listener) {
        mOnSeekBarChangeListener = listener;
    }
    private OnValueChangedListener mOnSeekBarChangeListener = null;

    private static Point calcNewCentralPoint(float radius, int maxY, float touchX, float touchY) {

        double x,y;
        // y = sqrt(R^2 / (1 + touchX^2 / touchY^2))
        y = Math.sqrt(radius * radius / (1 + touchX * touchX / (touchY * touchY)));
        y = touchY >= 0? y: -y;

        y = Math.min(y, maxY);

        // x = sqrt(R^2 - y^2)
        x = getXFromY(y, radius);
        x = touchX >= 0? x: -x;

        return new Point((int)x,(int)y);
    };

    static double getXFromY(double y, double radius) {
        return Math.sqrt(radius * radius - y * y);
    };

    static double getDegree(double x, double y) {
        double degree = Math.atan(y / x);

        if (x < 0) {
            degree = -Math.PI + degree;
        }

        return degree;
    }

    static Point getPointFromDegree(double radius, double degree) {
        double val = 1 / Math.tan(degree);

        double y = Math.sqrt(radius * radius / (1 + val * val));

        y = degree >= 0 || degree <= -Math.PI? y: -y;

        double x = getXFromY(y, radius);
        x = degree >= -Math.PI/2 && degree <= Math.PI/2? x: -x;

        return new Point((int)x, (int)y);
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }

    private final static MyLogger sLogger = new MyLogger("CircleSeekbar");

}
