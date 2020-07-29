import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, MediaTypes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.TestKit
import akka.util.ByteString
import dziemich.calculator.routes.RestApi
import dziemich.calculator.utils.{Error, JsonMarshaller, Result, ValidationError}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.FiniteDuration

class RouteSpec()
  extends AnyFunSpec
    with Matchers
    with BeforeAndAfterAll
    with ScalatestRouteTest
    with JsonMarshaller {
  val route: Route = new RestApi(system, FiniteDuration(10000, TimeUnit.MILLISECONDS)).route

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  describe("Route") {
    it("should handle correct incoming request with valid expression") {
      val json = ByteString("""{"expression":"1+2"}""".stripMargin)

      val postRequest = HttpRequest(
        HttpMethods.POST,
        uri = "/evaluate",
        entity = HttpEntity(MediaTypes.`application/json`, json))

      val expected = Result(3)

      postRequest ~> route ~> check {
        responseAs[Result] shouldEqual expected
      }
    }

    it("should handle correct incoming request with invalid expression") {
      val json = ByteString("""{"expression":"(1+2"}""".stripMargin)

      val postRequest = HttpRequest(
        HttpMethods.POST,
        uri = "/evaluate",
        entity = HttpEntity(MediaTypes.`application/json`, json))

      postRequest ~> route ~> check {
        responseAs[Error] shouldEqual Error(ValidationError.UNBALANCED_PARENTHESES.toString)
      }
    }
  }
}