package hivemind

import ants.{GameInProgress, Parser, Game}
import java.io.{InputStream, OutputStream, BufferedWriter, OutputStreamWriter}
import io.Source
import hivemind.util.BufferedSource
import javax.swing.JFrame
import ui.BoardRenderer
import java.util.ArrayList
import scala.collection.JavaConversions._
import java.awt.geom.Rectangle2D
import java.awt.{Color, Graphics, Graphics2D}
import java.awt.event._

class HiveMind(in: InputStream = System.in, out: OutputStream = System.out) extends Runnable {

  val source = new BufferedSource(in, Source.DefaultBufSize)
  val writer = new BufferedWriter(new OutputStreamWriter(out))

  def run() {
    try {
      def playNextTurn(game: Game) {
        val newGameState = Parser.parse(writer, source, game.parameters, game.board.water)
        updateBoard(newGameState)
        if (newGameState.gameOver) Unit
        else {
          playNextTurn(newGameState)
        }
      }

      playNextTurn(GameInProgress())
    } catch {
      case t => t.printStackTrace()
    }
  }

  var frame: Frame = null

  def updateBoard(newGameState: Game) {
    if (frame == null) {
      frame = new Frame(8, newGameState)
    } else {
      frame.addGameState(newGameState)
    }
  }
}

class Frame(scale: Int, initialBoard: Game) extends JFrame {
  val turnHeight = 50

  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setSize(initialBoard.parameters.columns * scale + 1, turnHeight + initialBoard.parameters.rows * scale + 1)
  setVisible(true)
  createBufferStrategy(2)

  addKeyListener(new KeyListener() {
    def keyPressed(p1: KeyEvent) {
      p1.getKeyCode match {
        case KeyEvent.VK_RIGHT => currIndex = math.min(currIndex + 1, currBoard.size - 1)
        case KeyEvent.VK_LEFT => currIndex = math.max(currIndex - 1, 0)
        case _ =>
      }
      repaint()
    }

    def keyTyped(p1: KeyEvent) {}
    def keyReleased(p1: KeyEvent) {}
  })

  var mouseClickedInBar = false

  addMouseMotionListener(new MouseMotionListener() {
    def mouseDragged(p1: MouseEvent) {
      if (mouseClickedInBar) {
        val t = (p1.getX * (currBoard.size-1)) / (columns * scale + 1)
        if (t >= 0 && t < currBoard.size) {
          currIndex = t
          repaint()
        }
      }
    }

    def mouseMoved(p1: MouseEvent) {}
  })

  addMouseListener(new MouseListener() {
    def mouseClicked(p1: MouseEvent) {
      if (p1.getY >= 0 && p1.getY < turnHeight) {
	mouseClickedInBar = true
        val t = (p1.getX * (currBoard.size-1)) / (columns * scale + 1)
        if (t >= 0 && t < currBoard.size) {
          currIndex = t
          repaint()
        }
      }
    }

    def mouseReleased(p1: MouseEvent) {
	mouseClickedInBar = false
    }

    def mousePressed(p1: MouseEvent) {}
    def mouseEntered(p1: MouseEvent) {}
    def mouseExited(p1: MouseEvent) {}
  })

  val rows = initialBoard.parameters.rows
  val columns = initialBoard.parameters.columns

  var currIndex = 0
  var currBoard: ArrayList[Game] = new ArrayList[Game](List(initialBoard))
  val boardRenderer = new BoardRenderer(scale)

  def addGameState(g: Game) {
    if (currIndex == currBoard.size - 1)
      currIndex += 1

    currBoard.add(g)

    repaint()
  }

  override def paint(g: Graphics) {
    super.paint(g)
    if (g == null) return
    if (currBoard == null) return

    val x = ((columns * scale + 1) * currIndex) / currBoard.size
    val barWidth = (columns * scale + 1) / currBoard.size

    val r = new Rectangle2D.Double(x, 0, barWidth, turnHeight)

    val graphics = g.asInstanceOf[Graphics2D]
    graphics.setColor(Color.LIGHT_GRAY)
    graphics.fill(r)

    val turnText = (currIndex + 1) + " / " + currBoard.size + "  "
    val ttWidth = g.getFontMetrics.stringWidth(turnText)
    graphics.setColor(Color.BLACK)
    g.drawString(turnText, columns * scale + 1 - ttWidth, g.getFontMetrics.getHeight * 3)

    val boardGraphics = g.create(0, turnHeight, columns * scale + 1, rows * scale + 1).asInstanceOf[Graphics2D]
    try {
      boardRenderer.draw(currBoard(currIndex), boardGraphics)
    } finally {
      boardGraphics.dispose()
    }
  }
}
