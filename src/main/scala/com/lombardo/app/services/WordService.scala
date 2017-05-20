package com.lombardo.app.services

import org.slf4j.LoggerFactory
import com.lombardo.app.services.Model._

class WordService {

  val logger =  LoggerFactory.getLogger(getClass)
  val postgresService = new RepositoryService
  val redisService = new RedisService

  val tableName = "words"
  val columnName = "canonical_word"

  def findAll(input: String, prefix: String, suffix: String): SearchResult = {
    val t0 = System.currentTimeMillis

    val subSets = getWordSubsets(input).filter(_.matches(".*[aeiouy]+.*"))

    val rawResultSet = redisService.findAllWithSearch(tableName, columnName, subSets) match {
      case Some(results) => results.map(result => Word(result("word"), result("points").toInt))
      case None => List()
    }

    val suffixFilteredSet = suffix match {
      case "" => rawResultSet
      case _ =>
        val scrubbedSuffix = suffix.replaceAll("[*]", ".")
        val matcher = s""".*$scrubbedSuffix$$"""

        rawResultSet.filter(_.word.matches(matcher))
    }

    val prefixFilteredSet = prefix match {
      case "" => suffixFilteredSet
      case _ =>
        val scrubbedPrefix = prefix.replaceAll("[*]", ".")
        val matcher = s"""^$scrubbedPrefix.*"""

        suffixFilteredSet.filter(_.word.matches(matcher))
    }

    val t1 = System.currentTimeMillis
    val duration = t1 - t0

    logger.info(s"""${rawResultSet.length} raw results || ${prefixFilteredSet.length} returned after filters applied || elapsed time ${duration} ms""")

    SearchResult(prefixFilteredSet.length, prefixFilteredSet.sortWith(_.points > _.points))
  }


  private def getWordSubsets(word: String): List[String] = {
    val charList = word.split("").toList

    word.contains("*") match {
      case true =>
        val cleanWord = word.replaceFirst("[*]", "")
        val wildCardSet = (97 to 122)
          .flatMap(i => { getWordSubsets(cleanWord + i.toChar.toString) })
          .toSet
          .toList

        wildCardSet
      case false =>
        val combinations = (1 to charList.length)
          .flatMap(charList.combinations)
          .map(_.sorted.mkString)
          .toList

        combinations
    }
  }
}