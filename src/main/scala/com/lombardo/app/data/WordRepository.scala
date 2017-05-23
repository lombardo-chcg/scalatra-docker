package com.lombardo.app.data

import org.slf4j.LoggerFactory

import scala.io.Source

object WordRepository {
  val logger =  LoggerFactory.getLogger(getClass)
  val inputFile = "./db/word_list.txt"
  val scrabblePointsMap = Map(
    "a" -> 1,
    "b" -> 3,
    "c" -> 3,
    "d" -> 2,
    "e" -> 1,
    "f" -> 4,
    "g" -> 2,
    "h" -> 4,
    "i" -> 1,
    "j" -> 8,
    "k" -> 5,
    "l" -> 1,
    "m" -> 3,
    "n" -> 1,
    "o" -> 1,
    "p" -> 3,
    "q" -> 10,
    "r" -> 1,
    "s" -> 1,
    "t" -> 1,
    "u" -> 4,
    "v" -> 4,
    "w" -> 4,
    "x" -> 8,
    "y" -> 4,
    "z" -> 10
  )
  val wordsWithFriendsPointsMap = Map(
    "a" -> 1,
    "b" -> 4,
    "c" -> 4,
    "d" -> 2,
    "e" -> 1,
    "f" -> 4,
    "g" -> 3,
    "h" -> 3,
    "i" -> 1,
    "j" -> 10,
    "k" -> 5,
    "l" -> 2,
    "m" -> 4,
    "n" -> 2,
    "o" -> 1,
    "p" -> 4,
    "q" -> 10,
    "r" -> 1,
    "s" -> 1,
    "t" -> 1,
    "u" -> 2,
    "v" -> 5,
    "w" -> 4,
    "x" -> 8,
    "y" -> 3,
    "z" -> 10
  )
  val validChars = """[a-zA-Z]+""".r

  val wordMap = Source.fromFile(inputFile)
    .getLines
    .filter(_.matches(validChars.toString))
    .map(_.toLowerCase)
    .toList
    .groupBy(word => word.split("").sorted.mkString(""))
    .mapValues(wordList => {
      wordList.map(word => {
        val scrabblePoints = word.split("").map(l => scrabblePointsMap(l)).foldLeft(0)(_ + _)
        val wordsWithFriendsPoints = word.split("").map(l => wordsWithFriendsPointsMap(l)).foldLeft(0)(_ + _)

        s"""$word,$scrabblePoints,$wordsWithFriendsPoints"""
      })
    })


  def data: Map[String, List[String]] = {
    wordMap
  }

  def test() = {
    try {
      logger.info(s"""data created: repository size ${wordMap.size.toString}""")
    } catch {
      case e : Throwable => logger.error(e.getMessage)
    }
  }
}
