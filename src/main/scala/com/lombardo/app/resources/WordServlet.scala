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
    val lastLetter = params.getOrElse("last", "").toLowerCase
    logger.info(s"""GET /words/$input""")

    if (input.length >= 13) halt(400, "message" -> "cannot exceed 13 characters")
    if (lastLetter.length > 1) halt(400, "message" -> "last letter filter must be one character only")

    val result = wordService.findAll(input)

    lastLetter match {
      case "" => result
      case _ =>
        val filteredList = result.hits.filter(hit => lastLetter == hit.word.charAt(hit.word.length-1).toString)
        val filteredCount = filteredList.length

        new SearchResult(filteredCount, filteredList)
    }
  }

}
