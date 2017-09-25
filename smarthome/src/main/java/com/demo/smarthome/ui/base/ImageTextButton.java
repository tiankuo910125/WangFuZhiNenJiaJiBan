package com.demo.smarthome.ui.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.smarthome.R;


/**
 * Created by liukun on 2016/3/17.
 */
public class ImageTextButton extends LinearLayout{

    private Context mContext;
    private ImageView image;
    private TextView text;

    public ImageTextButton(Context context) {
        super(context);
        mContext = context;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.image_text_button, this);
        text = (TextView)findViewById(R.id.text);
        image = (ImageView)findViewById(R.id.image);
    }

    public ImageTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.image_text_button, this);
        text = (TextView)findViewById(R.id.text);
        image = (ImageView)findViewById(R.id.image);
    }

    // ----------------public method-----------------------------

/*
   * setImageResource方法
   */

    public
    void setImageResource(int resId) {

        image.setImageResource(resId);
    }


/*
   * setText方法
   */

    public
    void setText(int resId) {

        text.setText(resId);
    }


    public
    void setText(CharSequence buttonText) {

        text.setText(buttonText);
    }


/*
   * setTextColor方法
   */

    public
    void setTextColor(int color) {

        text.setTextColor(color);
    }
}
