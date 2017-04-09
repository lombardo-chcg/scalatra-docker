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

  def findAll(resource: String) : List[Map[String, String]] = {
    try {
      Class.forName("org.postgresql.Driver")
      val pgConnection = DriverManager.getConnection(postgresUri, postgresUsername, postgresPassword)

      logger.info("PG connection established")

      val dbMetaData = pgConnection.getMetaData
      val columns = getColumnNames(dbMetaData, resource)

      val sql = "select * from " + resource
      val resultSet = pgConnection.createStatement.executeQuery(sql)
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

  def findOne(resource: String, id: Int) : Map[String, String] = {
    try {
      Class.forName("org.postgresql.Driver")
      val pgConnection = DriverManager.getConnection(postgresUri, postgresUsername, postgresPassword)

      logger.info("PG connection established")

      val dbMetaData = pgConnection.getMetaData
      val columns = getColumnNames(dbMetaData, resource)

      val sql = "select * from " + resource + " where id = " + id
      val resultSet = pgConnection.createStatement.executeQuery(sql)
      val output = Map.newBuilder[String, String]

      if (resultSet.next) {
        columns.foreach(col =>
          output += (col -> resultSet.getString(col))
        )
      }

      resultSet.close
      pgConnection.close

      output.result
    } catch {
      case e =>

        val errorText = e.toString

        logger.error(errorText)
        Map()
    }
  }

  private def getColumnNames(metaData: DatabaseMetaData, tableName: String): List[String] = {
    val cols = metaData.getColumns(null, null, tableName, null)
    val columnNames = List.newBuilder[String]

    while (cols.next) {
      columnNames += cols.getString("COLUMN_NAME")
    }

    columnNames.result
  }
}
