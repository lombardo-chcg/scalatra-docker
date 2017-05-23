package com.lombardo.app.services

import java.sql._

import com.lombardo.app.connectors.dbConnector
import com.lombardo.app.models.hasFindAll
import org.slf4j.LoggerFactory

class RepositoryService extends hasFindAll {

  val logger =  LoggerFactory.getLogger(getClass)

  def findAll(resource: String) : Option[List[Map[String, String]]] = {
    try {
      val pgConnection = dbConnector.getConnection
      val sql = "select * from " + resource
      val allRows = List.newBuilder[Map[String, String]]

      val dbMetaData = pgConnection.getMetaData
      val columns = getColumnNames(dbMetaData, resource)
      val resultSet = pgConnection.createStatement.executeQuery(sql)

      while (resultSet.next) {
         allRows += getRow(resultSet, columns)
      }

      resultSet.close
      dbConnector.close(pgConnection)

      Some(allRows.result)
    } catch {
      case e : Throwable =>

        val errorText = e.toString

        logger.error(errorText)
        None
    }
  }

  def findAllWithSearch(resource: String, column: String, searchTermList: List[String]) : Option[List[Map[String, String]]] = {
    try {
      val pgConnection = dbConnector.getConnection
      val searchHits = List.newBuilder[Map[String, String]]

      val dbMetaData = pgConnection.getMetaData
      val columns = getColumnNames(dbMetaData, resource)

      logger.info(s"""making ${searchTermList.length} sql requests""")

      for (i <- searchTermList) {
        val sql = s"""select * from $resource where $column='$i'"""
        val resultSet = pgConnection.createStatement.executeQuery(sql)

        while (resultSet.next) {
          searchHits += getRow(resultSet, columns)
        }

        resultSet.close
      }

      dbConnector.close(pgConnection)

      Some(searchHits.result)
    } catch {
      case e : Throwable =>

        val errorText = e.toString

        logger.error("error")
        None
    }
  }

  def findOne(resource: String, id: Int) : Option[Map[String, String]] = {
    try {
      val pgConnection = dbConnector.getConnection
      val sql = "select * from " + resource + " where id = " + id

      val dbMetaData = pgConnection.getMetaData
      val columns = getColumnNames(dbMetaData, resource)
      val resultSet = pgConnection.createStatement.executeQuery(sql)

      resultSet.next

      val row = getRow(resultSet, columns)

      resultSet.close
      dbConnector.close(pgConnection)

      Some(row)
    } catch {
      case e : Throwable =>

        val errorText = e.toString

        logger.error(errorText)
        None
    }
  }

  def insert(resource: String, insertReq: Map[String, String]): Option[Int] = {
    try {
      val pgConnection = dbConnector.getConnection
      val cols = insertReq.keys.mkString(", ")
      val vals = insertReq.values.mkString("', '")
      val sql = s"insert into $resource ($cols) values ('$vals')"

      logger.info(sql)

      val stmt = pgConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

      stmt.executeUpdate;
      val resultSet = stmt.getGeneratedKeys

      resultSet.next

      val row = resultSet.getInt("ID")

      stmt.close
      dbConnector.close(pgConnection)

      Some(row)
    } catch {
      case e : Throwable =>

        val errorText = e.toString

        logger.error(errorText)
        None
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

  private def getRow(resultSet: ResultSet, columns: List[String]): Map[String, String] = {
    val row = Map.newBuilder[String, String]

    columns.foreach(col =>
      row += (col -> resultSet.getString(col))
    )

    row.result
  }
}
