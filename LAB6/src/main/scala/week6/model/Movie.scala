package week6.model

//import org.joda.time.{DateTime, DateTimeZone}
// TODO: yearOfRelease to year (DateTime)

/**
 * Model of a movie
 * @param id unique ID of a movie
 * @param title
 * @param director
 * @param yearOfRelease
 */

import java.time.Year
import java.util.Calendar
case class Movie(id: String, title: String, director: Director, yearOfRelease: Calendar)
//case class Movie(id: String, title: String, director: Director, yearOfRelease: DateTime.now)