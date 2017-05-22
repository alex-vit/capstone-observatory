package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.defaults.{
  minDistanceMeters, p, earthRadiusMeters,
  imageWidth, imageHeight
}

import scala.math._

/**
  * 2nd milestone: basic visualization
  */
object Visualization {
  
  // based on the first formula from https://en.wikipedia.org/wiki/Great-circle_distance
  private[observatory] def distance(location1: Location, location2: Location): Double = {
    val phi1 = toRadians(location1.lat)
    val lambda1 = toRadians(location1.lon)
    val phi2 = toRadians(location2.lat)
    val lambda2 = toRadians(location2.lon)

//    val deltaPhi = abs(phi1 - phi2)
    val deltaLambda = abs(lambda1 - lambda2)

    val deltaSigma = acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(deltaLambda))

    val distance = earthRadiusMeters * deltaSigma
    distance // also meters
  }

  // Moved calculation into it's own function, so I can test with different 'p' values.
  private[observatory] def calculatePrediction(known: Iterable[(Location, Double)], forLocation: Location, p: Double = 2) = {
    var weight, weightSum, weighedTemperatures = 0d

    known.foreach{
      case (loc, temperature) =>
        val dist = distance(loc, forLocation)
        weight =
          if (dist <= minDistanceMeters) 1
          else 1d / pow(dist, p)
        weightSum += weight
        weighedTemperatures += weight * temperature
    }
    weighedTemperatures / weightSum
  }

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    calculatePrediction(temperatures, location, p)
  }

  private[observatory] def getBounds(points: Iterable[(Double, Color)], value: Double): ((Double, Color), (Double, Color)) = {
    val arr = points.toArray.sortBy(_._1)

    if (value <= arr.head._1) (arr.head, arr.head)
    else if (value >= arr.last._1) (arr.last, arr.last)
    else {
      val i = (1 until arr.length).find(i => value < arr(i)._1).get
      (arr(i - 1), arr(i))
    }
  }

  private[observatory] def lerp(low: Int, high: Int, t: Double): Int =
    if (low == high) low
    else round((1 - t) * low + t * high).toInt

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    val (low, high) = getBounds(points, value)
    if (low == high) low._2
    else {
      val t = abs((value - low._1) / (high._1 - low._1))
      Color(
        lerp(low._2.red, high._2.red, t),
        lerp(low._2.green, high._2.green, t),
        lerp(low._2.blue, high._2.blue, t)
      )
    }
  }

  private[observatory] def rgbToPixel(color: Color, alpha: Int = 255): Pixel = {
    val Color(r, g, b) = color
    Pixel(r, g, b, alpha)
  }
  private[observatory] def idxToLoc(idx: Int, cols: Int): Location = {
    val x = idx % cols
    val y = idx / cols
    val lon = x - 180
    val lat =
      if (y <= cols / 2) 90 - y
      else -1 * (y - 90)
    Location(lat, lon)
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    val pixels = (for {
      i <- 0 until imageWidth * imageHeight
      loc = idxToLoc(i, imageWidth)
      temp = predictTemperature(temperatures, loc)
      color = interpolateColor(colors, temp)
      pixel = rgbToPixel(color)
    } yield pixel).toArray

    Image(imageWidth, imageHeight, pixels)
  }

}

