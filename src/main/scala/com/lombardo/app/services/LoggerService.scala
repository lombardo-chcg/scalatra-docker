package com.lombardo.app.services

import com.lombardo.app.models.Model.ServerLog

trait logger {
  def log(log: ServerLog): Option[Int]
}

class LoggerService extends logger {
  val repoService = new RepositoryService

  def log(log: ServerLog): Option[Int] = {

    repoService.insert("logs", caseClassToMap(log))
  }

  private def caseClassToMap(obj: Product): Map[String, Any] = {
    val keys = obj.getClass.getDeclaredFields.map(_.getName).toList
    val values = obj.productIterator.toList

    keys.zip(values).toMap
  }
}
