package project.data

import android.graphics.Paint

data class Box(val begingPoint: Point) {
    public lateinit var paint: Paint

    companion object {
        fun createdPaintBox(color: Int): Paint {
            val paint = Paint()
            paint.style = Paint.Style.FILL
            paint.isAntiAlias = true
            paint.color = color
            return paint
        }

        private val boxes = ArrayList<Box>()

        fun listOfBoxCreated(
            lines: ArrayList<Line>,
            l: Line,
            numberOfPointX: Int,
            numberOfPointY: Int
        ): ArrayList<Box> {
            boxes.clear()
            if (l.isVertical) {
                when (l.point1.i) {
                    0 -> isLeftesttVerticalMakebox(lines, l)
                    numberOfPointX - 1 -> isRightestVerticalMakebox(lines, l)
                    else -> isMiddleVerticalMakebox(lines, l)

                }
            } else {
                when (l.point1.j) {
                    0 -> isToppestLineHorizentalMakebox(lines, l)

                    numberOfPointY - 1 -> isBottomtLineHorizentalMakebox(lines, l)

                    else -> isMiddleLineHorizentalMakebox(lines, l)


                }
            }
            return boxes
        }

        private fun isMiddleLineHorizentalMakebox(lines: ArrayList<Line>, l: Line): Boolean =
            checkBottom(lines, l) or checkTop(lines, l)

        private fun isBottomtLineHorizentalMakebox(lines: ArrayList<Line>, l: Line): Boolean = checkTop(lines, l)

        private fun isToppestLineHorizentalMakebox(lines: ArrayList<Line>, l: Line): Boolean = checkBottom(lines, l)


        private fun checkTop(lines: ArrayList<Line>, l: Line): Boolean {
            val p1 = Point(l.point1.i, l.point1.j - 1)
            val p2 = Point(l.point2.i, l.point2.j - 1)
            val lineBottom = Line(p1, p2)
            return checkBottom(lines, lineBottom)
        }


        private fun checkBottom(lines: ArrayList<Line>, l: Line): Boolean {
            val p2 = Point(l.point1.i, l.point1.j + 1)
            return checkRigh(lines, Line(l.point1, p2))

        }

        private fun isMiddleVerticalMakebox(lines: ArrayList<Line>, l: Line): Boolean =
            checkRigh(lines, l) or checkLeft(lines, l)


        private fun isLeftesttVerticalMakebox(lines: ArrayList<Line>, l: Line): Boolean = checkRigh(lines, l)

        private fun isRightestVerticalMakebox(lines: ArrayList<Line>, l: Line): Boolean = checkLeft(lines, l)


        private fun checkLeft(lines: ArrayList<Line>, l: Line): Boolean {
            val p1 = Point(l.point1.i - 1, l.point1.j)
            val p2 = Point(l.point2.i - 1, l.point2.j)
            val lineLeft = Line(p1, p2)
            return checkRigh(lines, lineLeft)
        }

        private fun checkRigh(lines: ArrayList<Line>, lineLeft: Line): Boolean {
            val p1 = Point(lineLeft.point1.i + 1, lineLeft.point1.j)
            val p2 = Point(lineLeft.point2.i + 1, lineLeft.point2.j)
            val lineTop = Line(lineLeft.point1, p1)
            val lineBottom = Line(lineLeft.point2, p2)
            val lineRight = Line(p1, p2)

            if (lines.contains(lineBottom) && lines.contains(lineTop) && lines.contains(lineRight) && lines.contains(lineLeft)) {
                boxes.add(Box(lineLeft.point1))
                return true
            }
            return false
        }

    }

}

