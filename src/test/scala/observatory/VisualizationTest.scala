package observatory

import observatory.Visualization.{degreesToRadians, distance}

import math.{Pi, round}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers {

  test("Degrees to radians") {
    val deg = 180
    val rad = degreesToRadians(deg)
    assert(rad == Pi)
  }

  test("Distance between Riga and Vilnius is ~262 km") {
    val rigaLoc = Location(56.9496, 24.1052)
    val vilniusLoc = Location(54.6872, 25.2797)
    val dist = distance(rigaLoc, vilniusLoc)
    val km = round(dist / 1000)
    assert(km == 262L)
  }
}
