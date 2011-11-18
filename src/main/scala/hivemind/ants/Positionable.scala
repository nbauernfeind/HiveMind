package hivemind.ants

case class Tile(column: Int, row: Int) {
  def distanceTo(t: Tile): Int = {
    val dx = t.column - column
    val dy = t.row - row
    dx * dx + dy * dy
  }
}

sealed trait Positionable {
  def tile: Tile
}

case class MyAnt(tile: Tile) extends Positionable

case class EnemyAnt(tile: Tile, player: Int) extends Positionable

case class Corpse(tile: Tile) extends Positionable

case class Food(tile: Tile) extends Positionable

case class Water(tile: Tile) extends Positionable

case class MyHill(tile: Tile) extends Positionable

case class EnemyHill(tile: Tile, player: Int) extends Positionable
