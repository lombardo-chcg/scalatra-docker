package com.lombardo.app.resources

import com.lombardo.app.DemoapiStack
import com.lombardo.app.util.Util
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory

class DefaultServlet extends DemoapiStack with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats
  val logger =  LoggerFactory.getLogger(getClass)

  get("/?") {
    logger.info(s"""${request.getMethod} ${request.getRequestURI}""")

    response.setStatus(404)
    Util.json("resource not found.  available resources: /words")
  }


}
