package code.taxresidence

import code.api.util.APIUtil
import code.remotedata.RemotedataTaxResidence
import net.liftweb.common.Box
import net.liftweb.util.SimpleInjector

import scala.concurrent.Future

object TaxResidence extends SimpleInjector {

  val taxResidence = new Inject(buildOne _) {}

  def buildOne: TaxResidenceProvider =
    APIUtil.getPropsAsBoolValue("use_akka", false) match {
      case false  => MappedTaxResidenceProvider
      case true => RemotedataTaxResidence     // We will use Akka as a middleware
    }
}

trait TaxResidenceProvider {
  def getTaxResidence(customerId: String): Future[Box[List[TaxResidence]]]
  def addTaxResidence(customerId: String, domain: String, taxNumber: String): Future[Box[TaxResidence]]
}

trait TaxResidence {
  def customerId: Long
  def taxResidenceId: String
  def domain: String
  def taxNumber: String
}


class RemotedataTaxResidenceCaseClasses {
  case class getTaxResidence(customerId: String)
  case class addTaxResidence(customerId: String, domain: String, taxNumber: String)
}

object RemotedataTaxResidenceCaseClasses extends RemotedataTaxResidenceCaseClasses