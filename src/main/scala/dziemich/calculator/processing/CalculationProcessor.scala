package dziemich.calculator.processing

import dziemich.calculator.utils.{BasicOperations, ProcessingError}
import dziemich.calculator.utils.GlobalTypes.CalculationResult

object CalculationProcessor {
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
            if (Long.MaxValue / 10 < parsed) return Left(ProcessingError.LONG_OVERFLOW)
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
}
