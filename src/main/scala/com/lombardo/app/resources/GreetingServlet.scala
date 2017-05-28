package com.lombardo.app.resources

import org.json4s._
import org.scalatra.json._
import org.slf4j.LoggerFactory
import com.lombardo.app._
import com.lombardo.app.services.GreetingService
import com.lombardo.app.models.Model._
import com.lombardo.app.utils.ApiUtils

class GreetingServlet extends DemoapiStack with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats
  val logger =  LoggerFactory.getLogger(getClass)
  val greetingService = new GreetingService()

  before() {
    contentType = formats("json")
  }

  notFound {
    ApiUtils.json("resource not found")
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
          ApiUtils.json(s"""greeting with id $id does not exist""")
      }
    } catch {
        case e : Throwable => response.setStatus(400)
          ApiUtils.json("param must be valid number")
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
          ApiUtils.json("invalid request")
      }
    } catch {
      case e : Throwable => response.setStatus(404)
        ApiUtils.json("invalid input")
    }
  }
}
