package com.lombardo.app.services

import org.slf4j.LoggerFactory
import com.lombardo.app.services.Model._

class WordService {

  val logger =  LoggerFactory.getLogger(getClass)
  val repoService = new RepositoryService

  val tableName = "words"
  val columnName = "canonical_word"

  def findAll(input: String): SearchResult = {
    val subSets = getWordSubsets(input)

    val hits = repoService.findAllWithSearch(tableName, columnName, subSets) match {
      case Some(results) => results.map(result => new Word(result("word"), result("points").toInt))
      case None => List()
    }

    logger.info(s"""${hits.length} words found""")
    val results = new SearchResult(hits.length, hits.sortWith(_.points > _.points))

    results
  }

  private def getWordSubsets(word: String): List[String] = {
    val charList = word.split("").toList

    val output = for {
      length <- 1 to charList.length
      combo <- charList.combinations(length)
    } yield combo.sorted.mkString

    val output2 = (1 to charList.length)
      .flatMap(num => charList.combinations(num))
      .map(chars => chars.sorted.mkString)
      .toList

    output.toList
    output2
  }
}