package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import math._
import Visualization.{predictTemperature, interpolateColor, rgbToPixel}

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {

  /**
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return The latitude and longitude of the top-left corner of the tile, as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(zoom: Int, x: Int, y: Int): Location = {
//    def num2deg(xtile, ytile, zoom):
//      n = 2.0 ** zoom
//      lon_deg = xtile / n * 360.0 - 180.0
//      lat_rad = math.atan(math.sinh(math.pi * (1 - 2 * ytile / n)))
//      lat_deg = math.degrees(lat_rad)
//      return (lat_deg, lon_deg)
    val n = pow(2.0d, zoom)
    val lonDeg = x.toDouble / n * 360 - 180
    val latRad = atan(sinh(Pi * (1 - 2 * y.toDouble / n)))
    val latDeg = toDegrees(latRad)
    Location(latDeg, lonDeg)
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return A 256×256 image showing the contents of the tile defined by `x`, `y` and `zooms`
    */
  def tile(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)], zoom: Int, x: Int, y: Int): Image = {

    val width, height = 256
    val alpha = 127

    val xOffset = width * x
    val yOffset = height * y

    val pixels = (for {
      xx <- xOffset until xOffset + width
      yy <- yOffset until yOffset + height
      tileLoc = tileLocation(zoom, xx, yy)
      temp = predictTemperature(temperatures, tileLoc)
      color = interpolateColor(colors, temp)
      pixel = rgbToPixel(color = color, alpha = alpha)
    } yield pixel).toArray

    Image(width, height, pixels)
  }

  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    * @param yearlyData Sequence of (year, data), where `data` is some data associated with
    *                   `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](
                           yearlyData: Iterable[(Int, Data)],
                           generateImage: (Int, Int, Int, Int, Data) => Unit
                         ): Unit = yearlyData.par.foreach {
    case (year: Int, data: Data) => for {
      zoom <- 0 to 3
      x <- 0 until pow(2, zoom).toInt
      y <- 0 until pow(2, zoom).toInt
    } generateImage(year, zoom, x, y, data)
  }

}
