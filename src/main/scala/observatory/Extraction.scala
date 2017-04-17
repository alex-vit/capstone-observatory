package observatory

import java.time.LocalDate

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
  * 1st milestone: data extraction
  */
object Extraction {

  import org.apache.log4j.{Level, Logger}
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  case class TemperatureEntry(month: Int, day: Int, latitude: Double, longitude: Double, temperature: Double)

  private[observatory] val spark = SparkSession.builder().appName("observatory").master("local[*]").getOrCreate()

  import spark.implicits._

  private def fsPath(filename: String): String = Extraction.getClass.getResource(filename).getPath

  private def concatUDF = udf((stn: String, wban: String) => (stn, wban))

  private def stationsDF(stationsFile: String): DataFrame =
    spark.read.csv(fsPath(stationsFile))
      .toDF("stn", "wban", "latitude", "longitude")
      .na.fill("", Array("stn", "wban"))
      .withColumn("id", concatUDF($"stn", $"wban"))

  private def temperaturesDF(year: Int, temperaturesFile: String): DataFrame =
    spark.read.csv(fsPath(temperaturesFile))
      .toDF("stn", "wban", "month", "day", "temperature")
      .na.fill("", Array("stn", "wban"))
      .withColumn("id", concatUDF($"stn", $"wban"))

  private def toCelsius(fahrenheit: Double): Double = (fahrenheit - 32) * 5 / 9

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    val stations: DataFrame = stationsDF(stationsFile)
    val temperatures: DataFrame = temperaturesDF(year, temperaturesFile)
    val joined: DataFrame = temperatures.join(stations, stations("id") === temperatures("id"))

    val converted: Dataset[TemperatureEntry] = joined
      .select("month", "day", "latitude", "longitude", "temperature")
      .na.drop()
      .map(row => TemperatureEntry
        (
          row.getAs[String]("month").toInt,
          row.getAs[String]("day").toInt,
          row.getAs[String]("latitude").toDouble,
          row.getAs[String]("longitude").toDouble,
          toCelsius(row.getAs[String]("temperature").toDouble)
        )
      )


    val x: Array[(LocalDate, Location, Double)] = converted.collect().map {
      case TemperatureEntry(month, day, latitude, longitude, temperature) => (LocalDate.of(year, month, day), Location(latitude, longitude), temperature)
    }
    x
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    val grouped: Map[Location, Iterable[(LocalDate, Location, Double)]] = records.groupBy(_._2)
    val averaged: Map[Location, Double] = grouped.mapValues(list => list.map(_._3).sum / list.size)
    averaged
  }

}
