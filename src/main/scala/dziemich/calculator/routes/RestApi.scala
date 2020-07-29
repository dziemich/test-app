package dziemich.calculator.routes

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.pattern.ask
import akka.util.Timeout
import dziemich.calculator.actors.CalculatorActor.PerformCalculation
import dziemich.calculator.actors.ValidatorActor.PerformValidation
import dziemich.calculator.actors.{CalculatorActor, ValidatorActor}
import dziemich.calculator.utils.GlobalTypes.{CalculationResult, ValidationResult}
import dziemich.calculator.utils.{Error, Expression, JsonMarshaller, Result}
import scala.concurrent.{ExecutionContextExecutor, Future}


trait Api {
  def createCalculator(): ActorRef
  def createValidator(): ActorRef

  lazy val calculatorActor: ActorRef = createCalculator()
  lazy val validatorActor: ActorRef = createValidator()
}

class RestApi(actorSystem: ActorSystem, timeout: Timeout) extends Api with JsonMarshaller {
  implicit val requestTimeout: Timeout = timeout
  implicit def executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  
  def createCalculator(): ActorRef = actorSystem.actorOf(CalculatorActor.props)
  def createValidator(): ActorRef = actorSystem.actorOf(ValidatorActor.props)
  
  def requestValidation(event: String): Future[ValidationResult] = {
    validatorActor.ask(PerformValidation(event)).mapTo[ValidationResult]
  }

  def requestCalculation(validationResult: ValidationResult): Future[CalculationResult] = {
    calculatorActor.ask(PerformCalculation(validationResult)).mapTo[CalculationResult]
  }
  
  protected val evaluateRoute: Route = {
    pathPrefix("evaluate") {
      post {
        pathEndOrSingleSlash {
          entity(as[Expression]) { ex =>
            onSuccess(requestValidation(ex.expression).flatMap(vr => requestCalculation(vr))) {
              case Left(ex) => complete(Error(ex))
              case Right(value) => complete(Result(value))
            }
          }
        }
      }
    }
  }
  
  val route: Route = evaluateRoute
}