package com.example.ismailamrani.comptable.customitems;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.localdata.ScreenSize;
import com.example.ismailamrani.comptable.models.ScreenSizeModel;

/**
 * Created by Ismail Amrani on 25/11/2015.
 */
public class CustomTextView extends TextView {

    int height, size;
    String Font, Arabic, Frensh;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CostumFont,
                0, 0);

        try {
            size = a.getInteger(R.styleable.CostumFont_Size, 0);
            Font = a.getString(R.styleable.CostumFont_Font);
            Arabic = a.getString(R.styleable.CostumFont_Arabic);
            Frensh = a.getString(R.styleable.CostumFont_Frensh);
        } finally {
            a.recycle();
        }

        ScreenSize Size = new ScreenSize(context);
        ScreenSizeModel HW = Size.GetSize(1);
        height = HW.getHEIGHT();

        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/" + Font);
        setTypeface(typeFace);

        setTextSize(TypedValue.COMPLEX_UNIT_PX, (size * height) / 492);
        setText(Frensh);
    }

    public void SetText(String Text) {
        setText(Text);
    }
}