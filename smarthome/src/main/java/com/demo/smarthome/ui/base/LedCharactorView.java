package com.demo.smarthome.ui.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.demo.smarthome.R;

import java.util.ArrayList;


public class LedCharactorView extends View {

    private int width;// 控件的宽度
    private int height;// 控件的高度
    private Paint paint = new Paint();


    @Deprecated
    private String value = "0";
    private Drawable backgroundRes;


    public LedCharactorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public LedCharactorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        int size = height = getHeight();
        if (height > width/2)
            size = width/2;

        paint.setAntiAlias(true);//抗锯齿

        ArrayList<Integer> resList = new ArrayList<>();
        for (int i=0;i<value.length();i++){
            int resId = findResId(value.charAt(i));
            if (resId != -1)
                resList.add(resId);
        }
        int length = Math.min(resList.size(),3); //最多显示3位
        for (int i = 0;i<length;i++){
            Bitmap mBitmap = ((BitmapDrawable) getResources().getDrawable(resList.get(i))).getBitmap();
            Rect mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            Rect mDstRect = new Rect(width/6*(3-length)+width/3*i,(height-size)/2,width/6*(3-length)+width/3*(i+1),(height + size) / 2);
            canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, paint);
        }
        super.onDraw(canvas);
    }

    public void setValue(String value) {
        this.value = value;
        invalidate();
    }

    private int findResId(char c){
        int resId=-1;
        switch (c){
            case '0':{
                resId = R.drawable.led_0;
                break;
            }
            case '1':{
                resId = R.drawable.led_1;
                break;
            }
            case '2':{
                resId = R.drawable.led_2;
                break;
            }
            case '3':{
                resId = R.drawable.led_3;
                break;
            }
            case '4':{
                resId = R.drawable.led_4;
                break;
            }
            case '5':{
                resId = R.drawable.led_5;
                break;
            }
            case '6':{
                resId = R.drawable.led_6;
                break;
            }
            case '7':{
                resId = R.drawable.led_7;
                break;
            }
            case '8':{
                resId = R.drawable.led_8;
                break;
            }
            case '9':{
                resId = R.drawable.led_9;
                break;
            }
            case 'C':{
                resId = R.drawable.led_signal2;
                break;
            }
            case '%':{
                resId = R.drawable.led_signal1;
                break;
            }

        }
        return resId;
    }

    private int findHighLightResId(char c){
        int resId=-1;
        switch (c){
            case '0':{
                resId = R.drawable.led_0_a;
                break;
            }
            case '1':{
                resId = R.drawable.led_1_a;
                break;
            }
            case '2':{
                resId = R.drawable.led_2_a;
                break;
            }
            case '3':{
                resId = R.drawable.led_3_a;
                break;
            }
            case '4':{
                resId = R.drawable.led_4_a;
                break;
            }
            case '5':{
                resId = R.drawable.led_5_a;
                break;
            }
            case '6':{
                resId = R.drawable.led_6_a;
                break;
            }
            case '7':{
                resId = R.drawable.led_7_a;
                break;
            }
            case '8':{
                resId = R.drawable.led_8_a;
                break;
            }
            case '9':{
                resId = R.drawable.led_9_a;
                break;
            }
            case 'C':{
                resId = R.drawable.led_signal2_red;
                break;
            }
            case '%':{
                resId = R.drawable.led_signal_red;
                break;
            }

        }
        return resId;
    }
}