package com.lombardo.app.services

import java.util.concurrent.TimeUnit

import org.slf4j.LoggerFactory
import com.lombardo.app.services.Model._

class WordService {

  val logger =  LoggerFactory.getLogger(getClass)
  val postgresService = new RepositoryService
  val redisService = new RedisService

  val tableName = "words"
  val columnName = "canonical_word"

  def findAll(input: String, prefix: String, suffix: String): SearchResult = {
    val t0 = System.nanoTime

    val subSets = getWordSubsets(input)

    val rawResultSet = redisService.findAllWithSearch(tableName, columnName, subSets) match {
      case Some(results) => results.map(result => new Word(result("word"), result("points").toInt))
      case None => List()
    }

    val suffixFilteredSet = suffix match {
      case "" => rawResultSet
      case _ => rawResultSet.filter(_.word.endsWith(suffix))
    }

    val prefixFilteredSet = prefix match {
      case "" => suffixFilteredSet
      case _ => suffixFilteredSet.filter(_.word.startsWith(prefix))
    }

    val t1 = System.nanoTime
    val duration = TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS);

    logger.info(s"""${rawResultSet.length} raw results || ${prefixFilteredSet.length} returned after filters applied || elapsed time ${duration} ms""")

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