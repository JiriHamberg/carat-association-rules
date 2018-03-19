package caratAPIPrototype.services

import scalaj.http.{Http, HttpOptions}
import com.typesafe.config._
import scala.concurrent.duration._

case class SparkJobOptions(
  applicationName: String,
  minSupport: Option[Double],
  minConfidence: Option[Double],
  excluded: List[String]
)

object SparkDispatcher {

	val conf = ConfigFactory.load()
	val sparkBackendProtocol = conf.getString("spark-server.protocol")
	val sparkBackendAddr = conf.getString("spark-server.address")
	val sparkBackendPort = conf.getString("spark-server.port")
	val sparkBackendTimeout = conf.getInt("spark-server.timeout")

	def postRequest(sparkJobOptions: SparkJobOptions): String = {
		val request = Http(s"${sparkBackendProtocol}://${sparkBackendAddr}:${sparkBackendPort}")
			.header("Content-Type", "application/json")
			.header("Charset", "UTF-8")
			.header("Accept", "application/json")
			.option(HttpOptions.readTimeout(sparkBackendTimeout.seconds.toMillis.toInt))
      .param("applicationName", sparkJobOptions.applicationName)
      .param("minSupport", sparkJobOptions.minSupport.map(_.toString).getOrElse(""))
      .param("minConfidence", sparkJobOptions.minConfidence.map(_.toString).getOrElse(""))
      .param("excluded", sparkJobOptions.excluded.mkString(","))
			.asString
		request.body
	}

  def mockRequest(sparkJobOptions: SparkJobOptions): String = {
    Thread.sleep(120000)
    scala.io.Source.fromFile("src/main/resources/example_rules.json").mkString
  }

}
