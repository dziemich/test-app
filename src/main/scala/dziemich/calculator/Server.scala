package dziemich.calculator

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.util.Timeout
import dziemich.calculator.routes.RestApi

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.{Duration, FiniteDuration}


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