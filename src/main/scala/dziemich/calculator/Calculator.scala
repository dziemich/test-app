package dziemich.calculator


class Calculator {
  type CalculationResult = Either[Exception, Int]


  def performCalculation(s: String): CalculationResult = {
    if (s == null || s.length == 0) return Left(new IllegalStateException("String"))
    calculate(s, 0)._1
  }

  def calculate(s: String, i: Int): (CalculationResult, Int) = {
    var result: CalculationResult = Right(0)
    var tmp: CalculationResult = Right(0)
    var num: CalculationResult = Right(0)
    var op = '+'
    var index = i;

    while ( {
      index < s.length
    }) {
      val c = s.charAt({
        index += 1;
        index - 1
      })
      if (Character.isDigit(c)) tmp = tmp.map(t => t * 10 + c - '0')
      else if (c == '(') {
        val ret = calculate(s, index)
        tmp = ret._1
        index = ret._2
      }
      else if (c == ')') {
        result = result.flatMap(res => doCalc(num, tmp, op).map(dc => res + dc))
        return (result, index)
      }
      else {
        num = doCalc(num, tmp, op)
        if (c == '+' || c == '-') {
          result = result.flatMap(result => num.map(num => result + num));
          num = Right(0)
        }
        tmp = Right(0)
        op = c
      }
    }
    result = result.flatMap(res => doCalc(num, tmp, op).map(doCalc => res + doCalc))
    (result, index)
  }

  private def cal(num: Int, tmp: Int, op: Char) = if (op == '+') num + tmp
  else if (op == '-') num - tmp
  else if (op == '*') num * tmp
  else num / tmp


  private def doCalc(num: CalculationResult, tmp: CalculationResult, op: Char) = num.flatMap(n =>
    tmp.flatMap(
      t => {
        if (t == 0) Left(new IllegalStateException("Div by 0")) else Right(cal(n, t, op))
      }
    )
  )


  def calcRec(seq1: Seq[Char]): CalculationResult = {
    var tmp1: Int = 0
    var res1: Int = 0
    var num1: Int = 0
    var op: Char = '+'

    def cal0(num: Int, tmp: Int, op: Char): CalculationResult = {
      if (op == '+') Right(num + tmp)
      else if (op == '-') Right(num - tmp)
      else if (op == '*') Right(num * tmp)
      else if (tmp == 0) Left(new IllegalStateException("0 div")) else Right(num / tmp)
    }

    def recHelper(remaining: List[Char]): Either[Exception, Int] = {
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
            num1 = cal0(num1, tmp1, op) match {
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

    recHelper(seq1.toList).flatMap(res => cal0(num1, tmp1, op).map(cal => cal + res))
  }

}

object Main {

  def main(args: Array[String]): Unit = {
    val s = new Calculator()
    println(s.calcRec("2+2".toSeq))
    println(s.performCalculation("(12+2)/(5-3)"))
    println(s.performCalculation("(2+2)*2"))
    println(s.performCalculation("2+2*2"))
    println(s.performCalculation("2/(2-2)"))

  }
}

