package com.lombardo.app

import org.scalatra._
import org.scalatra.scalate.ScalateSupport


trait DemoapiStack extends ScalatraServlet with ScalateSupport  {

  notFound {
    println(s"""404 returned from ${request.getMethod} ${request.getRequestURI}""")

    response.setContentType("JSON")
    response.setStatus(404)
    s"""{ "message": "resource not found.  available resources: /words" }"""
  }
}
