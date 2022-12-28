package com.sanghm2.customview.customUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.sanghm2.customview.R;

public class CustomEditText extends RelativeLayout {
    private TextView textView;
    private EditText editText;
    private String label;
    private String hint;

    public CustomEditText(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.custom_edit_text, this);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, 0);

        try {
            // get the text and colors specified using the names in attrs.xml
            label = a.getString(R.styleable.CustomEditText_label);
            hint = a.getString(R.styleable.CustomEditText_hint);

        } finally {
            a.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.custom_edit_text, this);

        //text view
        textView = (TextView) this.findViewById(R.id.text_view);
        textView.setText(label);

        //edit text
        editText = (EditText) this.findViewById(R.id.edit_text);
        editText.setHint(hint);

    }
    public String getText() {
        return editText.getText().toString();
    }
    public void setTextView(String text){
        editText.setText(text);
    }

    public void setHint(String text){
        editText.setHint(text+"");
    }
    public void setLabel(String text){
        textView.setText(text+"");
    }

}
