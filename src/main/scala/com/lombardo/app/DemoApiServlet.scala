package com.lombardo.app

import org.scalatra._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._

class DemoApiServlet extends DemoapiStack with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats
  case class Greeting(language: String, content: String)

  before() {
    contentType = formats("json")
  }

  get("/") {
    ("message" -> "hello world")
  }

  get("/greetings") {
    val greetings = List(
      Greeting("English", "Hello World"),
      Greeting("Spanish", "Hola Mundo"),
      Greeting("French", "Bonjour le monde"),
      Greeting("Italian", "Ciao mondo")
    )

    greetings
  }
}
