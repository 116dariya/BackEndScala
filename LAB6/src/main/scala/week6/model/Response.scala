package week6.model

sealed trait Response

case class SuccessfulResponse(status: Int, message: String) extends Response
case class ErrorResponse(status: Int, message: String) extends Response
case class ItemNotFound(status: Int, message: String) extends Response
case class Updated() extends Response