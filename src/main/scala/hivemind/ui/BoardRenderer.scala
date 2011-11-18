package hivemind.ui

import hivemind.ants.{Tile, Game}
import java.awt.geom.{Rectangle2D, Line2D}
import java.util.Collections
import java.awt.{AlphaComposite, Color, Graphics2D}

class BoardRenderer(scale: Int) extends Renderer[Game] {
  val EnemyColours = java.util.Arrays.asList(Color.RED, Color.CYAN, Color.YELLOW, Color.PINK, Color.ORANGE, Color.GREEN, Color.MAGENTA)
  Collections.shuffle(EnemyColours)

  def draw(t: Game, g: Graphics2D) {
    drawGrid(t, g)
    drawTiles(t.board.water.keys, Color.BLUE, g)

    // Draw Hills
    drawTiles(t.board.myHills.keys, Color.WHITE, g, border = true)
    t.board.enemyHills.values.groupBy(_.player).foreach {
      case (player, hills) => drawTiles(hills.map(_.tile), EnemyColours.get(player), g, border = true)
    }

    // Draw Food
    drawTiles(t.board.food.keys, new Color(0xCC9966), g, border = true)

    // Draw Ants Last
    drawCircles(t.board.myAnts.keys, Color.WHITE, g)
    t.board.enemyAnts.values.groupBy(_.player).foreach {
      case (player, ants) => drawCircles(ants.map(_.tile), EnemyColours.get(player), g)
    }

    // Draw Fog of War
    g.setColor(Color.BLACK)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f))
    for (j <- 0 to t.parameters.rows) {
      var start = 0
      for (i <- 0 to t.parameters.columns) {
        val tile = Tile(i,j)
        val visible = t.board.myAnts.values.find(_.tile.distanceTo(tile) <= t.parameters.viewRadius).isDefined
        if (visible) {
          if (i - start > 0) {
            val p = Point(start, j) * scale
            val r = new Rectangle2D.Double(p.x,p.y,scale*(i-start),scale)
            g.fill(r)
          }
          start = i + 1
        }
      }
      if (start < t.parameters.columns + 1) {
        val p = Point(start, j) * scale
        val r = new Rectangle2D.Double(p.x,p.y,scale*(t.parameters.columns + 1 - start),scale)
        g.fill(r)
      }
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

  private def drawTiles(ws: Iterable[Tile], c: Color, g: Graphics2D, border: Boolean = false, alpha: Double = 1.0) {
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha.floatValue))

    for (w <- ws) {
      val p = Point(w.column, w.row) * scale
      val r = new Rectangle2D.Double(p.x + 1, p.y + 1, scale - 2, scale - 2)
      g.setColor(c)
      g.fill(r)
      if (border) {
        g.setColor(Color.BLACK)
        g.draw(r)
      }
    }
  }

  private def drawCircles(as: Iterable[Tile], c: Color, g: Graphics2D) {
    for (a <- as) {
      val p = Point(a.column, a.row) * scale
      g.setColor(c)
      g.fillOval(p.x + 1, p.y + 1, scale - 2, scale - 2)
      g.setColor(Color.black)
      g.drawOval(p.x + 1, p.y + 1, scale - 2, scale - 2)
    }
  }
}