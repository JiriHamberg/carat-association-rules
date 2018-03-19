import caratAPIPrototype.servlets.{MainServlet, SparkDispatchServlet}

import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {

    if(scala.util.Properties.envOrElse("SCALATRA_ENVIRONMENT", "") == "development") {
      context.initParameters("org.scalatra.environment") = "development"
    } else {
      context.initParameters("org.scalatra.environment") = "production"
    }

    context.mount(new MainServlet, "/*")
    context.mount(new SparkDispatchServlet(context), "/spark-submit")
  }
}
