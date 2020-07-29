package dziemich.calculator.actors

import akka.actor.{Actor, Props}
import akka.util.Timeout
import dziemich.calculator.utils.GlobalTypes.ValidationResult
import akka.actor.{Actor, ActorRef, Props}
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout
import dziemich.calculator.utils.GlobalTypes.{CalculationResult, ValidationResult}
import akka.pattern.{ask, pipe}
import dziemich.calculator.actors.Validator.PerformValidation
import dziemich.calculator.utils.{BasicOperations, ValidationError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.collection.mutable

object Validator {
  def props(implicit timeout: Timeout): Props = Props(new Validator)

  case class PerformValidation(inputString: String)

}

class Validator extends Actor {
  val operators: Array[Char] = Array('+', '-', '*', '/')
  val charsAllowed: Array[Char] = Array('(', ')') ++ operators

  def isAllowed(c: Char): Boolean = charsAllowed.contains(c) || c.isDigit

  def validateFirst(c: Char): Boolean = c == '(' || c.isDigit

  def validateLast(c: Char): Boolean = c == ')' || c.isDigit

  def validate(string: String): ValidationResult = {
    val cleaned = string.filterNot((x: Char) => x.isWhitespace)
    if (cleaned.length <= 2) return Left(ValidationError.INPUT_TOO_SHORT)
    val stack: mutable.Stack[Char] = mutable.Stack()

    val first = cleaned.charAt(0)
    if (first == '(') stack.push(first)
    val last = cleaned.last

    if (!validateFirst(first)) return Left(ValidationError.ILLEGAL_BEGINNING)
    if (!validateLast(last)) return Left(ValidationError.ILLEGAL_ENDING)

    for (i <- 1 until cleaned.length) {
      val current = cleaned.charAt(i)
      val previous = cleaned.charAt(i - 1)

      if (!isAllowed(current)) return Left(ValidationError.ILLEGAL_CHARACTER)
      current match {
        case '(' => stack.push(current)
        case ')' => {
          if (stack.isEmpty) return Left(ValidationError.UNBALANCED_PARENTHESES)
          if (operators.contains(previous)) return Left(ValidationError.INVALID_OPERATOR_USAGE)
          if (stack.top == '(') {
            stack.pop()
          } else return Left(ValidationError.UNBALANCED_PARENTHESES)
        }
        case '+' | '*' | '/' => {
          if (
            (List('(') ++ operators).contains(previous)) return Left(ValidationError.INVALID_OPERATOR_USAGE)
        }
        case '-' => if (operators.contains(previous)) return Left(ValidationError.INVALID_OPERATOR_USAGE)
        case digit if digit.isDigit => {
          if (previous == ')') return Left(ValidationError.MISSING_OPERATOR)
        }
      }
    }
    if (stack.isEmpty) Right(cleaned) else Left(ValidationError.UNBALANCED_PARENTHESES)
  }

  override def receive: Receive = {
    case PerformValidation(input) => pipe(Future.successful(validate(input))).to(sender())
  }

}
