package dziemich.calculator.actors

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import akka.util.Timeout
import dziemich.calculator.actors.ValidatorActor.PerformValidation
import dziemich.calculator.processing.ValidationProcessor
import dziemich.calculator.utils.GlobalTypes.ValidationResult
import dziemich.calculator.utils.ProcessingError

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ValidatorActor {
  def props(implicit timeout: Timeout): Props = Props(new ValidatorActor)
  case class PerformValidation(inputString: String)
}

class ValidatorActor extends Actor {
  override def receive: Receive = {
    case PerformValidation(input) => pipe(Future.successful(ValidationProcessor.validate(input))).to(sender())
  }
}
