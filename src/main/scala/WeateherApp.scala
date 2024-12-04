import sttp.client3._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.DefaultFormats

object WeatherApp extends App{

  val apiKey = "85f41b4ae0dd98de437bed7a11bf8bad"
  val lat = 45.23
  val lon = 19.82
  val url = s"https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey"

  //get request
  def getWeatherData(): String = {
    val backend = HttpURLConnectionBackend()
    val request = basicRequest.get(uri"$url")
    val response = request.send(backend)

    response.body.getOrElse("")
  }

//parse data
  def parseWeatherData(json: String): (String, Double, Double, String, Double, Double, Double, Double, Double, Double,Double, String, String) = {
    implicit val formats: Formats = DefaultFormats  

    val parsedJson = parse(json)
    val city = (parsedJson \ "name").extract[String]
    val lat = (parsedJson \ "coord" \ "lat").extract[Double]
    val lon = (parsedJson \ "coord" \ "lon").extract[Double]
    val main = (parsedJson \ "main")
    val temp = (main \ "temp").extract[Double]
    val feelsLike = (main \ "feels_like").extract[Double]
    val tempMin = (main \ "temp_min").extract[Double]
    val tempMax = (main \ "temp_max").extract[Double]
    val pressure = (main \ "pressure").extract[Double]
    val humidity = (main \ "humidity").extract[Double]
    val seaLevel = (main \ "sea_level").extract[Double]
    val sys = (parsedJson \ "sys")
    val sunriseEpoch = (sys \ "sunrise").extract[Long]
    val sunsetEpoch = (sys \ "sunset").extract[Long]
    val dateTimeFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val date = java.time.LocalDateTime.now().format(dateTimeFormatter)
    val sunrise = java.time.Instant.ofEpochSecond(sunriseEpoch).atZone(java.time.ZoneId.systemDefault()).toLocalDateTime.format(dateTimeFormatter)
    val sunset = java.time.Instant.ofEpochSecond(sunsetEpoch).atZone(java.time.ZoneId.systemDefault()).toLocalDateTime.format(dateTimeFormatter)

    (city, lat, lon, date, temp, feelsLike, tempMin, tempMax, pressure, humidity, seaLevel, sunrise, sunset)
  }

//save data to weather_data.csv
  def saveToCSV(data: (String, Double, Double, String, Double, Double, Double, Double, Double, Double, Double, String, String)): Unit = {
    val writer = new java.io.PrintWriter(new java.io.File("weather_data.csv"))
    writer.write("City Name, Latitude, Longitude, Date, Temperature, Feels Like, Temp Min, Temp Max, Pressure, Humidity, Sea Level, Sunrise Time, Sunset Time\n")
    writer.write(data.productIterator.mkString(",") + "\n")
    writer.close()
  }

//run methods
  val weatherJson = getWeatherData()
  val weatherData = parseWeatherData(weatherJson)
  saveToCSV(weatherData)
  println("Weather data has been saved to 'weather_data.csv'.")
}
