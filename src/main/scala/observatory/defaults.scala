package observatory


object defaults {
  
  private[observatory] val tileWidth, tileHeight = 256
  private[observatory] val imageWidth = 360
  private[observatory] val imageHeight = 180
  private[observatory] val alpha = 127
  
  private[observatory] val p = 2
  private[observatory] val minDistanceMeters = 1000d
  private[observatory] val earthRadiusMeters = 6371 * 1000
  
  private[observatory] val gridLatStart = -89
  private[observatory] val gridLatEnd = 90
  private[observatory] val gridLonStart = -180
  private[observatory] val gridLonEnd = 179
  
  private[observatory] val temperatureColorScale = Seq(
    (60d,   Color(255,  255,  255)),
    (32d,   Color(255,  0,    0)),
    (12d,   Color(255,  255,  0)),
    (0d,    Color(0,    255,  255)),
    (-15d,  Color(0,    0,    255)),
    (-27d,  Color(255,  0,    255)),
    (-50d,  Color(33,   0,    107)),
    (-60d,  Color(0,    0,    0))
  )
  private[observatory] val deviationColorScale = Seq(
    (7d,  Color(0,    0,    0)),
    (4d,  Color(255,  0,    0)),
    (2d,  Color(255,  255,  0)),
    (0d,  Color(255,  255,  255)),
    (-2d, Color(0,    255,  255)),
    (-7d, Color(0,    0,    255))
  )

  private[observatory] val dataYearRange = 1975 to 2015
  
}