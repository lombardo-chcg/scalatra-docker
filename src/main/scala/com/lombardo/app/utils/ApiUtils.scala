package com.lombardo.app.utils

import org.slf4j.LoggerFactory

object ApiUtils {
  val logger =  LoggerFactory.getLogger(getClass)

  def json(message: String) : (String, String) = {
    "message" -> message
  }

  def jsonString(message: String) : String = {
    s"""{ "message": "$message" }"""
  }

  def opTimer[T](opName: String , operation: => T): T = {
    val t0 = System.currentTimeMillis

    val result = operation

    val t1 = System.currentTimeMillis
    val duration = t1 - t0

    logger.info(s"""$opName elapsed time $duration ms""")

    result
  }

  implicit class ProductMapper(val obj: Product) {

    def toMap = {
      val keys = obj.getClass.getDeclaredFields.map(_.getName).toList
      val values = obj.productIterator.toList

      keys.zip(values).toMap
    }
  }
}
