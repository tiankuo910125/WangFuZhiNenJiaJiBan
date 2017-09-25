package com.demo.smarthome.ui.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.demo.smarthome.R;


public class CircleProgressView extends View {

    private int width;// 控件的宽度
    private int height;// 控件的高度
    private int radius;// 圆形的半径
    private Paint paint = new Paint();


    @Deprecated
    private int progressColor = Color.parseColor("#505050");// 进度条颜色
    private float progressbarscale = 0.64f;// 控件内偏距占空间本身的比例
    private int startAngle = 135;
    private int endAngle = 270;// 以startAngle为初始角，经过endAngle结束;
    RectF rectf = new RectF();

    private int minValue = -50;
    private int maxValue = 50;
    private int value = 50;
    private Drawable backgroundRes;
    private Drawable insideRes;
    private Drawable arrowRes;

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CircleProgressView, defStyleAttr, 0);
        maxValue = a.getInteger(R.styleable.CircleProgressView_maxValue,50);
        minValue = a.getInteger(R.styleable.CircleProgressView_minValue,-50);
        value = a.getInteger(R.styleable.CircleProgressView_initValue,0);

        if (a.hasValue(R.styleable.CircleProgressView_circleProgressViewOutside)) {
            backgroundRes = a.getDrawable(
                    R.styleable.CircleProgressView_circleProgressViewOutside);
            backgroundRes.setCallback(this);
        }else {
            backgroundRes = context.getResources().getDrawable(R.drawable.meter_on_bg);
        }

        if (a.hasValue(R.styleable.CircleProgressView_circleProgressViewInside)) {
            insideRes = a.getDrawable(
                    R.styleable.CircleProgressView_circleProgressViewInside);
            insideRes.setCallback(this);
        }else {
            insideRes = context.getResources().getDrawable(R.drawable.meter_screen);
        }

        if (a.hasValue(R.styleable.CircleProgressView_circleProgressViewArrow)) {
            arrowRes = a.getDrawable(
                    R.styleable.CircleProgressView_circleProgressViewArrow);
            arrowRes.setCallback(this);
        }else {
            arrowRes = context.getResources().getDrawable(R.drawable.meter);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        int size = height = getHeight();
        if (height > width)
            size = width;

        paint.setAntiAlias(true);//抗锯齿

        Bitmap mBitmap = ((BitmapDrawable) backgroundRes).getBitmap();
        Rect mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        Rect mDstRect = new Rect((width-size)/2,(height-size)/2,(width+size)/2,(height + size) / 2);
        canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, paint);

        if (maxValue == minValue) minValue = maxValue-1;
        int rotateAngle = (int) (270f/(maxValue-minValue)*(value-minValue));
        radius = (int) (size * progressbarscale / 2f);
        //绘制扇形
        rectf.set((width - radius * 2) / 2f, (height - radius * 2) / 2f,
                ((width - radius * 2) / 2f) + (2 * radius),
                ((height - radius * 2) / 2f) + (2 * radius));
        paint.setColor(progressColor);
        canvas.drawArc(rectf, startAngle + rotateAngle, endAngle - rotateAngle, true, paint);

        mBitmap = ((BitmapDrawable) arrowRes).getBitmap();
        Matrix matrix = new Matrix();
        matrix.postTranslate(mBitmap.getWidth()/2f, mBitmap.getHeight()/2f);
        matrix.postRotate(rotateAngle - 102);
        Bitmap dstbmp = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(),
                matrix, true);
        Log.d("CircleProgressView","Bitmap width is "+mBitmap.getWidth()+ " height is "+mBitmap.getHeight()+" Dstbmp width is "+ dstbmp.getWidth()+" height is "+dstbmp.getHeight());
        mSrcRect = new Rect((dstbmp.getWidth()-mBitmap.getWidth())/2, (dstbmp.getHeight()-mBitmap.getHeight())/2,
                (dstbmp.getWidth()+mBitmap.getWidth())/2, (dstbmp.getHeight()+mBitmap.getHeight())/2);
        mDstRect = new Rect((width-size)/2,(height-size)/2,(width+size)/2,(height+size)/2);
        canvas.drawBitmap(dstbmp, mSrcRect, mDstRect, paint);

        mBitmap = ((BitmapDrawable) insideRes).getBitmap();
        mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mDstRect = new Rect((width-size)/2,(height-size)/2,(width+size)/2,(height+size)/2);
        canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, paint);

        super.onDraw(canvas);
    }

    public int dp2px(int dp) {
        return (int) ((getResources().getDisplayMetrics().density * dp) + 0.5);
    }

    /**
     * 设置进度
     *
     * @param value
     *            <p>
     *            ps: 百分比 0~100;
     */
    public void setValue(int value) {
        if (value > maxValue) value = maxValue;
        if (value < minValue) value = minValue;
        this.value = value;
        invalidate();
    }

    public void setMaxValue(int value) {
        if (value < minValue) value = minValue+1;
        this.maxValue = value;
        invalidate();
    }

    public void setMinValue(int value) {
        if (value > maxValue)  value = maxValue-1;
        this.minValue = value;
        invalidate();
    }

}