package project.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import project.data.*
import project.data.Point
import project.data.Point.Companion.computeDistance
import project.logic.G


class GameView : View {

    private lateinit var state: State

    private var offsetX = 0f
    private var offsetY = 0f
    private var raidus = 20f
    private val spaceBetweenDot = 200f
    private val raiduScore = 80f
    private val spaceBetweenScore = 400f
    private val playerTextSize = 100f
    private val textCoordiate = 20f


    private var paintDot = Paint()
    private var paintText = Paint()
    private var paintBackGroud = Paint()
    private var paintTextScore = Paint()


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(
        context: Context, attrs: AttributeSet, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    init {
        reset(false, false)

        paintDot.style = Paint.Style.FILL
        paintDot.isAntiAlias = true
        paintDot.color = Color.WHITE

        paintText.style = Paint.Style.FILL
        paintText.isAntiAlias = true
        paintText.color = Color.WHITE
        paintText.textAlign = Paint.Align.CENTER
        paintText.textSize = textCoordiate

        paintTextScore.style = Paint.Style.FILL
        paintTextScore.isAntiAlias = true
        paintTextScore.color = Color.WHITE
        paintTextScore.textAlign = Paint.Align.CENTER
        paintTextScore.textSize = playerTextSize
        paintTextScore.setTypeface(Typeface.MONOSPACE)


    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        offsetY = (height - (state.numberOfPointY - 1) * spaceBetweenDot) / 2
        offsetX = (width - (state.numberOfPointX - 1) * spaceBetweenDot) / 2
    }

    fun reset(a: Boolean, b: Boolean) {
        state = State(5, 5, Player(Color.BLUE, "a", a), Player(Color.BLACK, "B", b))
        playnext()
        refresh()
    }

    private fun playnext() {
        if (state.currentPlayer.isCpu) {
            G.handler.postDelayed({
                aiEasy();
            }, 1000)
        }
    }

    private fun aiEasy() {
        if (isGameFinish()) {
            return
        }
        val r = (Math.random() * state.avaibleMoves.size).toInt()
        connectLine(state.avaibleMoves[r])

    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawLines(canvas)
        drawPoints(canvas)
        drawBoxes(canvas)
        showPlayerScore(canvas!!)
        if (isGameFinish()) {
            showWinnner(canvas)
        }
    }

    private fun drawBoxes(canvas: Canvas?) {
        for (i in state.boxes) {
            drawHouse(canvas, i)
        }

    }

    private fun drawLines(canvas: Canvas?) {
        for (i in state.lines) {
            drawLine(canvas, i)
        }
    }

    private fun drawPoints(canvas: Canvas?) {
        forEachPoint { i, j ->
            val p = Point(i, j)
            drawDot(canvas, p)
            drawDotCordinate(canvas, p)
        }
    }

    private fun showWinnner(canvas: Canvas) {
        val message = whoWin()
        canvas.drawText(message, (width / 2).toFloat(), (height - 100).toFloat(), paintTextScore)
    }

    private fun whoWin(): String {
        var max = Int.MIN_VALUE
        var player = ""
        for (i in state.players) {
            if (i.score > max) {
                max = i.score
                player = i.name
            }
        }
        return makeMessage(player)
    }

    private fun makeMessage(s: String): String = "$s is winner"

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (canNotTouch()) {
            return true
        }
        val comparePoints = arrayListOf<Compare>()
        forEachPoint { i, j ->
            val p = Point(i, j)
            val result = computeDistance(p.getX(), p.getY(), event.x, event.y)
            comparePoints.add(Compare(result, Point(i, j)))
        }
        comparePoints.sortBy { point -> point.result }

        val l = Line(comparePoints[0].point, comparePoints[1].point)

        if (comparePoints[0].result <= spaceBetweenDot / 1.75 && !state.lines.contains(l)) {
            //touch validate
            connectLine(l)

        }

        return super.onTouchEvent(event)
    }

    private fun canNotTouch(): Boolean = isGameFinish() || state.currentPlayer.isCpu

    private fun refresh() = invalidate()

    private fun connectLine(l: Line) {
        l.paint = Line.createPaintLine(state.currentPlayer.color)
        state.lines.add(l)
        state.avaibleMoves.remove(l)
        syncCurrentPlayer(l)
        refresh()
        playnext()

    }

    fun syncCurrentPlayer(l: Line) {
        if (checkHouseCreatedAndCanChangeCurrentPlayer(l)) {
            changeCurrentPlayer()
        }
    }

    fun checkHouseCreatedAndCanChangeCurrentPlayer(l: Line): Boolean {

        val a = Box.listOfBoxCreated(state.lines, l, state.numberOfPointX, state.numberOfPointY)
        if (a.size != 0) {
            for (i in a) {
                state.currentPlayer.score++
                i.paint = Box.createdPaintBox(state.currentPlayer.color)
            }
            state.boxes.addAll(a)
            return false
        }
        return true
    }

    private fun changeCurrentPlayer() {
        val index = (state.players.indexOf(state.currentPlayer) + 1) % state.players.size
        state.currentPlayer = state.players[index]
    }

    private fun isGameFinish(): Boolean = state.boxes.size >= (state.numberOfPointX - 1) * (state.numberOfPointY - 1)


    private fun forEachPoint(a: (i: Int, j: Int) -> Unit) {
        for (i in 0 until state.numberOfPointX) {
            for (j in 0 until state.numberOfPointY) {
                a(i, j)
            }
        }
    }

    private fun drawDot(canvas: Canvas?, point: Point) =
        canvas!!.drawCircle(point.getX(), point.getY(), raidus, paintDot)

    private fun drawLine(canvas: Canvas?, line: Line) =
        canvas!!.drawLine(line.point1.getX(), line.point1.getY(), line.point2.getX(), line.point2.getY(), line.paint)

    private fun drawDotCordinate(canvas: Canvas?, point: Point) =
        canvas!!.drawText("${point.i}:${point.j}", point.getX(), point.getY() + 40, paintText)

    private fun drawHouse(canvas: Canvas?, box: Box) = canvas!!.drawCircle(
        box.begingPoint.getX() + spaceBetweenDot / 2,
        box.begingPoint.getY() + spaceBetweenDot / 2,
        raidus * 4,
        box.paint
    )

    private fun Point.getX() = computPointX(offsetX, raidus, spaceBetweenDot)

    private fun Point.getY() = computPointY(offsetY, raidus, spaceBetweenDot)

    private fun showPlayerScore(canvas: Canvas) {
        val baseX = (width - spaceBetweenScore * state.players.lastIndex) / 2
        val baseY = raiduScore

        for (i in 0 until state.players.size) {
            val player = state.players[i]
            val b = Rect()
            paintTextScore.getTextBounds(player.score.toString(), 0, player.score.toString().length, b)
            val cy = baseY + b.height() / 2
            val cx = baseX + i * spaceBetweenScore

            canvas.drawCircle(cx, baseY, raiduScore, player.createdPiantPlayerShowScore())
            //for score
            canvas.drawText(player.score.toString(), cx, cy, paintTextScore)
            //for playet
            canvas.drawText(player.name.toString(), cx, cy + raiduScore + b.height() / 2 + 10, paintTextScore)

        }


    }
}


