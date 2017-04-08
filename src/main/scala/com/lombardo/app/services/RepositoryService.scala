package com.lombardo.app.services

import java.sql._

import org.postgresql._
import org.slf4j.LoggerFactory

import scala.collection.immutable.HashMap

class RepositoryService {

  val logger =  LoggerFactory.getLogger(getClass)
  val postgresUri = "jdbc:postgresql://0.0.0.0:5431/greeting"
  val postgresUsername = "postgres"
  val postgresPassword = "postgres"

  def findAll(resource: String, columns: List[String]) : List[Map[String, String]] = {
    try {

      Class.forName("org.postgresql.Driver")
      val pgConnection = DriverManager.getConnection(postgresUri, postgresUsername, postgresPassword)

      logger.info("PG connection established")

      val sql = "select * from " + resource
      val resultSet = pgConnection.createStatement.executeQuery(sql)

      logger.info("Reset set retrieved")

      val output = List.newBuilder[Map[String, String]]

      while (resultSet.next) {
        val currentCol = Map.newBuilder[String, String]

        columns.foreach(col =>
          currentCol += (col -> resultSet.getString(col))
        )

        output += currentCol.result
      }

      resultSet.close
      pgConnection.close

      output.result
    } catch {
      case e =>

        val errorText = e.toString

        logger.error(errorText)
        List()
    }
  }
}
