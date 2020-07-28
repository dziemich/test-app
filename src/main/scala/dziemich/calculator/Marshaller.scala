package dziemich.calculator
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json._
import de.heikoseeberger.akkahttpplayjson._

case class Expression(expression: String)
case class Result(result: Long)
case class Error(error: String)

trait CMarshaller extends PlayJsonSupport {
  implicit val expressionMarshaller: OFormat[Expression] = Json.format[Expression]
  implicit val resultMarshaller: OFormat[Result] = Json.format[Result]
  implicit val errorMarshaller: OFormat[Error] = Json.format[Error]
}

//class CustomMarshaller extends Marshaller