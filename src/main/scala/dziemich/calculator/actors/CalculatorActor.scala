package dziemich.calculator.actors

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import akka.util.Timeout
import dziemich.calculator.actors.CalculatorActor.PerformCalculation
import dziemich.calculator.processing.CalculationProcessor
import dziemich.calculator.utils.{BasicOperations}
import dziemich.calculator.utils.GlobalTypes.{CalculationResult, ValidationResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CalculatorActor {
  def props(implicit timeout: Timeout): Props = Props(new CalculatorActor)
  case class PerformCalculation(validationResult: ValidationResult)
}

class CalculatorActor extends Actor {
  
  override def receive: Receive = {
    case PerformCalculation(vr) => vr match {
      case Left(error) => pipe(Future(Left(error))).to(sender())
      case Right(seq) => pipe(Future(CalculationProcessor.calcRec(seq))).to(sender())
    }
  }
}