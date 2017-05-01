package observatory

import observatory.Extraction.locateTemperatures
import observatory.Visualization._

object Main extends App {

  val black = Color(0, 0, 0)
  val white = Color(255, 255, 255)

  val vec = Vector[(Double, Color)](
    (0, black),
    (1, white)
  )
  val v = 0.5
  val interp = interpolateColor(vec, v)
  println(interp)

}
