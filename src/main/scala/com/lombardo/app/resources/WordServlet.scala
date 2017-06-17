package com.lombardo.app.resources

import com.lombardo.app.DemoapiStack
import com.lombardo.app.models.Model.{SearchResult, ServerEvent}
import com.lombardo.app.services.{LoggerService, WordService}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.HaltException
import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory
import com.lombardo.app.utils.ApiUtils
import java.util.Calendar

class WordServlet extends DemoapiStack with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats
  val logger =  LoggerFactory.getLogger(getClass)
  val serverLogger = new LoggerService
  val wordService = new WordService

  before() {
    contentType = formats("json")
  }

  get("/?") {
    logger.info(s"""${request.getMethod} ${request.getRequestURI}""")


    ApiUtils.json("scrabble helper!  sample resource usage: GET /words/some*hing?prefix=mo&suffix=h*")
  }

  get("/?:searchTerm") {
    val startTime = System.currentTimeMillis
    val requestURI = s"""${request.getRequestURI} ${if (request.getQueryString != null) request.getQueryString else "" }"""

    logger.info(s"""${request.getMethod} $requestURI""")

    val input = params.getOrElse("searchTerm", "").toLowerCase
    val suffix = params.getOrElse("suffix", "").toLowerCase
    val prefix = params.getOrElse("prefix", "").toLowerCase
    val sortBy = params.getOrElse("sortBy", "").toLowerCase

    if (!input.matches("""[a-zA-Z*]+""")) halt(400, ApiUtils.json("invalid input.  only letters and wildcard(*) are allowed"))
    if (input.length >= 15)               halt(400, ApiUtils.json("search term cannot exceed 15 characters"))
    if (input.count(_ == '*') > 2)        halt(400, ApiUtils.json("only two wildcards are allowed"))

    val result = wordService.findAll(input, prefix, suffix, sortBy)

    val log = ServerEvent(System.currentTimeMillis.toString, requestURI, request.getHeader("User-Agent"), result.count, System.currentTimeMillis - startTime)
    serverLogger.logEvent(log)

    result
  }

}
