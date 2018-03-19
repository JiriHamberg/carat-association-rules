
package caratAPIPrototype.servlets

import scala.util.Try

import org.scalatra._
import scalate.ScalateSupport
import org.scalatra.json._
import org.json4s.{DefaultFormats, Formats}
//import org.fusesource.scalate.{ TemplateEngine, Binding }
//import org.fusesource.scalate.layout.DefaultLayoutStrategy

import scala.concurrent.ExecutionContext

import caratAPIPrototype.services.AppCounts


class MainServlet extends ScalatraServlet with ScalateSupport with FutureSupport with JacksonJsonSupport{
  protected implicit val executor = ExecutionContext.global
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  get("/") {
    contentType = "text/html"
    ssp("templates/views/base.ssp", "layout" -> "", "contextPath" -> request.getContextPath())
  }

  get("/app-counts") {
    contentType = formats("json")
    AppCounts.loadAppCounts()
  }

}
