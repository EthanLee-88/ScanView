package com.ethan.scanview.view;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

import java.util.Random;

/**
 * 设备信息
 *
 * EthanLee
 */
public class PointPosition {
    // 信号等级
    private int rank = 0;
    // 所能显示的区域半径，这里是默认值
    private int radio = 60;
    // 扫描区域中心
    private PointF centerPoint = new PointF(0f, 0f);
    // 实心小圆圆心
    private PointF mPoint;
    private Random random = new Random();
    // 设备名称
    private String userName = "";
    private int[] colors = {Color.parseColor("#FFE10505"),
            Color.parseColor("#FFFF9800"),
            Color.parseColor("#FF9C27B0"),
            Color.parseColor("#FF02188F"),
            Color.parseColor("#FF0431D8")};
    public int pointColor;

    public int getRank() {
        return rank;
    }

    public int getRadio() {
        return radio;
    }

    public PointF getCenterPoint() {
        return centerPoint;
    }

    public PointPosition setRank(int mRank) {
        if (mRank > 70) {
            mRank = 70;
        }
        if (mRank < 20) {
            mRank = 20;
        }
        this.rank = mRank + 20;
        return this;
    }

    public PointPosition setRadio(int radio) {
        if (radio < 0) return this;
        this.radio = radio;
        return this;
    }

    public PointPosition setCenterPoint(PointF centerPoint) {
        if (centerPoint == null) return this;
        this.centerPoint = centerPoint;
        return this;
    }

    public PointPosition setPoint(int currentDegree) {
        if (mPoint == null) mPoint = new PointF(0f, 0f);

        // rank是信号等级，这里设置的范围是 20 - 90
        // radio 是可现实的区域半径，也就是扫面区域大圆半径
        // distance 是根据等级 rank和 区域半径算出来的实心小圆到大圆中心处的距离
        float distance = radio * rank / 100;
        // 三角函数分别算出View中心点距离目标点的横、纵坐标距离
        float xDistance = (float) (distance * Math.cos(currentDegree * 2 * Math.PI / 360));
        float yDistance = (float) (distance * Math.sin(currentDegree * 2 * Math.PI / 360));
        // 算出点的横纵坐标
        mPoint.x = centerPoint.x + xDistance;
        mPoint.y = centerPoint.y + yDistance;
        // 算一个随机颜色
        pointColor = colors[random.nextInt(4)];
        return this;

//        float xRandomFactor = (random.nextFloat() * 2 - 1);
//        float yRandomFactor = (random.nextFloat() * 2 - 1);
//        float xDistance = xRandomFactor * distance;
//        double yDistance = Math.sqrt(distance * distance - xDistance * xDistance);
//        mPoint.y = (float) (centerPoint.y + yDistance * (yRandomFactor / Math.abs(yRandomFactor)));
//        mPoint.x = centerPoint.x + xDistance;
//        return this;
    }

    public PointF getPoint() {
        return this.mPoint;
    }
}
