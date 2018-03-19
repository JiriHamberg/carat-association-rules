
package caratAPIPrototype.servlets

import caratAPIPrototype.services.SparkDispatcher
import caratAPIPrototype.services.SparkJobOptions
//import caratAPIPrototype.servlets.MyFutureSupport
import org.scalatra.MyFutureSupport
//import caratAPIPrototype.services.SparkJobOptions

import scala.util.{ Try, Success, Failure }
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.duration._

import com.typesafe.config._

import org.scalatra._
import org.scalatra.FutureSupport

import org.json4s.{DefaultFormats, Formats}
//import org.json4s.JsonAST.{JValue}
import org.scalatra.json._
//import org.json4s.JsonDSL._

import javax.servlet.ServletContext

class SparkDispatchServlet(context: ServletContext) extends ScalatraServlet with JacksonJsonSupport with MyFutureSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  val conf = ConfigFactory.load()
	//override def asyncTimeout = conf.getInt("spark-server.timeout") seconds
  implicit val timeout: Duration = conf.getInt("spark-server.timeout") seconds
  implicit val executor = ExecutionContext.global



  before() {
    contentType = formats("json")
  }

	post("/") {
    val maybeSparkJobOptions = Try(parsedBody.extract[SparkJobOptions])

    maybeSparkJobOptions match {

      case Success(sparkJobOptions) =>

        if(context.initParameters("org.scalatra.environment") == "development") {
          Future(SparkDispatcher.mockRequest(sparkJobOptions))
        } else {
          Future(SparkDispatcher.postRequest(sparkJobOptions)) recoverWith {
            case e: Exception => Future(InternalServerError(e.getMessage + "\n" + e.getStackTrace.mkString("\n")))
          }
        }

     case Failure(e) => Future(InternalServerError(e.getMessage + "\n" + e.getStackTrace.mkString("\n")))

    }

    /*if(context.initParameters("org.scalatra.environment") == "development") {
      Future(SparkDispatcher.mockRequest(sparkJobOptions))
    } else {
      Future(SparkDispatcher.postRequest(sparkJobOptions)) recoverWith {
        case e: Exception => Future(InternalServerError(e.getMessage + "\n" + e.getStackTrace.mkString("\n")))
      }
    }*/
  }

}
