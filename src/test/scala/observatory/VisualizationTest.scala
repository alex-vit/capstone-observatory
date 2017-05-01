package observatory

import observatory.Visualization.{calculatePrediction, degreesToRadians, distance}

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

  test("Very close location should have the same predicted temperature") {
    val rigaLoc = Location(56.9496, 24.1052)
    val veryClose = Location(56.94961, 24.10519)
    //  val vilniusLoc = Location(54.6872, 25.2797)
    val dist = distance(rigaLoc, veryClose)
    assert(dist <= 1000)

    val temp = 25.0
    val known = List((rigaLoc, temp))
    val prediction = calculatePrediction(known, veryClose)
    assert(prediction == temp)
  }

  test("Midpoint prediction should be ~= AVG(t1, t2)") {
    val tallinnLoc = Location(59.4370, 24.7536)
    val rigaLoc = Location(56.9496, 24.1052)
    val vilniusLoc = Location(54.6872, 25.2797)

    val t1 = 20.0
    val t2 = 10.0

    val known = List(
      (tallinnLoc, t1),
      (vilniusLoc, t2)
    )

    val prediction = calculatePrediction(known = known, forLocation = rigaLoc, p = 2)
    val average = (t1 + t2) / 2

    assert(round(prediction) == round(average))

  }
}
