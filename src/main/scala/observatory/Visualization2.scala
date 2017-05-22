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
    
    import observatory.defaults.{tileWidth, tileHeight}

    val pixels = (for {
      x <- 0 until tileWidth
      y <- 0 until tileHeight
      tileLoc = tileLocation(zoom, x, y)
      temp = predictTemperature(grid, tileLoc)
      color = interpolateColor(colors, temp)
      pixel = rgbToPixel(color)
    } yield pixel).toArray

    Image(tileWidth, tileHeight, pixels)
  }

  private[observatory] def predictTemperature(grid: (Int, Int) => Double, tileLocation: Location): Double = {
    val Location(lat, lon) = tileLocation // y, x
    val baseLat = lat.toInt
    val baseLon = lon.toInt

    val y = lat - baseLat
    val x = lon - baseLon

    val d00 = grid(baseLat, baseLon)
    val d01 = grid(baseLat, baseLon + 1)
    val d10 = grid(baseLat + 1, baseLon)
    val d11 = grid(baseLat + 1, baseLon + 1)

    bilinearInterpolation(x, y, d00, d01, d10, d11)
  }

}
