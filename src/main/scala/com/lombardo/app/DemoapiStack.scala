package com.lombardo.app

import org.scalatra._
import scalate.ScalateSupport
import org.fusesource.scalate.{Binding, TemplateEngine}
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import javax.servlet.http.HttpServletRequest

import collection.mutable
import com.lombardo.app.util.Util
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport

trait DemoapiStack extends ScalatraServlet with ScalateSupport with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  notFound {
    response.setStatus(404)
    Util.json("resource not found.  available resources: /words")
  }
}
