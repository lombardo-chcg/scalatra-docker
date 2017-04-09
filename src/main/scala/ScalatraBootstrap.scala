import com.lombardo.app._
import com.lombardo.app.resources._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new GreetingServlet, "/*")
  }
}
