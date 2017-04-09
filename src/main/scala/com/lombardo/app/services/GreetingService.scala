package com.lombardo.app.services

import java.sql.Timestamp

import org.slf4j.LoggerFactory
import com.lombardo.app.services.RepositoryService

class GreetingService {

  val logger =  LoggerFactory.getLogger(getClass)
  val repoService = new RepositoryService

  case class Greeting(id: Int, language: String, content: String, create_date: String)

  def getAll: List[Greeting] = {
    val greetings = repoService
      .findAll("greetings")
      .map { dbResult => convertToGreeting(dbResult) }

    greetings
  }

  def getOne(id: Int): Greeting = {
    val rawGreeting = repoService
      .findOne("greetings", id)

    convertToGreeting(rawGreeting)
  }


  private def convertToGreeting(dbResult: Map[String, String]): Greeting = {
    val greeting = Greeting(dbResult("id").toInt, dbResult("language"), dbResult("content"), dbResult("create_date"))

    greeting
  }
}
