package com.lombardo.app.util

import java.util.concurrent.TimeUnit

import org.slf4j.LoggerFactory

object Util {
  val logger =  LoggerFactory.getLogger(getClass)

  def json(message: String) : (String, String) = {
    "message" -> message
  }

  def opTimer[T](operation: => T): T = {
    val startTime = System.nanoTime

    val result = operation

    val endTime = System.nanoTime
    val duration = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS)

    logger.info(s"""${operation.toString} elapsed time $duration""")

    result
  }
}
