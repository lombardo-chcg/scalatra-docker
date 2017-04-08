package com.lombardo.app.services

import org.slf4j.LoggerFactory
import com.lombardo.app.services.RepositoryService

class GreetingService {

  val logger =  LoggerFactory.getLogger(getClass)
  val repoService = new RepositoryService

  case class Greeting(id: Int, language: String, content: String)
  val classProps = List("id", "language", "content")

  def getAll : Any = {
    val greetings = repoService
      .findAll("greetings", classProps)
      .map { dbResult => convertToGreeting(dbResult) }

    greetings
  }


  private def convertToGreeting(dbResult: Map[String, String]): Greeting = {
    val greeting = Greeting(dbResult("id").toInt, dbResult("language"), dbResult("content"))

    greeting
  }
}
