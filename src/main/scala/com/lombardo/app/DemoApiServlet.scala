package com.lombardo.app

import org.scalatra._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import org.slf4j.{Logger, LoggerFactory}

class DemoApiServlet extends DemoapiStack with JacksonJsonSupport {

  case class Greeting(language: String, content: String)

  protected implicit val jsonFormats: Formats = DefaultFormats
  val logger =  LoggerFactory.getLogger(getClass)

  before() {
    contentType = formats("json")
  }

  get("/") {
    logger.info("GET /")
    ("message" -> "hello world")
  }

  get("/greetings") {
    logger.info("GET /greetings")
    val greetings = List(
      Greeting("English", "Hello World"),
      Greeting("Spanish", "Hola Mundo"),
      Greeting("French", "Bonjour le monde"),
      Greeting("Italian", "Ciao mondo")
    )

    greetings
  }
}
