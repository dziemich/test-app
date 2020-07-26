//package dziemich.calculator
//
//import scala.collection.mutable
//
//class Validator {
//  val operators: Array[Char] = Array('+', '-', '*', '/')
//  val charsAllowed: Array[Char] = Array('(', ')') ++ operators
//
//  def isAllowed(c: Char): Boolean = charsAllowed.contains(c) || c.isDigit
//
//  def validateFirst(c: Char): Boolean = c == '(' || c.isDigit
//
//  def validate(string: String): (Boolean, String) = {
//    var cleaned = string.filterNot((x: Char) => x.isWhitespace)
//    if (cleaned.length <= 2) return (false, cleaned)
//    var stack: mutable.Stack[Char] = mutable.Stack()
//
//    val first = cleaned.charAt(0)
//    if (first == '-') cleaned = "0".concat(cleaned)
//    if (!validateFirst(first)) return (false, cleaned)
//
//    for (i <- 1 until cleaned.length) {
//      val c = cleaned.charAt(i)
//      if (!isAllowed(c)) return (false, cleaned)
//      if (charsAllowed.contains(c) && cleaned.charAt(i - 1))
//      c match {
//        case '(' => stack.push(c)
//        case ')' => {
//          if (stack.top == '(') {
//            stack.pop()
//          } else return (false, cleaned)
//        }
//        case op if operators.contains(op) => {
//          if (operators.contains(cleaned.charAt(i - 1))) return (false, cleaned)
//        }
//      }
//    }
//    return (stack.isEmpty, cleaned)
//  }
//}
