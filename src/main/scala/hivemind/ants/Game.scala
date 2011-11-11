package hivemind.ants

case class GameParameters(loadTime: Int = 2000, turnTime: Int = 2000, rows: Int = 20, columns: Int = 20, seed: Int = 0,
                          turns: Int = 500, viewRadius: Int = 93, attackRadius: Int = 6, spawnRadius: Int = 6)

case class GameInProgress(turn: Int = 0, parameters: GameParameters = GameParameters(), board: Board = Board()) extends Game {
  val gameOver = false
  def including[P <: Positionable](positionable: P) = this.copy(board = this.board including positionable)
  def including(p: Positionable*): GameInProgress = p.foldLeft(this){(game, positionable) => game.including(positionable)}
}
case class GameOver(turn: Int = 0, parameters: GameParameters = GameParameters(), board: Board = Board()) extends Game {
  val gameOver = true
}

sealed trait Game {
  def turn: Int
  def parameters: GameParameters
  def board: Board
  def gameOver: Boolean
}

