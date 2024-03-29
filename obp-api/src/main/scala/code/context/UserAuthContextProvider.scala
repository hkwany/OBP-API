package code.context

import code.api.util.APIUtil
import code.remotedata.RemotedataUserAuthContext
import com.openbankproject.commons.model.{BasicUserAuthContext, UserAuthContext}
import net.liftweb.common.Box
import net.liftweb.util.SimpleInjector

import scala.collection.immutable.List
import scala.concurrent.Future


object UserAuthContextProvider extends SimpleInjector {

  val userAuthContextProvider = new Inject(buildOne _) {}

  def buildOne: UserAuthContextProvider =
    APIUtil.getPropsAsBoolValue("use_akka", false) match {
      case false  => MappedUserAuthContextProvider
      case true => RemotedataUserAuthContext   // We will use Akka as a middleware
    }
}

trait UserAuthContextProvider {
  def createUserAuthContext(userId: String, key: String, value: String, consumerId: String): Future[Box[UserAuthContext]]
  def getUserAuthContexts(userId: String): Future[Box[List[UserAuthContext]]]
  def getUserAuthContextsBox(userId: String): Box[List[UserAuthContext]]
  def createOrUpdateUserAuthContexts(userId: String, userAuthContexts: List[BasicUserAuthContext]): Box[List[UserAuthContext]]
  def deleteUserAuthContexts(userId: String): Future[Box[Boolean]]
  def deleteUserAuthContextById(userAuthContextId: String): Future[Box[Boolean]]
}

class RemotedataUserAuthContextCaseClasses {
  case class createUserAuthContext(userId: String, key: String, value: String, consumerId: String)
  case class getUserAuthContexts(userId: String)
  case class getUserAuthContextsBox(userId: String)
  case class createOrUpdateUserAuthContexts(userId: String, userAuthContext: List[BasicUserAuthContext])
  case class deleteUserAuthContexts(userId: String)
  case class deleteUserAuthContextById(userAuthContextId: String)
}

object RemotedataUserAuthContextCaseClasses extends RemotedataUserAuthContextCaseClasses