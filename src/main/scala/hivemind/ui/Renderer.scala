package hivemind.ui

import java.awt.Graphics2D

case class Point(col: Int,  row: Int) {
  def *(s: Int): Point = Point(col*s, row*s)

  def x: Int = col
  def y: Int = row
}

trait Renderer[T] {
  def draw(t: T, g: Graphics2D)
}