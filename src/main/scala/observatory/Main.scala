package observatory

import observatory.Extraction.locateTemperatures
import observatory.Visualization.{getBounds, lerp}

object Main extends App {

//  val list = Vector[Tuple2[Double, Color]](
//    (0.2, Color(1, 2, 0)),
//    (1.1, Color(1, 2, 3)),
//    (1.6, Color(1, 2, 3))
//  )
//  val v = 0.1
//  val bounds = getBounds(list, v)
//  println(f"${bounds._1}, ${bounds._2}")

  println(lerp(0, 255, 0.1))

}
