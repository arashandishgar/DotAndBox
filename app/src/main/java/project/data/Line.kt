package project.data

import android.graphics.Paint


data class Line(var point1: Point, var point2: Point) {
    lateinit var paint: Paint


    var isVertical = false

    init {
        val deltaI = point1.i - point2.i
        val deltaJ = point1.j - point2.j
        isVertical = checkIsVertical(deltaI)
        standardLine(deltaI, deltaJ)

    }

    companion object {
        fun createPaintLine(color: Int) = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            this.color = color
            strokeWidth = 20f

        }
    }


    fun checkIsVertical(deltaI: Int) = deltaI == 0

    private fun standardLine(deltaI: Int, deltaJ: Int) {
        if (deltaJ > 0 || deltaI > 0) {
            point1 = point2.also {
                point2 = point1

            }
        }
    }


}



