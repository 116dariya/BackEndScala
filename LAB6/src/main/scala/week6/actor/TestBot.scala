package week6.actor

import java.util.Calendar
import scala.io.StdIn
import week6.model.{DateTime, Director, ErrorResponse, ItemNotFound, Movie, SuccessfulResponse, Updated}

object TestBot {

  case object TestCreate

  case object TestConflictCreate

  case object TestRead

  case object TestNotFound

  case object TestDelete

  case object TestUpdate

  //scase object TestConflictUpdate

  def props(manager: ActorRef) = Props(
    new TestBot(manager)
  )
}

class TestBot(manager: ActorRef) extends Actor with ActorLogging {

  import TestBot._

  override def receive: Receive = {
    //Create
    case TestCreate =>
      manager ! MovieManager.CreateMovie(
        Movie(
          "1",
          "Joker",
          Director("dir-1", "Todd", "Philips"),
          Calendar.getInstance()
        )
      )

    case TestConflictCreate =>
      manager ! MovieManager.CreateMovie(
        Movie(
          "2",
          "Charlie's Angels",
          Director("dir-2", "Ivan", "Ivanov"),
          Calendar.getInstance()
        )
      )

      manager ! MovieManager.CreateMovie(
        Movie(
          "2",
          "Test Test",
          Director("dir-2", "Ivan", "Ivanov"),
          Calendar.getInstance()
        )
      )

    //Test Read
    case TestRead =>
      manager ! MovieManager.ReadMovie("1")

    //Test notFound
    case TestNotFound =>
      manager ! MovieManager.ReadMovie("60")

      manager ! MovieManager.UpdateMovie(
        Movie(
          "10",
          "Charlie's Angels",
          Director("dir-2", "Ivan", "Ivanov"),
          Calendar.getInstance()
        )
      )

      manager ! MovieManager.DeleteMovie("99")

    // test Update
    case TestUpdate =>
      manager ! MovieManager.UpdateMovie
      (
        Movie(
          "2",
          "Test Test",
          Director("dir-2", "Ivan", "Ivanov"),
          Calendar.getInstance()
        )
        )

    //    case TestConflictUpdate =>
    //      manager ! MovieManager.UpdateMovie(Movie("77", "Test Test", Director("dir-2", "Ivan", "Ivanov"), 2019))

    //Test Delete
    case TestDelete =>
      manager ! MovieManager.DeleteMovie("1")


    case SuccessfulResponse(status, msg) =>
      log.info("Received Successful Response with status: {} and message: {}", status, msg)

    case ErrorResponse(status, msg) =>
      log.warning("Received Error Response with status: {} and message: {}", status, msg)

    case movie: Movie =>
      log.info("Received movie: [{}]", movie)

    case ItemNotFound(status, msg) =>
      log.warning("Item not Found Response with status: {} and message: {}", status, msg)

    case Updated: Movie =>
      log.info("Updated movie: [{}]", Updated)
  }
}