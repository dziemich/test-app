package dziemich.calculator.processing

import dziemich.calculator.utils.GlobalTypes.ValidationResult
import dziemich.calculator.utils.ProcessingError

import scala.collection.mutable

object ValidationProcessor {
  val operators: Array[Char] = Array('+', '-', '*', '/')
  val charsAllowed: Array[Char] = Array('(', ')') ++ operators

  def isAllowed(c: Char): Boolean = charsAllowed.contains(c) || c.isDigit

  def validateFirst(c: Char): Boolean = c == '(' || c.isDigit

  def validateLast(c: Char): Boolean = c == ')' || c.isDigit

  def validate(string: String): ValidationResult = {
    val cleaned = string.filterNot((x: Char) => x.isWhitespace)
    if (cleaned.length <= 2) return Left(ProcessingError.INPUT_TOO_SHORT)
    val stack: mutable.Stack[Char] = mutable.Stack()

    val first = cleaned.charAt(0)
    if (first == '(') stack.push(first)
    val last = cleaned.last

    if (!validateFirst(first)) return Left(ProcessingError.ILLEGAL_BEGINNING)
    if (!validateLast(last)) return Left(ProcessingError.ILLEGAL_ENDING)

    for (i <- 1 until cleaned.length) {
      val current = cleaned.charAt(i)
      val previous = cleaned.charAt(i - 1)

      if (!isAllowed(current)) return Left(ProcessingError.ILLEGAL_CHARACTER)
      current match {
        case '(' => stack.push(current)
        case ')' =>
          if (stack.isEmpty) return Left(ProcessingError.UNBALANCED_PARENTHESES)
          if (operators.contains(previous)) return Left(ProcessingError.INVALID_OPERATOR_USAGE)
          if (stack.top == '(') {
            stack.pop()
          } else return Left(ProcessingError.UNBALANCED_PARENTHESES)
        case '+' | '*' | '/' =>
          if (
            (List('(') ++ operators).contains(previous)) return Left(ProcessingError.INVALID_OPERATOR_USAGE)
        case '-' => if (operators.contains(previous)) return Left(ProcessingError.INVALID_OPERATOR_USAGE)
        case digit if digit.isDigit =>
          if (previous == ')') return Left(ProcessingError.MISSING_OPERATOR)
      }
    }
    if (stack.isEmpty) Right(cleaned) else Left(ProcessingError.UNBALANCED_PARENTHESES)
  }

}

