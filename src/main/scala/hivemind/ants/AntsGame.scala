package hivemind.ants

import java.io.{InputStream, OutputStream, BufferedWriter, OutputStreamWriter}
import io.Source
import hivemind.util.BufferedSource

class AntsGame(in: InputStream = System.in, out: OutputStream = System.out) {

  val source = new BufferedSource(in, Source.DefaultBufSize)
  val writer = new BufferedWriter(new OutputStreamWriter(out))

  def run() {
    try {

      def playNextTurn(game: Game) {
        val newGameState = Parser.parse(source, game.parameters, game.board.water)
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

}
