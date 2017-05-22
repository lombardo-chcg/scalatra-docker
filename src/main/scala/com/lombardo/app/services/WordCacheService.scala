package com.lombardo.app.services

import com.lombardo.app.data.WordRepository
import com.lombardo.app.models.hasFindAll
import org.slf4j.LoggerFactory

class WordCacheService extends hasFindAll {

  val logger =  LoggerFactory.getLogger(getClass)

  def findAllWithSearch(resource: String, column: String, searchTermList: List[String]) : Option[List[Map[String, String]]] = {
    logger.info(s"""Making ${searchTermList.length} Internal Cache Requests requests""")

    val hits = searchTermList
      .flatMap(WordRepository.data.get)
      .flatten
      .map(result => {
        val split = result.split(",")

        Map("word" -> split(0), "scrabblePoints" -> split(1), "wordsWithFriendsPoints" -> split(2))
      })

    Some(hits)
  }
}
