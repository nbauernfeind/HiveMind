package hivemind.ui

import hivemind.ants.{Tile, Game}
import java.awt.geom.{Rectangle2D, Line2D}
import java.awt.{Color, Graphics2D}
import java.util.Collections
import scala.collection.JavaConversions._

class BoardRenderer(scale: Int) extends Renderer[Game] {
  val EnemyColours: Array[Color] = Array(Color.RED, Color.CYAN, Color.YELLOW, Color.PINK, Color.ORANGE, Color.GREEN, Color.MAGENTA)
  Collections.shuffle(java.util.Arrays.asList(EnemyColours: _*))

  def draw(t: Game, g: Graphics2D) {
    drawGrid(t, g)
    drawTiles(t.board.water.keys, Color.BLUE, g)
    drawAnts(t.board.myAnts.keys, Color.WHITE, g)
    t.board.enemyAnts.values.groupBy(_.player).foreach {
      case (player, ants) => drawAnts(ants.map(_.tile), EnemyColours(player), g)
    }
  }

  private def drawGrid(t: Game, g: Graphics2D) {
    val rows = t.parameters.rows
    val cols = t.parameters.columns

    val br = new Rectangle2D.Double(0, 0, cols * scale, rows * scale)
    g.setColor(new Color(0xFFCC99)) // Brown
    g.fill(br)

    g.setColor(Color.lightGray)
    for (i <- 0 to cols) {
      val s = Point(i, 0) * scale
      val e = Point(i, rows) * scale
      val line = new Line2D.Double(s.x, s.y, e.x, e.y)
      g.draw(line)
    }

    for (i <- 0 to rows) {
      val s = Point(0, i) * scale
      val e = Point(cols, i) * scale
      val line = new Line2D.Double(s.x, s.y, e.x, e.y)
      g.draw(line)
    }
  }

  private def drawTiles(ws: Iterable[Tile], c: Color, g: Graphics2D) {
    g.setColor(c)
    for (w <- ws) {
      val p = Point(w.column, w.row) * scale
      val r = new Rectangle2D.Double(p.x + 1, p.y + 1, scale - 2, scale - 2)
      g.draw(r)
      g.fill(r)
    }
  }

  private def drawAnts(as: Iterable[Tile], c: Color, g: Graphics2D) {
    for (a <- as) {
      val p = Point(a.column, a.row) * scale
      g.setColor(c)
      g.fillOval(p.x + 1, p.y + 1, scale - 2, scale - 2)
      g.setColor(Color.black)
      g.drawOval(p.x + 1, p.y + 1, scale - 2, scale - 2)
    }
  }
}