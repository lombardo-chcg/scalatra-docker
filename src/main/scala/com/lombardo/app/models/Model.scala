package com.lombardo.app.models

object Model {
  case class Greeting(language: String, content: String)

  case class Word(word: String, scrabblePoints: Int, wordsWithFriendsPoints: Int)

  case class SearchResult(count: Int, hits: List[Word])

  case class ServerEvent(server_timestamp: String, user_request: String, user_agent: String, result_size: Int, elapsed_time_ms: Long)
}

trait hasFindAll {
  def findAllWithSearch(resource: String, column: String, searchTermList: List[String]): Option[List[Map[String, String]]]
}