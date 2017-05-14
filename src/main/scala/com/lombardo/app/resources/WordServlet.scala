package com.lombardo.app.resources

import com.lombardo.app.DemoapiStack
import com.lombardo.app.services.Model.SearchResult
import com.lombardo.app.services.WordService
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.HaltException
import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory

class WordServlet extends DemoapiStack with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats
  val logger =  LoggerFactory.getLogger(getClass)
  val wordService = new WordService

  before() {
    contentType = formats("json")
  }

  get("/?:searchTerm") {
    val input = params("searchTerm").toLowerCase
    val suffix = params.getOrElse("suffix", "").toLowerCase
    val prefix = params.getOrElse("prefix", "").toLowerCase

    logger.info(s"""GET /words/$input""")

    if (input.length >= 13) halt(400, "message" -> "search term cannot exceed 13 characters")

    wordService.findAll(input, prefix, suffix)
  }

}
