package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.Interaction.{tile, tileLocation}
import observatory.Visualization.{interpolateColor, predictTemperature, rgbToPixel}

/**
  * 5th milestone: value-added information visualization
  */
object Visualization2 {
  /**
    * @param x X coordinate between 0 and 1
    * @param y Y coordinate between 0 and 1
    * @param d00 Top-left value
    * @param d01 Bottom-left value
    * @param d10 Top-right value
    * @param d11 Bottom-right value
    * @return A guess of the value at (x, y) based on the four known values, using bilinear interpolation
    *         See https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */
  def bilinearInterpolation(
    x: Double,
    y: Double,
    d00: Double,
    d01: Double,
    d10: Double,
    d11: Double
  ): Double = {
    d00 * (1 - x) * (1 - y) +
    d10 * x       * (1 - y) +
    d01 * (1 - x) * y       +
    d11 * x       * y
  }

  /**
    * @param grid Grid to visualize
    * @param colors Color scale to use
    * @param zoom Zoom level of the tile to visualize
    * @param x X value of the tile to visualize
    * @param y Y value of the tile to visualize
    * @return The image of the tile at (x, y, zoom) showing the grid using the given color scale
    */
  def visualizeGrid(
    grid: (Int, Int) => Double,
    colors: Iterable[(Double, Color)],
    zoom: Int,
    x: Int,
    y: Int
  ): Image = {

    val temperatures: Stream[(Location, Double)] = (
      for {
        lat <- -89 to 90
        lon <- -180 to 179
        loc = Location(lat, lon)
        temp = grid(lat, lon)
      } yield (loc, temp)
      ).toStream

    tile(temperatures, colors, zoom, x, y)
  }

}
