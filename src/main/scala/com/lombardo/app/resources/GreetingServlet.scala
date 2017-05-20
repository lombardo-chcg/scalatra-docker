package com.lombardo.app.resources

import org.json4s._
import org.scalatra.json._
import org.slf4j.LoggerFactory
import com.lombardo.app._
import com.lombardo.app.services.GreetingService
import com.lombardo.app.services.Model._
import com.lombardo.app.util.Util

class GreetingServlet extends DemoapiStack with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats
  val logger =  LoggerFactory.getLogger(getClass)
  val greetingService = new GreetingService()

  before() {
    contentType = formats("json")
  }

  notFound {
    Util.json("resource not found")
  }

  get("/?") {
    logger.info(s"""${request.getMethod} ${request.getRequestURI}""")

    greetingService.getAll
  }

  get ("/:id") {
    logger.info(s"""${request.getMethod} ${request.getRequestURI}""")

    try {
      val id = params("id").toInt

      greetingService.getOne(id) match {
        case Some(g) => g
        case None => response.setStatus(404)
          Util.json(s"""greeting with id $id does not exist""")
      }
    } catch {
        case e: NumberFormatException => response.setStatus(400)
          Util.json("param must be valid number")
    }
  }

  post ("/?") {
    logger.info(s"""${request.getMethod} ${request.getRequestURI} ${request.body.filter(_ >= ' ')}""")

    val greeting = parsedBody.extract[Greeting]

    try {
      val json = parse(request.body)
      val language = (json \ "language").extract[String]
      val content = (json \ "content").extract[String]

      greetingService.create(language, content) match {
        case Some(i) => response.setStatus(201)
          "id" -> i
        case None => response.setStatus(404)
          Util.json("invalid request")
      }
    } catch {
      case _ => response.setStatus(404)
        Util.json("invalid input")
    }
  }
}
