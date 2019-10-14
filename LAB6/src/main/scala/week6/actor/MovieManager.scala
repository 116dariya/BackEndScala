package week6.actor

import scala.actors.Actor
import scala.actors.Actor._
import akka.actor.{Actor, ActorLogging, Props}
import week6.model.{ErrorResponse, Movie, SuccessfulResponse, Updated, ItemNotFound}
import scala.io.StdIn

// props
// messages
object MovieManager {

  case class CreateMovie(movie: Movie)
  case class ReadMovie(id: String)
  case class UpdateMovie(movie: Movie)
  case class DeleteMovie(id: String)

  def props() = Props(new MovieManager)
}

// know about existing movies
// can create a movie
// can manage movie
class MovieManager extends Actor with ActorLogging {

  // import companion OBJECT

  override def preStart(): Unit = log.info("IoT Application started")
  override def postStop(): Unit = log.info("IoT Application stopped")


  import MovieManager._

  var movies: Map[String, Movie] = Map()

  override def receive: Receive = {

    case CreateMovie(movie) =>
      movies.get(movie.id) match {
        case Some(existingMovie) =>
          log.warning(s"Could not create a movie with ID: ${movie.id} because it already exists.")
          sender() ! ErrorResponse(409, s"Movie with ID: ${movie.id} already exists.")

        case None =>
          movies = movies + (movie.id -> movie)
          log.info("Movie with ID: {} created.", movie.id)
          sender() ! SuccessfulResponse(201, s"Movie with ID: ${movie.id} created.")
      }

    case msg: ReadMovie =>
      movies.get(msg.id) match {
        case Some(movie) =>
          log.info(s"Movie with ID = $id was read.")
          sender() ! movie

        case None =>
          log.warning(s"Could not read a movie with ID: $id because it already exists.")
          sender() ! ErrorResponse(404, s"Movie with ID: ${msg.id} not found.")
      }


    case UpdateMovie(movie) =>
      movies.get(movie.id) match {
        case Some(existingMovie) =>
          movies += (movie.id -> movie)
          log.info("Movie with ID: {} updated.", movie.id)
          sender() ! Updated
        case None => ErrorResponse(404, s"Movie with ID: $id not found.")
      }



    case msg: DeleteMovie =>
      movies.get(msg.id) match {
        case Some(existingMovie) =>
          movies -= msg.id
          log.info(s"Movie with ID = $id was deleted.")
          sender() ! SuccessfulResponse(200, s"Movie with ID = $id successfully deleted.")
        case None => ItemNotFound(404, s"Movie with ID: ${msg.id} not found.")
      }


      def randomInt(): Int = {
        // FIXME: use random
        val r = new scala.util.Random
        val r1 = 0 + r.nextInt((1000 - 0) + 1)
        r1
      }

  }
}