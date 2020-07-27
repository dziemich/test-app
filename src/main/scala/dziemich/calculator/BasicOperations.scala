package dziemich.calculator

import dziemich.calculator.GlobalTypes.CalculationResult

object BasicOperations {
  @scala.annotation.tailrec
  private def add(x: Long, y: Long): Long = if (y == 0) x 
  else add(x ^ y, (x & y) << 1)

  @scala.annotation.tailrec
  private def sub(x: Long, y: Long): Long = if (y == 0) x
  else sub(x ^ y, (~x & y) << 1)

  private def mul(x: Long, y: Long): Long = if (y == 0) 0
  else if (y > 0) x + mul(x, y - 1)
  else if (y < 0) -mul(x, -y)
  else -1

  private def div(x: Long, y: Long) = {
    val sign = if ((x < 0) ^ (y < 0)) -1 else 1
    val dividend = Math.abs(x)
    val divisor = Math.abs(y)
    var quotient: Long = 0
    var temp: Long = 0

    for (i <- 31 to 0 by -1) {
      if (temp + (divisor << i) <= dividend) {
        temp += divisor << i
        quotient |= 1L << i
      }
    }
    sign * quotient
  }

  def calculate(num: Long, tmp: Long, op: Char): CalculationResult = {
    if (op == '+') Right(add(num, tmp))
    else if (op == '-') Right(sub(num, tmp))
    else if (op == '*') Right(mul(num, tmp))
    else if (tmp == 0 && op == '/') Left(new IllegalStateException("0 div")) else Right(div(num, tmp))
  }
}
