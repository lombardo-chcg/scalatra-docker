package com.lombardo.app.services

import com.redis._

trait RedisResult extends Product with Serializable
case class RedisSearchResult(word: String, points: String)

case class FuckingShit(content: List[List[String]]) extends RedisResult

class RedisService {

  val redis = new RedisClient("localhost", 32771)

  def findAllWithSearch(resource: String, column: String, searchTermList: List[String]) : Option[List[Map[String, String]]] = {
    val searchHits = List.newBuilder[List[Option[String]]]
    val container = List.newBuilder[Any]

    val output = searchTermList.map(word => {
      val result = redis.lrange(word, 0, -1)
//      println(result.getClass, " <=====> ", result)

     result match {
        case None => None
        case Some(result) =>
//          println("case Some(result)===>", result)
          result.map(_ match {
            case None => None
            case Some(word) =>
//              println(word)
              word
          })
      }
    })


    println(output)
    output.map(nested => {
      println(nested.getClass, nested)
      nested
    })
    None
  }
}
