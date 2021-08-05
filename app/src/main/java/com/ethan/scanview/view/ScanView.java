package com.ethan.scanview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.ethan.scanview.R;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 蓝牙扫描
 *
 * EthanLee
 */
public class ScanView extends View {
    private Paint circlePaint;
    // 两圆间的半径差
    private int spaceBetweenCircle;
    private Paint scanPaint;
    private Shader mShader;
    private Matrix matrix;
    // 实心小圆
    private Paint pointPaint;
    // 扫描到的设备
    private CopyOnWriteArrayList<PointPosition> pointList = new CopyOnWriteArrayList();
    // 阴影旋转角度
    private int rotationInt = 0;
    // 停止扫描
    private boolean stopScan = false;

    public ScanView(Context context) {
        this(context, null);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRes(context, attrs, 0);
    }

    private void initRes(Context context, AttributeSet attrs, int defStyleAttr) {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setStrokeWidth(2);
        circlePaint.setColor(Color.parseColor("#FF605F5F"));

        scanPaint = new Paint();
        scanPaint.setAntiAlias(true);
        scanPaint.setDither(true);

        pointPaint = new Paint();
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setAntiAlias(true);
        pointPaint.setDither(true);
        pointPaint.setColor(Color.parseColor("#FF3700B3"));

        matrix = new Matrix();
    }

    private void initShader() {
        mShader = new SweepGradient(getWidth() >> 1, getHeight() >> 1,
                new int[]{Color.TRANSPARENT, Color.parseColor("#FF60A8A1")}, null);
        scanPaint.setShader(mShader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int size = Math.min(getMeasureSize(widthMeasureSpec), getMeasureSize(heightMeasureSpec));
        setMeasuredDimension(size, size);
        spaceBetweenCircle = (int) ((dipToPx(size / 2) - 10) / 6);
    }

    private int getMeasureSize(int measureSpec) {
        int measureMode = MeasureSpec.getMode(measureSpec);
        int measureSize = MeasureSpec.getSize(measureSpec);
        if (measureMode == MeasureSpec.EXACTLY) return measureSize;
        if (measureMode == MeasureSpec.AT_MOST) return Math.min(600, measureSize);
        return 600;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("tag", "getMeasuredWidth = " + getMeasuredWidth());
        Log.d("tag", "getMeasuredHeight = " + getMeasuredHeight());
        Log.d("tag", "getWidth = " + getWidth());
        Log.d("tag", "getHeight = " + getHeight());
        initShader();
//        setBackground(getResources().getDrawable(R.mipmap.start));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        for (int i = 0; i <= 6; i++) {
            canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, spaceBetweenCircle * i, circlePaint);
        }
        drawScan(canvas);
    }

    private void drawScan(Canvas canvas) {
        canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, spaceBetweenCircle * 5, scanPaint);
        drawPoint(canvas);
        postScan();
    }

    private void drawPoint(Canvas canvas) {
        for (PointPosition pointPosition : pointList) {
            pointPaint.setColor(pointPosition.pointColor);
            if (pointList.indexOf(pointPosition) == pointList.size() - 1) {
                canvas.drawCircle(pointPosition.getPoint().x, pointPosition.getPoint().y, spaceBetweenCircle >> 1, pointPaint);
            }
            canvas.drawCircle(pointPosition.getPoint().x, pointPosition.getPoint().y, spaceBetweenCircle >> 2, pointPaint);
        }
    }

    private void postScan() {
        if (rotationInt >= 360) {
            rotationInt = 0;
        }
        rotationInt += 2;
        matrix.setRotate(rotationInt, getWidth() >> 1, getHeight() >> 1);// 会先清除之前的状态
//        matrix.postRotate(2f, getWidth() >> 1, getHeight() >> 1); // 状态累加
        mShader.setLocalMatrix(matrix);
        if (!stopScan) invalidate();
    }

    public void setScanStop() {
        if (this.stopScan) return;
        this.stopScan = true;
    }

    public void setStartScan() {
        if (!this.stopScan) return;
        this.stopScan = false;
        invalidate();
    }

    public void addPoint(PointPosition point) {
        if (stopScan) return;
        if (this.pointList.contains(point)) return;
        point.setRadio(spaceBetweenCircle * 6)
                .setCenterPoint(new PointF(getWidth() >> 1, getHeight() >> 1))
                .setPoint(rotationInt);
        this.pointList.add(point);
    }

    public void removePoint(PointPosition point) {
        if (this.pointList.contains(point)) {
            pointList.remove(point);
        }
        invalidate();
    }

    public void clearPoint() {
        if (pointList.size() == 0) return;
        pointList.clear();
        invalidate();
    }

    private float dipToPx(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dip, getResources().getDisplayMetrics());
    }
}
