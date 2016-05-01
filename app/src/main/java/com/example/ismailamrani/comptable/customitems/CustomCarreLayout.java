package com.example.ismailamrani.comptable.customitems;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.localdata.ScreenSize;
import com.example.ismailamrani.comptable.models.ScreenSizeModel;


/**
 * Created by Ismail Amrani on 21/02/2016.
 */
public class CustomCarreLayout extends RelativeLayout {

    int height, size;
    Context context;

    public CustomCarreLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        ScreenSize Size = new ScreenSize(context);
        ScreenSizeModel HW = Size.GetSize(1);
        height = HW.getHEIGHT();

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomHeightLayout,
                0, 0);

        try {
            size = a.getInteger(R.styleable.CustomHeightLayout_size, 0);
        } finally {
            a.recycle();
        }

    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        ViewGroup.LayoutParams params = super.getLayoutParams();
        params.width = height * size / 246;
        params.height = height * size / 246;
        return params;
    }

}
