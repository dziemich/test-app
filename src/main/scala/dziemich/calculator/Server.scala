package dziemich.calculator

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import dziemich.calculator.routes.{RestApi}

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.io.StdIn


object Server {
  def main(args: Array[String]) {
    implicit val system: ActorSystem = ActorSystem("system")
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    def requestTimeout(): Timeout = {
      val d = Duration(10000, "millis")
      FiniteDuration(d.length, d.unit)
    }

    val api = new RestApi(system, requestTimeout())
    Http().bindAndHandle(api.route, "localhost", 5555)
  }
}