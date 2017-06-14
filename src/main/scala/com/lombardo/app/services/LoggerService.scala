package com.lombardo.app.services

import com.lombardo.app.models.Model.ServerEvent
import com.lombardo.app.utils.ApiUtils._

trait logger {
  def logEvent(log: ServerEvent): Option[Int]
}

class LoggerService extends logger {
  val repoService = new RepositoryService

  def logEvent(event: ServerEvent): Option[Int] = {

    repoService.insert("logs", event.toMap)
  }
}
