package com.demo.smarthome.ui.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.demo.smarthome.R;


public class CircleSeekButton extends View {

    private String TAG="CircleSeekButton";
    private int width;// 控件的宽度
    private int height;// 控件的高度
    private int radius;// 圆形的半径
    private Paint paint = new Paint();


    @Deprecated
    private int progressColor = Color.parseColor("#909090");// 进度条颜色
    private float progressbarscale = 0.7f;// 控件内偏距占空间本身的比例
    private int startAngle = 135;
    private int endAngle = 270;// 以startAngle为初始角，经过endAngle结束;
    RectF rectf = new RectF();

    private int minValue;
    private int maxValue;
    private int value;
    private Drawable backgroundRes;
    private Drawable insideRes;
    private Drawable arrowRes;

    private ViewGroup mParentView;
    private boolean bDisallowInterceptTouchEvent=false;

    private Point mThumbCentre = new Point();

    public CircleSeekButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public CircleSeekButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CircleSeekButton, defStyleAttr, 0);
        maxValue = a.getInteger(R.styleable.CircleSeekButton_button_maxValue,50);
        minValue = a.getInteger(R.styleable.CircleSeekButton_button_minValue,-50);
        value = a.getInteger(R.styleable.CircleSeekButton_button_initValue, 0);
        if (a.hasValue(R.styleable.CircleSeekButton_circleButtonOutside)) {
            backgroundRes = a.getDrawable(
                    R.styleable.CircleSeekButton_circleButtonOutside);
            backgroundRes.setCallback(this);
        }else {
            backgroundRes = context.getResources().getDrawable(R.drawable.air_circle_button_bg);
        }

        if (a.hasValue(R.styleable.CircleSeekButton_circleButtonInside)) {
            insideRes = a.getDrawable(
                    R.styleable.CircleSeekButton_circleButtonInside);
            insideRes.setCallback(this);
        }else {
            insideRes = context.getResources().getDrawable(R.drawable.circle_button_inside);
        }

        if (a.hasValue(R.styleable.CircleSeekButton_circleButtonArrow)) {
            arrowRes = a.getDrawable(
                    R.styleable.CircleSeekButton_circleButtonArrow);
            arrowRes.setCallback(this);
        }else {
            arrowRes = context.getResources().getDrawable(R.drawable.circle_button_arrow);
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
        if (value >= maxValue) value=maxValue;
        if (value <= minValue) value = minValue;
        int rotateAngle = (int) (270f/(maxValue-minValue)*(value-minValue));
        radius = (int) (size * progressbarscale / 2f);
        //绘制扇形
        rectf.set((width - radius * 2) / 2f, (height - radius * 2) / 2f,
                ((width - radius * 2) / 2f) + (2 * radius),
                ((height - radius * 2) / 2f) + (2 * radius));
        paint.setColor(progressColor);
        canvas.drawArc(rectf, startAngle + rotateAngle, endAngle - rotateAngle, true, paint);
        //inside
        mBitmap = ((BitmapDrawable) insideRes).getBitmap();
        mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mDstRect = new Rect((width-size)/2,(height-size)/2,(width+size)/2,(height+size)/2);
        canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, paint);
        //arrow
        mBitmap = ((BitmapDrawable) arrowRes).getBitmap();
        Matrix matrix = new Matrix();
        matrix.postTranslate(mBitmap.getWidth()/2f, mBitmap.getHeight()/2f);
        matrix.postRotate(rotateAngle);
        Bitmap dstbmp = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(),
                matrix, true);
       // Log.d(TAG, "Bitmap width is " + mBitmap.getWidth() + " height is " + mBitmap.getHeight() + " Dstbmp width is " + dstbmp.getWidth() + " height is " + dstbmp.getHeight());
        mSrcRect = new Rect((dstbmp.getWidth()-mBitmap.getWidth())/2, (dstbmp.getHeight()-mBitmap.getHeight())/2,
                (dstbmp.getWidth()+mBitmap.getWidth())/2, (dstbmp.getHeight()+mBitmap.getHeight())/2);
        // Draw the example drawable on top of the text.
        mThumbCentre = getPointFromDegree(radius*0.65f, 90-(rotateAngle+startAngle));
        //Log.d(TAG, "mThumbCentre x is " + mThumbCentre.x  + " y is " + mThumbCentre.y + " angle is "+(rotateAngle+startAngle)+" center is "+width/2+","+height/2);
         mDstRect = new Rect(mThumbCentre.x - mBitmap.getWidth()/2 +width/2, mThumbCentre.y - mBitmap.getHeight()/2+height/2 ,
                 mThumbCentre.x + mBitmap.getWidth()/2+width/2 , mThumbCentre.y + mBitmap.getHeight()/2+height/2 );
        canvas.drawBitmap(dstbmp, mSrcRect, mDstRect, paint);

        super.onDraw(canvas);
    }

    private static final int TOUCH_PADDING = 100;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isEnabled())
            return true;
        if (bDisallowInterceptTouchEvent && mParentView != null) {
            mParentView.requestDisallowInterceptTouchEvent(true);//屏蔽父控件对ontouch事件的劫持
        }
        Rect rect =new Rect(mThumbCentre.x - arrowRes.getMinimumWidth()/2 +width/2, mThumbCentre.y - arrowRes.getMinimumHeight()/2+height/2 ,
                mThumbCentre.x +arrowRes.getMinimumWidth()/2+width/2 , mThumbCentre.y + arrowRes.getMinimumHeight()/2+height/2 );
        Rect rect2 = new Rect(rect.left - TOUCH_PADDING, rect.top - TOUCH_PADDING, rect.right + TOUCH_PADDING, rect.bottom + TOUCH_PADDING);

        float x = event.getX();
        float y = event.getY();

        //Log.d(TAG,"touch point x is "+x+" and y is "+y);
        //Log.d(TAG,"rect is ("+rect.left+","+rect.top+","+rect.right+","+rect.bottom+")");
        //Log.d(TAG,"rect2 is ("+rect2.left+","+rect2.top+","+rect2.right+","+rect2.bottom+")");

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (rect2.contains((int)x, (int)y)) {
                mThumbCentre = calcNewCentralPoint(radius*0.65f, x - width/2,  height/2-y);

                double currentDegree = getDegree(mThumbCentre.x, mThumbCentre.y);
                currentDegree = ((currentDegree+(360-startAngle))%360);
                Log.d(TAG,"touch point x is "+x+" and y is "+y+" ,mThumbCentre is ("+mThumbCentre.x+","+mThumbCentre.y+") and current Degree is "+ currentDegree);
                value = (int)((currentDegree-45)/270f*(maxValue-minValue))+minValue;
                 if (value >= maxValue) value=maxValue;
                 if (value <= minValue) value = minValue;
                Log.d(TAG,"value is "+value);
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

    static Point getPointFromDegree(double radius, double degree) {

        double y = radius*Math.cos(Math.PI * degree / 180);
        double x = radius*Math.sin(Math.PI *degree/180);

        return new Point((int)x, (int)y);
    }

    private static Point calcNewCentralPoint(float radius,  float touchX, float touchY) {

        double x,y;
        // y = sqrt(R^2 / (1 + touchX^2 / touchY^2))
        y = Math.sqrt(radius * radius / (1 + touchX * touchX / (touchY * touchY)));
        y = touchY >= 0? y: -y;

        // x = sqrt(R^2 - y^2)
        x = getXFromY(y, radius);
        x = touchX >= 0? x: -x;

        return new Point((int)x,(int)y);
    }

    static double getXFromY(double y, double radius) {
        return Math.sqrt(radius * radius - y * y);
    }

    static double getDegree(double x, double y) {
        double degree = Math.atan(y / x);

        if (x < 0) {
            degree = -Math.PI + degree;
        }

        return (360+45-degree/Math.PI*180)%360;
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

    public int getValue(){
        return value;
    }

    public void setParentViewDisallowInterceptTouchEvent(ViewGroup v,boolean b){
        mParentView = v;
        bDisallowInterceptTouchEvent = b;
    }
}