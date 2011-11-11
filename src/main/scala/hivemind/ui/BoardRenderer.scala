package hivemind.ui

import hivemind.ants.{Tile, Game}
import java.awt.geom.{Rectangle2D, Line2D}
import java.awt.{Color, Graphics2D}

class BoardRenderer(scale: Int) extends Renderer[Game] {
  def draw(t: Game, g: Graphics2D) {
    drawGrid(t, g)
    drawWater(t.board.water.keys, g)
    drawMyAnts(t.board.myAnts.keys, g)
  }

  private def drawGrid(t: Game, g: Graphics2D) {
    val rows = t.parameters.rows
    val cols = t.parameters.columns

    val br = new Rectangle2D.Double(0, 0, cols * scale, rows * scale)
    g.setColor(new Color(0x964514)) // Brown
    g.draw(br)

    g.setColor(Color.lightGray)
    for (i <- 0 to cols) {
      val s = Point(0, i) * scale
      val e = Point(rows, i) * scale
      val line = new Line2D.Double(s.x, s.y, e.x, e.y)
      g.draw(line)
    }

    for (i <- 0 to rows) {
      val s = Point(i, 0) * scale
      val e = Point(i, cols) * scale
      val line = new Line2D.Double(s.x, s.y, e.x, e.y)
      g.draw(line)
    }
  }

  private def drawWater(ws: Iterable[Tile], g: Graphics2D) {
    g.setColor(Color.blue)
    for (w <- ws) {
      val p = Point(w.column, w.row) * scale
      val r = new Rectangle2D.Double(p.x + 1, p.y + 1, scale - 2, scale - 2)
      g.draw(r)
    }
  }

  private def drawMyAnts(as: Iterable[Tile], g: Graphics2D) {
    for (a <- as) {
      val p = Point(a.column, a.row) * scale
      g.setColor(Color.white)
      g.fillOval(p.x + 1, p.y + 1, scale - 2, scale - 2)
      g.setColor(Color.black)
      g.drawOval(p.x + 1, p.y + 1, scale - 2, scale - 2)
    }
  }
}