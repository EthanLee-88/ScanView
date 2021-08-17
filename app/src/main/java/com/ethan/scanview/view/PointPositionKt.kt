package com.ethan.scanview.view

import android.graphics.Color
import android.graphics.PointF
import java.util.*
import kotlin.math.cos

class PointPositionKt {

    // 信号等级
    private var rank = 0

    // 所能显示的区域半径，这里是默认值
    private var radio = 60

    // 扫描区域中心
    private var centerPoint = PointF(0f, 0f)

    // 实心小圆圆心
    private lateinit var mPoint: PointF
    private var random = Random()

    // 设备名称
    private var userName = ""
    private var colors = intArrayOf(
        Color.parseColor("#FFE10505"),
        Color.parseColor("#FFFF9800"),
        Color.parseColor("#FF9C27B0"),
        Color.parseColor("#FF02188F"),
        Color.parseColor("#FF0431D8")
    )

    public var pointColor = Color.parseColor("#FF9C27B0")

    public fun setRank(mRank: Int): PointPositionKt {
        var team = mRank
        if (mRank > 70) {
            team = 70
        }
        if (mRank < 20) {
            team = 20
        }
        this.rank = team + 20
        return this
    }

    public fun setRadio(radio: Int): PointPositionKt {
        if (radio < 0) return this
        this.radio = radio;
        return this
    }

    public fun setCenterPoint(centerPoint: PointF): PointPositionKt {
        this.centerPoint = centerPoint
        return this
    }

    public fun setPoint(currentDegree : Int) : PointPositionKt {
        mPoint = PointF (0f, 0f)

        // rank是信号等级，这里设置的范围是 20 - 90
        // radio 是可现实的区域半径，也就是扫面区域大圆半径
        // distance 是根据等级 rank和 区域半径算出来的实心小圆到大圆中心处的距离
        var distance = radio * rank / 100
        // 三角函数分别算出View中心点距离目标点的横、纵坐标距离
        var xDistance = distance * cos(currentDegree * 2 * Math.PI / 360)
        var yDistance = distance * Math.sin(currentDegree * 2 * Math.PI / 360)
        // 算出点的横纵坐标
        mPoint.x = (centerPoint.x + xDistance).toFloat();
        mPoint.y = (centerPoint.y + yDistance).toFloat();
        // 算一个随机颜色
        pointColor = colors[random.nextInt(4)];
        return this;
    }

    public fun getPoint() : PointF {
        return this.mPoint
    }
}