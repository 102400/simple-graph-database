package com.vatcore.graphdb.json

final case class Response[T](
  code: Integer,
  message: String,
  data: T
)

object Response {

  val OK: Response[AnyRef] = OK(AnyRef)
  val BadRequest: Response[AnyRef] = Response(
    code = 40000,
    message = "Bad Request",
    data = AnyRef
  )
  val DatabaseNotExist: Response[AnyRef] = Response(
    code = 40001,
    message = "Database Not Exist",
    data = AnyRef
  )
  val ActionError: Response[AnyRef] = Response(
    code = 40002,
    message = "Action Error",
    data = AnyRef
  )

  def OK[T](data: T) = Response(
    code = 20000,
    message = "OK",
    data = data
  )

}

class Q {

  def f = {

    Response.OK

  }
}