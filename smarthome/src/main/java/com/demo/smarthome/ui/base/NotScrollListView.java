package com.demo.smarthome.ui.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by liukun on 2016/2/22.
 */
public class NotScrollListView extends ListView {

        public NotScrollListView(Context context) {
            super(context);
        }

        public NotScrollListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public NotScrollListView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }

}
