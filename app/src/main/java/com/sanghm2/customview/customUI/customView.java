package com.sanghm2.customview.customUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.sanghm2.customview.R;

public class customView extends View {

    private int circleColor , labelCol ;
    private String circleText ;
    private Paint circlePaint ;

    public customView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        circlePaint = new Paint() ;
        TypedArray a =  context.getTheme().obtainStyledAttributes(attrs , R.styleable.customView ,0 ,0 ) ;
        try {
            circleText = a.getString(R.styleable.customView_circleLabel);
            circleColor = a.getColor(R.styleable.customView_circleColor , 0) ;
            labelCol = a.getColor(R.styleable.customView_labelColor,0) ;
        }finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
           int viewWidth = 200 ;
           int viewHeight = 200 ;
           int radius = 0 ;
           if(viewWidth > viewHeight)
               radius = viewHeight - 10 ;
           else
               radius = viewWidth -10 ;
               circlePaint.setStyle(Paint.Style.FILL);
               circlePaint.setAntiAlias(true);

               circlePaint.setColor(circleColor);
               canvas.drawCircle(viewWidth,viewHeight,radius,circlePaint);
               circlePaint.setColor(labelCol);
               circlePaint.setTextAlign(Paint.Align.CENTER);
               circlePaint.setTextSize(50);
               canvas.drawText(circleText,viewWidth,viewHeight,circlePaint);
    }

    public int getCircleColor(){
        return circleColor ;
    }
    public int getLabelCol(){
        return labelCol;
    }
    public String getCircleText(){
        return circleText ;
    }

    public void setCircleColor(int newColor){
        circleColor = newColor ;
        invalidate();
        requestLayout();
    }
    public void setLabelCol(int newColor){
        labelCol =newColor ;
        invalidate();
        requestLayout();
    }
    public void setCircleText(String newText){
        circleText =newText ;
        invalidate();
        requestLayout();
    }
}
