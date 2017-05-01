package observatory

import observatory.Extraction.locateTemperatures
import observatory.Visualization.distance

object Main extends App {

//  val x = locateTemperatures(year = 2005, stationsFile = "/stations.csv", temperaturesFile = "/2005.csv")
//  println("Length: " + x.size)
  val rigaLoc = Location(56.9496, 24.1052)
  val vilniusLoc = Location(54.6872, 25.2797)
  val dist = distance(rigaLoc, vilniusLoc)
  println(dist)

}
