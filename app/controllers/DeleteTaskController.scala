package controllers

import javax.inject.{ Inject, Singleton }

import models.Task
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc.{ Action, AnyContent, Controller }
import scalikejdbc.AutoSession

@Singleton
class DeleteTaskController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def delete(taskId: Long): Action[AnyContent] = Action {
    implicit val session = AutoSession
    val result           = Task.deleteById(taskId)
    if (result > 0) {
      Redirect(routes.GetTasksController.index())
    } else {
      InternalServerError(Messages("DeleteTaskError"))
    }
  }

}
