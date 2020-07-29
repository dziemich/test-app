package dziemich.calculator.actors

import akka.actor.{Actor, ActorRef, Props}
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout
import dziemich.calculator.utils.GlobalTypes.{CalculationResult, ValidationResult}
import dziemich.calculator.actors.Calculator.PerformCalculation
import akka.pattern.{ask, pipe}
import dziemich.calculator.utils.BasicOperations

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Calculator {
  def props(implicit timeout: Timeout): Props = Props(new Calculator)

  case class PerformCalculation(validationResult: ValidationResult)

}

class Calculator extends Actor {

  def calcRec(seq1: Seq[Char]): CalculationResult = {
    var tmp1: Long = 0
    var res1: Long = 0
    var num1: Long = 0
    var op: Char = '+'

    @scala.annotation.tailrec
    def recHelper(remaining: List[Char]): CalculationResult = {
      def getClosingParamIndex(tail: List[Char]): Int = {
        var ind = 0;
        var parOpened = 1;
        while ( {
          ind += 1;
          ind - 1
        } < tail.length) {
          ind
          if (tail(ind) == '(') parOpened += 1
          else if (tail(ind) == ')') parOpened -= 1
          if (parOpened == 0) return ind
        }
        -1
      }

      remaining match {
        case Nil => Right(res1)
        case head :: tail => head match {
          case headChar if headChar.isDigit => {
            tmp1 = tmp1 * 10 + headChar - '0'
            recHelper(tail)
          }
          case '(' => {
            val closingParIndex: Int = getClosingParamIndex(tail)
            val skippedTail = tail.drop(closingParIndex + 1)

            tmp1 = calcRec(tail.take(closingParIndex)) match {
              case Left(ex) => return Left(ex)
              case Right(value) => value
            }
            recHelper(skippedTail)
          }
          case _ => {
            num1 = BasicOperations.calculate(num1, tmp1, op) match {
              case Left(ex) => return Left(ex)
              case Right(value) => value
            }
            if (head == '+' || head == '-') {
              res1 = res1 + num1
              num1 = 0
            }
            tmp1 = 0
            op = head
            recHelper(tail)
          }
        }
      }
    }

    recHelper(seq1.toList).flatMap(res => BasicOperations.calculate(num1, tmp1, op).map(cal => cal + res))
  }

  override def receive: Receive = {
    case PerformCalculation(vr) => vr match {
      case Left(error) => pipe(Future(Left(error.toString))).to(sender())
      case Right(seq) => pipe(Future(calcRec(seq))).to(sender())
    }
  }
}
