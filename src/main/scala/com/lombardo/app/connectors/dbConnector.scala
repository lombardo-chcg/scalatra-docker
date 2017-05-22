package com.lombardo.app.connectors

import java.sql._
import org.postgresql.ds.PGPoolingDataSource
import org.slf4j.LoggerFactory

object dbConnector {
  val connectionPool = new PGPoolingDataSource
  val logger =  LoggerFactory.getLogger(getClass)

  val postgresHost = sys.env("POSTGRES_HOST")
  val postgresPort = sys.env("POSTGRES_PORT").toInt
  val dbName = sys.env("POSTGRES_DB")
  val postgresUsername = sys.env("POSTGRES_USER")
  val postgresPassword = sys.env("POSTGRES_PASSWORD")

  def configure = {
    logger.info("setting up PG connection")

    connectionPool.setDataSourceName("Postgres");
    connectionPool.setServerName(postgresHost);
    connectionPool.setPortNumber(postgresPort);
    connectionPool.setDatabaseName(dbName);
    connectionPool.setUser(postgresUsername);
    connectionPool.setPassword(postgresPassword);
    connectionPool.setMaxConnections(300);

    retry(10){ testConnection }
  }

  def getConnection : Connection = {
    val c = connectionPool.getConnection
    logger.info("pg connection opened")
    c
  }

  def close(c: Connection) = {
    c.close
    logger.info("pg connection closed")
  }


  private def testConnection = {
    val tc = connectionPool.getConnection
    val meta = tc.getMetaData
    val cols = meta.getColumns(null, null, "postgres", null)
    cols.next
    logger.info("Postgres connection pool established")
    tc.close
  }

  private def retry[T](times: Int)(func: => T): T = {
    try {
      func
    } catch {
      case e : Throwable =>
        if (times > 1) {
          logger.error(s"""Could not connect to Postgres.  Will attempt ${times - 1} more times""")
          Thread.sleep(5000)
          retry(times - 1 )(func)
        }
        else {
          logger.error(e.getMessage)
          throw e
        }
    }
  }
}
