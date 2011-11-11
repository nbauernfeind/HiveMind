package hivemind.ui

import java.awt.Graphics2D

case class Point(row: Int,  col: Int) {
  def *(s: Int): Point = Point(row*s, col*s)

  def x: Int = col
  def y: Int = row
}

trait Renderer[T] {
  def draw(t: T, g: Graphics2D)
}