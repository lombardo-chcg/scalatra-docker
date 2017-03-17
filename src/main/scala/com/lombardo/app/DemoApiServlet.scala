package com.lombardo.app

import org.scalatra._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._

class DemoApiServlet extends DemoapiStack with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    ("message" -> "hello world")
  }

}
