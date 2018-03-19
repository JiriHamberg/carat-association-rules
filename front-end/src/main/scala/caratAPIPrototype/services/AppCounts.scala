package caratAPIPrototype.services

import scalaj.http.{Http, HttpOptions}
import com.typesafe.config._
import scala.concurrent.duration._

//import org.json4s._
//import org.json4s.jackson.JsonMethods._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

object AppCounts {
	//implicit val formats = org.json4s.DefaultFormats
  implicit val executor = ExecutionContext.global

  val conf = ConfigFactory.load()
  val appCountFile = conf.getString("app-count-file")

  def loadAppCounts(): Future[String] = Future {
    scala.io.Source.fromFile(appCountFile).getLines.mkString
    //parse(appCountsJson).extract[Map[String, Int]]
  }


}
