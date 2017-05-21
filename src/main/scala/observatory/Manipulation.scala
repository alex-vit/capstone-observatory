package observatory

import observatory.Visualization.{minDistance, p, distance, predictTemperature}

/**
  * 4th milestone: value-added information
  */
object Manipulation {

  /**
    * @param temperatures Known temperatures
    * @return A function that, given a latitude in [-89, 90] and a longitude in [-180, 179],
    *         returns the predicted temperature at this location
    */
  def makeGrid(temperatures: Iterable[(Location, Double)]): (Int, Int) => Double = {

    val temperatureMap = (
      for {
        lat <- -89 to 90
        lon <- -180 to 179
        key = (lat, lon)
        loc = Location(lat, lon)
        temp = predictTemperature(temperatures, loc)
      } yield (key, temp)
    ).toMap

    def capTo(x: Int, min: Int, max: Int): Int =
      if (x < min) min
      else if (x > max) max
      else x

    def grid(temperatureMap: Map[(Int, Int), Double])(lat: Int, lon: Int) = {
      val a = capTo(lat, -89, 90)
      val b = capTo(lon, -180, 179)
      temperatureMap((a, b))
    }

    grid(temperatureMap)
  }

  /**
    * @param temperaturess Sequence of known temperatures over the years (each element of the collection
    *                      is a collection of pairs of location and temperature)
    * @return A function that, given a latitude and a longitude, returns the average temperature at this location
    */
  def average(temperaturess: Iterable[Iterable[(Location, Double)]]): (Int, Int) => Double = {
    val temperatures: Map[Location, Double] = temperaturess.flatten.groupBy(_._1).mapValues(x => x.map(_._2).sum / x.size)
    makeGrid(temperatures)
  }

  /**
    * @param temperatures Known temperatures
    * @param normals A grid containing the “normal” temperatures
    * @return A sequence of grids containing the deviations compared to the normal temperatures
    */
  def deviation(temperatures: Iterable[(Location, Double)], normals: (Int, Int) => Double): (Int, Int) => Double = {
    val tempGrid = makeGrid(temperatures)
    (lat: Int, lon: Int) => {
      tempGrid(lat, lon) - normals(lat, lon)
    }
  }


}

