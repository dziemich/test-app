package dziemich.calculator.actors

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import akka.util.Timeout
import dziemich.calculator.actors.Calculator.PerformCalculation
import dziemich.calculator.utils.BasicOperations
import dziemich.calculator.utils.GlobalTypes.{CalculationResult, ValidationResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Calculator {
  def props(implicit timeout: Timeout): Props = Props(new Calculator)
  case class PerformCalculation(validationResult: ValidationResult)
}

class Calculator extends Actor {

  def calcRec(seq1: Seq[Char]): CalculationResult = {
    var parsed: Long = 0
    var result: Long = 0
    var accumulator: Long = 0
    var operator: Char = '+'

    @scala.annotation.tailrec
    def recHelper(remaining: List[Char]): CalculationResult = {
      def getClosingParamIndex(tail: List[Char]): Int = {
        var ind = 0
        var parOpened = 1
        while ( {
          ind += 1
          ind - 1
        } < tail.length) {
          ind
          if (tail(ind) == '(') parOpened += 1
          else if (tail(ind) == ')') parOpened -= 1
          if (parOpened == 0) return ind
        }
        ind
      }

      remaining match {
        case Nil => Right(result)
        case head :: tail => head match {
          case headChar if headChar.isDigit =>
            parsed = parsed * 10 + headChar - '0'
            recHelper(tail)
          case '(' =>
            val closingParIndex: Int = getClosingParamIndex(tail)
            val skippedTail = tail.drop(closingParIndex + 1)

            parsed = calcRec(tail.take(closingParIndex)) match {
              case Left(ex) => return Left(ex)
              case Right(value) => value
            }
            recHelper(skippedTail)
          case _ =>
            accumulator = BasicOperations.calculate(accumulator, parsed, operator) match {
              case Left(ex) => return Left(ex)
              case Right(value) => value
            }
            if (head == '+' || head == '-') {
              result = result + accumulator
              accumulator = 0
            }
            parsed = 0
            operator = head
            recHelper(tail)
        }
      }
    }
    recHelper(seq1.toList).flatMap(res => BasicOperations.calculate(accumulator, parsed, operator).map(cal => cal + res))
  }

  override def receive: Receive = {
    case PerformCalculation(vr) => vr match {
      case Left(error) => pipe(Future(Left(error.toString))).to(sender())
      case Right(seq) => pipe(Future(calcRec(seq))).to(sender())
    }
  }
}
