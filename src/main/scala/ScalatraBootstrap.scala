import com.lombardo.app._
import com.lombardo.app.resources._
import org.scalatra._
import javax.servlet.ServletContext

import com.lombardo.app.connectors.dbConnector
import com.lombardo.app.data.WordRepository
import sys.process._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    dbConnector.configure
    WordRepository.test

    context.mount(new DefaultServlet, "/")
    context.mount(new GreetingServlet, "/greetings")
    context.mount(new WordServlet, "/words")
  }
}
