package scheduler.api


object ApiMessages {

  case class Message(message: String)

  val UnknownException = "Unknown exception"
  val UnsupportedService = "Sorry, provided service is not supported."
}
