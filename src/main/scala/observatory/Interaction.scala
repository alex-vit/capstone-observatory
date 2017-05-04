package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import math._

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
    val lonDeg = x / n * 360d - 180d
    val latRad = atan(sinh(Pi * (1 - 2 * y / n)))
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
    ???
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
  ): Unit = {
    ???
  }

}
