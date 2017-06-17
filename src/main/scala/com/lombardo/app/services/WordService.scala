package com.lombardo.app.services

import org.slf4j.LoggerFactory
import com.lombardo.app.models.Model._
import com.lombardo.app.utils.ApiUtils

class WordService {
  val logger =  LoggerFactory.getLogger(getClass)
  val tableName = "words"
  val columnName = "canonical_word"

  val wordRepository = sys.env("REPOSITORY_SERVICE") match {
    case "POSTGRES" => new RepositoryService
    case "REDIS" => new RedisService
    case "LOCAL" => new WordCacheService
    case _ => new WordCacheService
  }

  def findAll(input: String, prefix: String, suffix: String, sortBy: String): SearchResult = {

    val subSets = ApiUtils.opTimer("getWordSubsets", {
      getWordSubsets(input).filter(_.matches(".*[aeiouy]+.*"))
    })

    val rawResultSet = ApiUtils.opTimer("wordRepository.findAllWithSearch", {
      wordRepository.findAllWithSearch(tableName, columnName, subSets) match {
        case Some(results) => results.map(r => Word(r("word"), r("scrabblePoints").toInt, r("wordsWithFriendsPoints").toInt))
        case None => List()
      }
    })

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

    logger.info(s"""${rawResultSet.length} raw results || ${prefixFilteredSet.length} returned after filters applied""")

    val resultSet = sortBy match {
      case "wordswithfriendspoints" => prefixFilteredSet.sortBy(- _.wordsWithFriendsPoints)
      case "scrabblepoints" => prefixFilteredSet.sortBy(- _.scrabblePoints)
      case "alpha" | "alphabetical" => prefixFilteredSet.sortBy(_.word)
      case "length" => prefixFilteredSet.sortBy(- _.word.length)
      case _ => prefixFilteredSet.sortBy(- _.wordsWithFriendsPoints)
    }

    SearchResult(resultSet.length, resultSet)
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