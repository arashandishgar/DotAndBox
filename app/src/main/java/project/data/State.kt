package project.data

class State(val numberOfPointX: Int, val numberOfPointY: Int, vararg val players: Player) {
    val lines = arrayListOf<Line>()
    val boxes = arrayListOf<Box>()
    var currentPlayer: Player
    val avaibleMoves=ArrayList<Line>()

    init {
        currentPlayer = players[0]
        for(i in 0..numberOfPointX-1){
            for(j in 0 .. numberOfPointY-2){
                avaibleMoves.add(Line(Point(i,j),Point(i,j+1)))
            }
        }

        for(i in 0..numberOfPointX-2){
            for(j in 0 .. numberOfPointY-1){
                avaibleMoves.add(Line(Point(i,j),Point(i+1,j)))
            }
        }
    }

}


