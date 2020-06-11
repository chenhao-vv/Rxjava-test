package com.vivo.chmusicdemo.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import com.vivo.chmusicdemo.R;

import androidx.appcompat.widget.AppCompatImageView;

public class RoundImageView extends AppCompatImageView {

    private static final String TAG = "RoundImageView";

    //状态保存与恢复
    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    private static final String STATE_BORDE_RADIUS = "state_border_radius";

    //图片类型，圆形或圆角
    private int mType;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;

    //圆角大小的默认值
    private static final int BORDER_RADIUS_DEFAULT = 10;
    //圆角大小
    private int mBorderRadius;

    //绘图的paint
    private Paint mBitmapPaint;
    //圆角的半径
    private int mRadius;
    //3*3矩阵，用于缩小放大
    private Matrix mMatrix;
    //渲染图像，使用图像为绘制图形着色
    private BitmapShader mBitmapShader;
    //view的宽度
    private int mWidth;
    private RectF mRoundRect;

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        mBorderRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_borderRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BORDER_RADIUS_DEFAULT, getResources().getDisplayMetrics()));
        mType = a.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);//默认是circle
        a.recycle();
    }

    @Override
    public void onMeasure(int width, int height) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(width, height);

        //如果是圆形，则强制改变view的宽高一致，以小值为准
        if(mType == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }
    }

    //初始化bitmapshader
    private void setUpshader() {
        Drawable drawable = getDrawable();
        if(drawable == null) {
            return;
        }
        Bitmap bmp = drawableToBitmap(drawable);
        if(bmp != null) {
            //将bmp作为着色器，就是在指定区域绘制bmp
            mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            float scale = 1.0f;
            if(mType == TYPE_CIRCLE) {
                //拿到bitmap的宽高
                int size = Math.min(bmp.getWidth(), bmp.getHeight());
                scale = mWidth * 1.0f / size;
            } else if(mType == TYPE_ROUND) {
                //如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例
                //缩放后的图片宽高，一定要大于view的宽高。这里取最大值
                scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight() * 1.0f / bmp.getHeight());
            }
            //shader的变换矩阵,我们这里主要用于放大和缩小
            mMatrix.setScale(scale, scale);
            //设置变换矩阵
            mBitmapShader.setLocalMatrix(mMatrix);
            //设置shader
            mBitmapPaint.setShader(mBitmapShader);
        } else {
            Log.d(TAG, "bmp == null");
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if(w <= 0 || h <= 0) {
            return null;
        }
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(getDrawable() == null) {
            return;
        }
        setUpshader();
        if(mType == TYPE_CIRCLE) {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
        } else if (mType == TYPE_ROUND) {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius, mBitmapPaint);
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldW, int oldH) {
        super.onSizeChanged(width, height, oldW, oldH);

        //圆角图片范围
        if(mType == TYPE_ROUND) {
            mRoundRect = new RectF(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_TYPE, mType);
        bundle.putInt(STATE_BORDE_RADIUS, mBorderRadius);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            Bundle bundle = (Bundle)((Bundle) ((Bundle) state).getParcelable(STATE_INSTANCE));
            this.mType = bundle.getInt(STATE_TYPE);
            this.mBorderRadius = bundle.getInt(STATE_BORDE_RADIUS);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    public void setmBorderRadius(int borderRadius) {
        int pxval = dp2px(borderRadius);
        if(this.mBorderRadius != pxval) {
            this.mBorderRadius = pxval;
            invalidate();
        }
    }

    public void setmType(int type) {
        if(this.mType != type) {
            this.mType = type;
            if(this.mType != TYPE_ROUND && this.mType != TYPE_CIRCLE) {
                this.mType = TYPE_CIRCLE;
            }
            requestLayout();
        }
    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }
}
