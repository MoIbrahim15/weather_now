package com.mohamedibrahim.weathernow.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.mohamedibrahim.weathernow.R;

@SuppressWarnings("FieldCanBeLocal")
public class CustomTextView extends AppCompatTextView {

    private final int GEORGIA_REGULAR = 10;
    private final int GEORGIA_BOLD = 20;
    private final int GEORGIA_ITALIC = 30;
    private final int GEORGIA_BOLD_ITALIC = 40;

    private int typefaceIndex = -1;
    private int fontRes;
    private Typeface typeface = null;

    public CustomTextView(Context context) {
        super(context);
        initialize(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            typefaceIndex = a.getInt(R.styleable.CustomTextView_fontType, 0);
            a.recycle();
            switch (typefaceIndex) {
                case GEORGIA_REGULAR:
                    fontRes = R.string.georgia_regular;
                    break;
                case GEORGIA_BOLD:
                    fontRes = R.string.georgia_bold;
                    break;
                case GEORGIA_ITALIC:
                    fontRes = R.string.georgia_italic;
                    break;
                case GEORGIA_BOLD_ITALIC:
                    fontRes = R.string.georgia_bold_italic;
                    break;
                default:
                    fontRes = 0;
            }

            if (fontRes != 0) {
                typeface = Typeface.createFromAsset(context.getAssets(), context.getString(fontRes));
                setTypeface(typeface);
            }
        }
    }
}