package observatory

import observatory.Extraction.locateTemperatures
import observatory.Visualization.{distance, calculatePrediction}

object Main extends App {

  val tallinnLoc = Location(59.4370, 24.7536)
  val rigaLoc = Location(56.9496, 24.1052)
  val vilniusLoc = Location(54.6872, 25.2797)

  val known = List(
    (tallinnLoc, 20.0),
    (vilniusLoc, 10.0)
  )

  val prediction = calculatePrediction(known = known, forLocation = rigaLoc)
  println(prediction)

  val prediction2 = calculatePrediction(known = known, forLocation = rigaLoc, p = 2.5)
  println(prediction)

}
