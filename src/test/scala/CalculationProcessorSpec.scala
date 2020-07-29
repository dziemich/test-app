import dziemich.calculator.processing.CalculationProcessor
import dziemich.calculator.utils.ProcessingError
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should

class CalculationProcessorSpec extends AnyFunSpec with should.Matchers {
  
  describe("CalculationProcessor") {
    it("should compute proper result for basic operations (\'+\', \'-\',\'*\',\'/\') operations with no parentheses") {
      CalculationProcessor.calcRec("1+4") should be(Right(1 + 4))
      CalculationProcessor.calcRec("3-1") should be(Right(3 - 1))
      CalculationProcessor.calcRec("2*3") should be(Right(2 * 3))
      CalculationProcessor.calcRec("8/2") should be(Right(8 / 2))
    }

    it("should handle longer numbers") {
      CalculationProcessor.calcRec("10+40") should be(Right(10 + 40))
      CalculationProcessor.calcRec("30-10") should be(Right(30 - 10))
      CalculationProcessor.calcRec("20*30") should be(Right(20 * 30))
      CalculationProcessor.calcRec("80/20") should be(Right(80 / 20))
    }

    it("should handle order of execution in expressions") {
      CalculationProcessor.calcRec("2+2*2") should be(Right(2 + 2 * 2))
      CalculationProcessor.calcRec("4-4/4") should be(Right(4 - 4 / 4))
    }

    it("should handle order of execution in expressions with parenthesis") {
      CalculationProcessor.calcRec("(2+2)*2") should be(Right((2 + 2) * 2))
      CalculationProcessor.calcRec("(8-4)/4") should be(Right((8 - 4) / 4))
    }

    it("should handle order of execution in expressions with nested parenthesis") {
      CalculationProcessor.calcRec("(12-(2+2))*2") should be(Right((12 - (2 + 2)) * 2))
      CalculationProcessor.calcRec("(12-(8-4))/4") should be(Right((12 - (8 - 4)) / 4))
    }

    it("should handle single negative number in parenthesis") {
      CalculationProcessor.calcRec("4*(-2)") should be(Right(4 * (-2)))
      CalculationProcessor.calcRec("4/(-2)") should be(Right(4 / (-2)))
      CalculationProcessor.calcRec("4+(-2)") should be(Right(4 + (-2)))
      CalculationProcessor.calcRec("4-(-2)") should be(Right(4 - (-2)))
      CalculationProcessor.calcRec("-4*(-2)") should be(Right(-4 * (-2)))
      CalculationProcessor.calcRec("-4/(-2)") should be(Right(-4 / (-2)))
      CalculationProcessor.calcRec("-4+(-2)") should be(Right((-4 + (-2))))
      CalculationProcessor.calcRec("-4-(-2)") should be(Right(-4 - (-2)))
    }
    
    it("should handle long overflow") {
      CalculationProcessor.calcRec("5555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555+1") should be(Left(ProcessingError.LONG_OVERFLOW))
    }
    
    it("should handle division by 0") {
      CalculationProcessor.calcRec("1/(4-4)") should be(Left(ProcessingError.DIVISION_BY_ZERO))
    }
  }
}
