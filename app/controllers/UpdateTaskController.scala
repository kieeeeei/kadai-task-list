package controllers

import javax.inject.{ Inject, Singleton }

import forms.TaskForm
import models.Task
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc.{ Action, AnyContent, Controller }
import scalikejdbc.AutoSession

@Singleton
class UpdateTaskController @Inject()(val messagesApi: MessagesApi)
    extends Controller
    with I18nSupport
    with TaskControllerSupport {

  def index(taskId: Long): Action[AnyContent] = Action { implicit request =>
    Task.findById(taskId) match {
      case Some(task) => {
        val filledForm = form.fill(TaskForm(task.id, task.content))
        Ok(views.html.edit(filledForm))
      }
      case None => NotFound("can not find taskId")
    }
  }

  def update: Action[AnyContent] = Action { implicit request =>
    form
      .bindFromRequest()
      .fold(
        formWithErrors => BadRequest(views.html.edit(formWithErrors)), { model =>
          implicit val session = AutoSession
          val result = Task
            .updateById(model.id.get)
            .withAttributes(
              'content -> model.content
            )
          if (result > 0)
            Redirect(routes.GetTasksController.index())
          else
            InternalServerError(Messages("UpdateTaskError"))
        }
      )
  }

}
