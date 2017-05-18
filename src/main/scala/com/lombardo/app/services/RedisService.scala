package com.lombardo.app.services

import com.redis._
import serialization._
import Parse.Implicits.parseString
import org.slf4j.LoggerFactory

class RedisService {

  val logger =  LoggerFactory.getLogger(getClass)
  val redis = new RedisClient("localhost", 32788)

  def findAllWithSearch(resource: String, column: String, searchTermList: List[String]) : Option[List[Map[String, String]]] = {
    val output = searchTermList.map(word => {
      redis.lrange(word, 0, -1)
    })

    val returnVal = output.flatten.flatten.map(x => {
      x match {
        case Some(thing) => thing
        case None => None
      }
    })

    try {
      val resultSet = returnVal.map(x => {
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
