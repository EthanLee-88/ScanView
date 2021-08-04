package com.ethan.scanview.view;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

import java.util.Random;

public class PointPosition {
    private int rank = 0;
    private int radio = 60;
    private PointF centerPoint = new PointF(0f, 0f);
    private PointF mPoint;
    private Random random = new Random();
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

        float distance = radio * rank / 100;

        float xDistance = (float) (distance * Math.cos(currentDegree * 2 * Math.PI / 360));
        float yDistance = (float) (distance * Math.sin(currentDegree * 2 * Math.PI / 360));
        mPoint.x = centerPoint.x + xDistance;
        mPoint.y = centerPoint.y + yDistance;
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
