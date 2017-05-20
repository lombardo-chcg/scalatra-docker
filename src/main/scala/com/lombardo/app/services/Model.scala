package com.lombardo.app.services

object Model {
  case class Greeting(language: String, content: String)
  case class Word(word: String, points: Int)
  case class SearchResult(count: Int, hits: List[Word])
}
