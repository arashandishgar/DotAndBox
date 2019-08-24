package project.data

data class Point(val i: Int, val j: Int) {
    companion object {
        fun computeDistance(x1: Float, y1: Float, x2: Float, y2: Float) = Math.sqrt(Math.pow((x1 - x2).toDouble(), 2.0) + Math.pow((y1 - y2).toDouble(), 2.0)).toFloat()
    }

    fun computPointX(offsetX: Float, raidus: Float, spaceBetweenDot: Float) = offsetX + raidus + i * spaceBetweenDot
    fun computPointY(offsetY: Float, raidus: Float, spaceBetweenDot: Float) = offsetY + raidus + j * spaceBetweenDot


    override fun toString(): String = "${this.i}:${this.j}"

}

data class Compare(val result: Float, val point: Point)

