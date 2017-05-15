package com.lombardo.app.services

import org.slf4j.LoggerFactory
import com.lombardo.app.services.Model._

class WordService {

  val logger =  LoggerFactory.getLogger(getClass)
  val repoService = new RepositoryService
  val redisService = new RedisService

  val tableName = "words"
  val columnName = "canonical_word"

  def findAll(input: String, prefix: String, suffix: String): SearchResult = {
    val subSets = getWordSubsets(input)

    redisService.findAllWithSearch(tableName, columnName, subSets)

    val rawResultSet = repoService.findAllWithSearch(tableName, columnName, subSets) match {
      case Some(results) => results.map(result => new Word(result("word"), result("points").toInt))
      case None => List()
    }

    logger.info(s"""${rawResultSet.length} words found""")

    val suffixFilteredSet = suffix match {
      case "" => rawResultSet
      case _ => rawResultSet.filter(_.word.endsWith(suffix))
    }

    val prefixFilteredSet = prefix match {
      case "" => suffixFilteredSet
      case _ => suffixFilteredSet.filter(_.word.startsWith(prefix))
    }

    new SearchResult(prefixFilteredSet.length, prefixFilteredSet.sortWith(_.points > _.points))
  }


  private def getWordSubsets(word: String): List[String] = {
    val charList = word.split("").toList

//    val output = for {
//      length <- 1 to charList.length
//      combo <- charList.combinations(length)
//    } yield combo.sorted.mkString

    val output2 = (1 to charList.length)
      .flatMap(num => charList.combinations(num))
      .map(chars => chars.sorted.mkString)
      .toList

//    output
    output2
  }
}