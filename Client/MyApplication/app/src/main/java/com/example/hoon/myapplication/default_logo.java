package com.example.hoon.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by hoon on 2017-11-15.
 */

public class default_logo extends RelativeLayout{

    public default_logo(Context context) {
        super(context);
        initView();
    }

    public default_logo(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public default_logo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View xml_design = inflater.inflate(R.layout.default_layout, this, false);
        addView(xml_design);
    }
}


