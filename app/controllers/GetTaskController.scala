package controllers

import javax.inject.{ Inject, Singleton }

import models.Task
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc.{ Action, AnyContent, Controller }

@Singleton
class GetTaskController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index(taskId: Long): Action[AnyContent] = Action { implicit request =>
    Task.findById(taskId) match {
      case Some(task) => Ok(views.html.show(task))
      case None       => NotFound("can not find taskId")
    }
  }

}
