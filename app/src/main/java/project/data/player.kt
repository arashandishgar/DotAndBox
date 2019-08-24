package project.data

import android.graphics.Paint

data class Player( val color: Int, val name: String,val isCpu:Boolean=false) {
    var score=0

    fun createdPiantPlayerShowScore(): Paint {
        val paintCircleScore = Paint()
        paintCircleScore.style = Paint.Style.FILL
        paintCircleScore.isAntiAlias = true
        paintCircleScore.color = color
        return paintCircleScore
    }
}