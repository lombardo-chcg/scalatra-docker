package com.lombardo.app.services

import org.slf4j.LoggerFactory

class GreetingService {

  val logger =  LoggerFactory.getLogger(getClass)
  val repoService = new RepositoryService

  case class Greeting(id: Int, language: String, content: String, create_date: String)

  def getAll: List[Greeting] = {
    repoService.findAll("greetings") match {
      case Some(g) => g.map { g => convertToGreeting(g) }
      case None => List()
    }
  }

  def getOne(id: Int): Option[Greeting] = {
    repoService.findOne("greetings", id) match {
      case Some(g) => Some(convertToGreeting(g))
      case None => None
    }
  }

  def create(language: String, content: String): Option[Int] = {
    val insertRequest = Map("language" -> language, "content" -> content)

    repoService.insert("greetings", insertRequest)
  }


  private def convertToGreeting(dbResult: Map[String, String]): Greeting = {
    val greeting = Greeting(dbResult("id").toInt, dbResult("language"), dbResult("content"), dbResult("createDate"))

    greeting
  }
}
