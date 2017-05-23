package observatory

import observatory.Visualization._

import math.{Pi, round}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers {

  private val points = Vector[(Double, Color)](
    (2d, Color(2, 2, 2)),
    (3d, Color(3, 3, 3)),
    (4d, Color(4, 4, 4))
  )
  
  test("Interpolate color with Coursera's values") {
    // def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color
    val ccolor = Color(231,0,24)
    val points = Iterable(
      (-1d, Color(0, 255, 0)),
      (0d, Color(0, 0, 255)),
      (1d, Color(255, 0, 0)),
      (2d, Color(255, 255, 0))
    )
    val value = 0.90588235294d
    
    val interpolated = interpolateColor(points, value)
    assert(interpolated == ccolor)
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

    val prediction = calculatePrediction(known = known, forLocation = rigaLoc)
    val average = (t1 + t2) / 2

    assert(round(prediction) == round(average))

  }

//  test("Interpolated 'middle' color should be (128, 128, 128)") {
//    val black = Color(0, 0, 0)
//    val white = Color(255, 255, 255)
//
//    val vec = Vector[(Double, Color)](
//      (0, black),
//      (1, white)
//    )
//    val v = 0.5
//    val interpolated = interpolateColor(vec, v)
//    assert(interpolated === Color(128, 128, 128))
//  }
//
//  test("getBounds should return 2 x lower bound if value is less than the smallest point") {
//    val v = 1d
//    val bounds: ((Double, Color), (Double, Color)) = getBounds(points, v)
//    assert(bounds._1 === bounds._2, "Result tuple should have 2 identical elements")
//    assert((2d, Color(2, 2, 2)) === bounds._1)
//  }
//
//  test("getBounds should return 2 x higher bound if value is greater than the largest point") {
//    val v = 5d
//    val bounds: ((Double, Color), (Double, Color)) = getBounds(points, v)
//    assert(bounds._1 === bounds._2, "Result tuple should have 2 identical elements")
//    assert((4, Color(4, 4, 4)) === bounds._1)
//  }
//
//  test("getBounds should not skip the last element (1 until points.length)") {
//    val v = 3.5d
//    val bounds: ((Double, Color), (Double, Color)) = getBounds(points, v)
//    assert(bounds._1 !== bounds._2, "Result tuple should have 2 identical elements")
//    assert((
//      (3d, Color(3, 3, 3)),
//      (4d, Color(4, 4, 4))
//    ) === bounds)
//  }

}
