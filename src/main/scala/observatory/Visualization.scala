package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import math._

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  private[observatory] def degreesToRadians(degrees: Double): Double = degrees * Pi / 180

  // based on the first formula from https://en.wikipedia.org/wiki/Great-circle_distance
  private[observatory] def distance(location1: Location, location2: Location): Double = {
    val phi1 = degreesToRadians(location1.lat)
    val lambda1 = degreesToRadians(location1.lon)
    val phi2 = degreesToRadians(location2.lat)
    val lambda2 = degreesToRadians(location2.lon)

//    val deltaPhi = abs(phi1 - phi2)
    val deltaLambda = abs(lambda1 - lambda2)

    val deltaSigma = acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(deltaLambda))

    val earthRadius = 6371 * 1000 // meters

    val distance = earthRadius * deltaSigma
    distance // also meters
  }

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    ???
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    ???
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    ???
  }

}

