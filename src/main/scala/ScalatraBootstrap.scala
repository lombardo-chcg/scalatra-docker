import com.lombardo.app._
import com.lombardo.app.resources._
import org.scalatra._
import javax.servlet.ServletContext

import com.lombardo.app.connectors.dbConnector

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    dbConnector.configure
    context.mount(new GreetingServlet, "/greetings")
    context.mount(new WordServlet, "/words")
  }
}
