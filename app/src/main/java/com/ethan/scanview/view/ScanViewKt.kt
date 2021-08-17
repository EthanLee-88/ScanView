package com.ethan.scanview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.View.MeasureSpec.AT_MOST
import java.util.concurrent.CopyOnWriteArrayList

class ScanViewKt : View {
    // 同心圆画笔
    private lateinit var paint: Paint  // 注释 1， lateinit关键词 ：可延迟赋值。 var关键词 ：可变变量声明（val 则类似于 java的 final）

    // 画扫描圆画笔
    private lateinit var scanPaint: Paint  // 注释 2，变量名在前，类型在后，中间引号

    // 设备点画笔
    private lateinit var pointPaint: Paint  // 注释 3，后面分号可以省略，但必须换行，否则下一条语句报错

    // 同心圆之间的间距
    private var spaceIntCircles: Int = 0

    // 扫描圆的阴影
    private lateinit var scanShader: Shader

    // 扫描圆阴影矩阵
    private var scanMatrix: Matrix = Matrix() // 注释 4，对象创建不用 java的 new关键词了

    // 旋转角度
    private var rotation: Int = 0

    // 扫描结果
    private var pointList: CopyOnWriteArrayList<PointPositionKt> = CopyOnWriteArrayList()

    // 停止扫描
    private var stopScan = false

    constructor(context: Context) : this(context, null) // 注释 5，构造器关键词 constructor

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    ) // 注释 6，构造器调用的 this、super放在形参表和方法体之间，加以引号

    constructor(context: Context, attrs: AttributeSet?, style: Int) : super(context, attrs, 0) {
        init()
    }

    private fun init() {   // 注释 7， 方法用关键字 fun修饰
        paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = Color.parseColor("#FFF10404")
        paint.isDither = true
        paint.isAntiAlias = true
        paint.strokeWidth = 2f

        scanPaint = Paint()
        scanPaint.isDither = true
        scanPaint.isAntiAlias = true

        pointPaint = Paint()
        pointPaint.style = Paint.Style.FILL
        pointPaint.isDither = true
        pointPaint.isAntiAlias = true
        pointPaint.color = Color.parseColor("#FF3700B3")
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {  //注释 8， 重写的方法声明使用关键字 override替换注解
        var width = getMeasureSize(widthMeasureSpec)
        var height = getMeasureSize(heightMeasureSpec)
        var size = Math.min(width, height)
        setMeasuredDimension(size, size)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        spaceIntCircles = width / 12 - width / 120
        scanShader = SweepGradient(
            (width / 2).toFloat(), (height / 2).toFloat(),
            intArrayOf(Color.TRANSPARENT, Color.GRAY), floatArrayOf(0.1f, 0.9f)
        )
        scanPaint.shader = scanShader
        scanShader.setLocalMatrix(scanMatrix)
    }

    private fun getMeasureSize(measureSpec: Int): Int {
        var mode = MeasureSpec.getMode(measureSpec)
        var size = MeasureSpec.getSize(measureSpec)
        if (mode == AT_MOST) return dipToPx(18f).toInt()  // 注释 9，类型强转使用 to..()方法
        return size
    }

    private fun dipToPx(dip: Float): Float {  // 注释 10，方法返回值类型方法形参列表后，以引号隔开
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dip, resources.displayMetrics)
    }

    override fun onDraw(canvas: Canvas) {
        // 画 6个圈
        for (i in 0..6) {  // 注释 11，for循环的其中一种形式： (i in 0..6)，0..6 表示区间。这里包含边界值。可用  (i in 0 until 6)去除有边界
            canvas.drawCircle(
                (height / 2).toFloat(), (height / 2).toFloat(),
                (i * spaceIntCircles).toFloat(), paint
            )
        }
        // 画带阴影的扫描圆
        canvas.drawCircle(
            (height / 2).toFloat(), (height / 2).toFloat(),
            (5 * spaceIntCircles).toFloat(), scanPaint
        )
        drawPoint(canvas)
        setRotation()
    }

    /***
     * 画所有的点 （设备）
     * ***/
    private fun drawPoint(canvas: Canvas) {
        for (item in pointList) { // 注释 12， item关键字的 for循环迭代遍历集合
            pointPaint.color = item.pointColor
            if (pointList.indexOf(item) == pointList.size - 1) {
                canvas.drawCircle(
                    item.getPoint().x, item.getPoint().y,
                    (spaceIntCircles / 2).toFloat(), pointPaint
                )
                continue
            }
            canvas.drawCircle(
                item.getPoint().x, item.getPoint().y,
                (spaceIntCircles / 4).toFloat(), pointPaint
            )
        }
    }
    /***
     * 改变旋转角度
     * ***/
    private fun setRotation() {
        if (rotation >= 360) rotation = 0
        rotation += 2
        scanMatrix.setRotate(rotation.toFloat(), (width / 2).toFloat(), (height / 2).toFloat())
        scanShader.setLocalMatrix(scanMatrix)
        if (!stopScan) invalidate()
    }

    /***
     *停止扫描
     * ***/
    fun setScanStop() {
        if (this.stopScan) return
        this.stopScan = true
    }

    /***
     * 开始扫描
     * ***/
    fun setStartScan() {
        if (!this.stopScan) return
        this.stopScan = false
        invalidate()
    }

    /***
     * 添加设备
     * ***/
    fun addPoint(point: PointPositionKt) {
        if (stopScan) return
        if (this.pointList.contains(point)) return
        point.setRadio(spaceIntCircles * 6)
            .setCenterPoint(PointF((width / 2).toFloat(), (height / 2).toFloat()))
            .setPoint(rotation)
        pointList.add(point)
    }

    /***
     * 移除某个设备
     * ***/
    fun removePoint(point: PointPositionKt) {
        if (pointList.contains(point)) pointList.remove(point)
        invalidate()
    }

    /***
     * 清除界面设备
     * ***/
    fun clearPoint() {
        if (pointList.size == 0) return
        pointList.clear()
        invalidate()
    }
}