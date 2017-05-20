package com.lombardo.app

import org.scalatra._
import scalate.ScalateSupport
import org.fusesource.scalate.{ TemplateEngine, Binding }
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import javax.servlet.http.HttpServletRequest
import collection.mutable
import com.lombardo.app.util.Util

trait DemoapiStack extends ScalatraServlet with ScalateSupport {

  notFound {
    response.setStatus(404)
    Util.json("resource not found.  available resources: /words")
  }
}
