
package caratAPIBackend.services

/*import scala.sys.process._
import scala.concurrent.{Future, ExecutionContext}

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._

import com.typesafe.config._

object AppStats {

	val conf = ConfigFactory.load()
	val sparkSubmitScript = conf.getString("sparkSubmit.script")

	implicit val executor =  ExecutionContext.global

	def runSpark(minSupport: Option[Double] = None, minConfidence: Option[Double] = None, excluded: String = ""): Future[String] = {
		val support = minSupport.getOrElse(defaultMinSupport)
		val confidence = minConfidence.getOrElse(defaultMinConfidence)

		Future {
      s"${sparkSubmitScript} ${sparkSubmitClass} ${sparkSubmitJar} ${support} ${confidence} ${excluded}" !!
		}
	}

}*/
