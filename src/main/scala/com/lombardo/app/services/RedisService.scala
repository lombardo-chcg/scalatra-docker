package com.lombardo.app.services

import com.redis._
import serialization._
import Parse.Implicits.parseString
import org.slf4j.LoggerFactory

class RedisService {

  val redisHost = sys.env("REDIS_HOST")
  val redisPort = sys.env("REDIS_PORT").toInt

  val logger =  LoggerFactory.getLogger(getClass)
  val redis = new RedisClient(redisHost, redisPort)

  def findAllWithSearch(resource: String, column: String, searchTermList: List[String]) : Option[List[Map[String, String]]] = {
    logger.info(s"""Making ${searchTermList.length} Redis requests""")

    val redisResultSet = searchTermList.map(redis.lrange(_, 0, -1))

    val unwrappedResultSet = redisResultSet.flatten.flatten.map({
        case Some(result) => result
        case None => None
    })

    try {
      val resultSet = unwrappedResultSet.map(x => {
        val split = x.toString.split(",")

        Map("word" -> split(0), "points" -> split(1))
      })

      Some(resultSet)
    } catch {
      case e =>
        logger.error(e.getMessage)
        None
    }
  }
}
