package com.lombardo.app.resources

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import org.slf4j.LoggerFactory
import com.lombardo.app._
import com.lombardo.app.services.GreetingService

class GreetingServlet extends DemoapiStack with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats
  val logger =  LoggerFactory.getLogger(getClass)
  val greetingService = new GreetingService()


  before() {
    contentType = formats("json")
  }

  get("/greetings") {
    logger.info("GET /greetings")

    greetingService.getAll
  }
}
