package com.jiangdg.circleprogressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;

/**自定义连接Button
 *http://blog.csdn.net/forrestl/article/details/52288389
 * Created by jianddongguo on 2017/8/14.
 */

public class CircleProgressView extends View {
    // 状态正在进行
    public static int STATE_DOING = 0;
    // 状态操作完成
    public static int STATE_DONE = 1;
    // 状态操作未完成或初始状态
    public static int STATE_UNDONE = 2;

    public static int NONE = -1;

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int circleX;
    private int circleY;
    private int radius;
    private int state;
    private int mSweepAngle = 1;
    private boolean isOddNumber = true;
    private int outsideCircleBgColor;
    private int progressArcBgColor;
    private int insideCircleBgColor;
    private int insideCircleTouchedBgColor;
    private int insideRectangleBgColor;
    private float tipTextSize;
    private int tipTextColor;
    // 进度值
    private int progress;
    private int totalSize;
    private boolean isShowTextTip;
    private boolean isTouched;
    // 点击事件回调
    private OnViewClickListener listener;
    private boolean isDisabled;

    public CircleProgressView(Context context) {
        super(context);
    }

    public interface OnViewClickListener{
        void onViewClick();
    }

    // 点击事件回调
    public void setOnViewClickListener(OnViewClickListener listener){
        this.listener = listener;
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.CircleProgressView);
        outsideCircleBgColor = ta.getColor(R.styleable.CircleProgressView_outsideCircleBgColor,getResources().getColor(R.color.colorWhite));
        progressArcBgColor = ta.getColor(R.styleable.CircleProgressView_progressArcBgColor,getResources().getColor(R.color.colorGray));
        insideCircleBgColor = ta.getColor(R.styleable.CircleProgressView_insideCircleBgColor,getResources().getColor(R.color.colorRed));
        insideCircleTouchedBgColor = ta.getColor(R.styleable.CircleProgressView_insideCircleTouchedBgColor,getResources().getColor(R.color.colorDeepRed));
        insideRectangleBgColor = ta.getColor(R.styleable.CircleProgressView_insideRectangleBgColor,getResources().getColor(R.color.colorRed));
        tipTextColor = ta.getColor(R.styleable.CircleProgressView_tipTextColor,getResources().getColor(R.color.colorWhite));
        tipTextSize = ta.getDimension(R.styleable.CircleProgressView_tipTextSize,34);
        ta.recycle();

        mPaint = new Paint();
    }

    public void setConnectState(int state){
        this.state = state;
        // 重新绘制View
        this.invalidate();
    }

    public int getConnectState() {
        return state;
    }

    public void setProgressVaule(int progress){
        this.progress = progress;
        // 重新绘制View
        this.invalidate();
    }

    public void setTotalSize(int totalSize){
        this.totalSize = totalSize;
    }

    public void setShowTextTipFlag(boolean isShowTextTip){
        this.isShowTextTip = isShowTextTip;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(listener == null || isDisabled())
            return super.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            isTouched = true;
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            isTouched = false;
            // 松开手时，处理触摸事件
            listener.onViewClick();
        }
        this.invalidate();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 调用setMeasuredDimension
        // 测量View大小
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int widthMeasureSpec) {
        int width = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            // 精度模式
            width = specSize;
        }else {
            // 默认大小
            width = 200;
            // wrap_content
            if(specMode == MeasureSpec.AT_MOST){
                width = Math.min(width,specSize);
            }
        }
        return width;
    }

    private int measureWidth(int heightMeasureSpec) {
        int height = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            // 精度模式
            height = specSize;
        }else {
            // 默认大小
            height = 200;
            // wrap_content
            if(specMode == MeasureSpec.AT_MOST){
                height = Math.min(height,specSize);
            }
        }
        return height;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 当View大小变化时，获取其宽高
        mWidth = getWidth();
        mHeight = getHeight();
        circleX = mWidth/2;
        circleY = mWidth/2;
        radius = mWidth / 2;
        // 设置默认状态
        state = STATE_UNDONE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOutSideCircle(canvas);

        if(STATE_DONE == state){
            drawInternelRectangle(canvas);
        }else{
            if(isTouched){
                drawInternelCircle(canvas,insideCircleTouchedBgColor);
            }else{
                drawInternelCircle(canvas,insideCircleBgColor);
            }
            // 绘制弧形进度条
            if(STATE_DOING == state){
                drawProgressArc(canvas);
            }
        }
    }

    private void drawOutSideCircle(Canvas canvas){
        mPaint.setStrokeWidth(2);
        mPaint.setColor(outsideCircleBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawCircle(circleX,circleY,radius,mPaint);
    }

    private void drawInternelCircle(Canvas canvas,int colorType){
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(colorType);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(circleX,circleY,(float) (radius-radius*0.15),mPaint);
    }

    private void drawInternelRectangle(Canvas canvas){
        mPaint.setStrokeWidth(2);
        mPaint.setColor(insideRectangleBgColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect((float) (mWidth*0.3),(float) (mWidth*0.3),(float)( mWidth-mWidth*0.3)
                ,(float) (mWidth-mWidth*0.3),mPaint);
    }

    private void drawProgressArc(Canvas canvas){
        mPaint.setStrokeWidth((int)(radius * 0.15));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(progressArcBgColor);

        if(progress >= 0){
            if(totalSize == 0)
                return;
            canvas.drawArc(new RectF((float) (radius*0.08),(float) (radius*0.08),2*radius-(float) (radius*0.08),2*radius-(float) (radius*0.08))
                    ,180,(int)(Float.parseFloat(new DecimalFormat("0.00")
                            .format((float)progress/totalSize)) * 360),false,mPaint);
            if(isShowTextTip){
                drawTextTip(canvas,(int)(Float.parseFloat(new DecimalFormat("0.00")
                        .format((float)progress/totalSize)) * 100)+" %");
            }
        }else if(progress == NONE){
            if(isOddNumber){
                canvas.drawArc(new RectF((float) (radius*0.08),(float) (radius*0.08),2*radius-(float) (radius*0.08),2*radius-(float) (radius*0.08))
                        ,180,mSweepAngle,false,mPaint);
                mSweepAngle ++;
                if(mSweepAngle >= 360)
                    isOddNumber = false;
            }else{
                canvas.drawArc(new RectF((float) (radius*0.08),(float) (radius*0.08),2*radius-(float) (radius*0.08),2*radius-(float) (radius*0.08))
                        ,180,-mSweepAngle,false,mPaint);
                mSweepAngle--;
                if(mSweepAngle == 0)
                    isOddNumber = true;
            }
            this.postInvalidateDelayed(5);
        }
    }

    private void drawTextTip(Canvas canvas,String tipText){
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(tipTextSize);
        mPaint.setColor(tipTextColor);
        //Paint.Align.CENTER , x表示字体中心位置；
        // Paint.Align.LEFT ,x表示文本左边位置；
        mPaint.setTextAlign(Paint.Align.CENTER);
        float xCenter = getMeasuredHeight()/2;
        float yBaseLine = (getMeasuredHeight() - mPaint.getFontMetrics().bottom + mPaint.getFontMetrics().top)/2
                -mPaint.getFontMetrics().top;
        canvas.drawText(tipText,xCenter,yBaseLine,mPaint);
    }
}
