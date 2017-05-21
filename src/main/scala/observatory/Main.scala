package observatory

import observatory.Extraction.locateTemperatures
import observatory.Visualization._

object Main extends App {
  private val deviationColors: Iterable[(Double, Color)] = Vector(
    (7d,  Color(0,    0,    0)),
    (4d,  Color(255,  0,    0)),
    (2d,  Color(255,  255,  0)),
    (0d,  Color(255,  255,  255)),
    (-2d, Color(0,    255,  255)),
    (-7d, Color(0,    0,    255))
  )
}
