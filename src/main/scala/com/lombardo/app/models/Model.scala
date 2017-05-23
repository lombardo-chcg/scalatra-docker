package com.lombardo.app.models

object Model {
  case class Greeting(language: String, content: String)

  case class Word(word: String, scrabblePoints: Int, wordsWithFriendsPoints: Int)

  case class SearchResult(count: Int, hits: List[Word])
}

trait hasFindAll {
  def findAllWithSearch(resource: String, column: String, searchTermList: List[String]): Option[List[Map[String, String]]]
}