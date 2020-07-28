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
    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    

//    object MyJsonProtocol extends DefaultJsonProtocol {
//    }

//    import MyJsonProtocol._
//    import spray.json._
    
    def requestTimeout(): Timeout = {
      val d = Duration(10000, "millis")
      FiniteDuration(d.length, d.unit)
    }
    val api = new RestApi(system, requestTimeout())
    
    val bindingFuture = Http().bindAndHandle(api.routes, "localhost", 5555)

    println(s"Server online at http://localhost:5555/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}