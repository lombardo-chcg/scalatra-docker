package com.lombardo.app.resources

import org.json4s._
import org.scalatra.json._
import org.slf4j.LoggerFactory
import com.lombardo.app._
import com.lombardo.app.services.GreetingService

case class Greeting(language: String, content: String)

class GreetingServlet extends DemoapiStack with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats
  val logger =  LoggerFactory.getLogger(getClass)
  val greetingService = new GreetingService()

  before() {
    contentType = formats("json")
  }

  get("/?") {
    logger.info("GET /greetings")

    greetingService.getAll
  }

  get ("/:id") {
    logger.info("GET /greetings/" + params("id"))

    try {
      val id = params("id").toInt

      greetingService.getOne(id) match {
        case Some(g) => g
        case None => response.setStatus(404)
          jsonResponse(s"""greeting with id $id does not exist""")
      }
    } catch {
        case e: NumberFormatException => response.setStatus(400)
          jsonResponse("param must be valid number")
    }
  }

  post ("/?") {
    logger.info("POST /greetings" + request.body)


    val greeting = parsedBody.extract[Greeting]
    logger.info(greeting.toString)
    try {
      val json = parse(request.body)
      val language = (json \ "language").extract[String]
      val content = (json \ "content").extract[String]

      greetingService.create(language, content) match {
        case Some(i) => response.setStatus(201)
          "id" -> i
        case None => response.setStatus(404)
          jsonResponse("invalid request")
      }
    } catch {
      case _ => response.setStatus(404)
        jsonResponse("invalid input")
    }
  }

  private def jsonResponse(message: String) : (String, String) = {
    "message" -> message
  }
}
