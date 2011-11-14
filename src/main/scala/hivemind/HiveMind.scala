package hivemind

import ants.{GameInProgress, Parser, Game}
import java.io.{InputStream, OutputStream, BufferedWriter, OutputStreamWriter}
import io.Source
import hivemind.util.BufferedSource
import javax.swing.JFrame
import ui.BoardRenderer
import java.awt.{Graphics, Graphics2D}

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
      frame.setGameState(newGameState)
    }
  }
}

class Frame(scale: Int, initialBoard: Game) extends JFrame {
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setSize(initialBoard.parameters.columns * scale, initialBoard.parameters.rows * scale)
  setVisible(true)
  createBufferStrategy(2)

  var currBoard: Game = initialBoard
  val boardRenderer = new BoardRenderer(scale)

  def setGameState(g: Game) {
    currBoard = g
    repaint()
  }

  override def paint(g: Graphics) {
    super.paint(g)
    if (g == null) return
    if (currBoard == null) return

    val graphics = g.asInstanceOf[Graphics2D]
    boardRenderer.draw(currBoard, graphics)
  }
}
