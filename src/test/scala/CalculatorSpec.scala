import dziemich.calculator.actors.Calculator
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should

class CalculatorSpec extends AnyFunSpec with should.Matchers {
  val calculator: Calculator = new Calculator()
  
  describe("Calculator") {
    it("should compute proper result for basic operations (\'+\', \'-\',\'*\',\'/\') operations with no parentheses") {
      calculator.calcRec("1+4") should be(Right(1 + 4))
      calculator.calcRec("3-1") should be(Right(3 - 1))
      calculator.calcRec("2*3") should be(Right(2 * 3))
      calculator.calcRec("8/2") should be(Right(8 / 2))
    }

    it("should handle longer numbers") {
      calculator.calcRec("10+40") should be(Right(10 + 40))
      calculator.calcRec("30-10") should be(Right(30 - 10))
      calculator.calcRec("20*30") should be(Right(20 * 30))
      calculator.calcRec("80/20") should be(Right(80 / 20))
    }

    it("should handle order of execution in expressions") {
      calculator.calcRec("2+2*2") should be(Right(2 + 2 * 2))
      calculator.calcRec("4-4/4") should be(Right(4 - 4 / 4))
    }

    it("should handle order of execution in expressions with parenthesis") {
      calculator.calcRec("(2+2)*2") should be(Right((2 + 2) * 2))
      calculator.calcRec("(8-4)/4") should be(Right((8 - 4) / 4))
    }

    it("should handle order of execution in expressions with nested parenthesis") {
      calculator.calcRec("(12-(2+2))*2") should be(Right((12 - (2 + 2)) * 2))
      calculator.calcRec("(12-(8-4))/4") should be(Right((12 - (8 - 4)) / 4))
    }

    it("should handle single negative number in parenthesis") {
      calculator.calcRec("4*(-2)") should be(Right(4 * (-2)))
      calculator.calcRec("4/(-2)") should be(Right(4 / (-2)))
      calculator.calcRec("4+(-2)") should be(Right(4 + (-2)))
      calculator.calcRec("4-(-2)") should be(Right(4 - (-2)))
      calculator.calcRec("-4*(-2)") should be(Right(-4 * (-2)))
      calculator.calcRec("-4/(-2)") should be(Right(-4 / (-2)))
      calculator.calcRec("-4+(-2)") should be(Right((-4 + (-2))))
      calculator.calcRec("-4-(-2)") should be(Right(-4 - (-2)))
    }
  }
}
