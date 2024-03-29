package code.customer

import java.util.Date
import com.openbankproject.commons.model.{BankId, Customer, CustomerMessage, User}
import net.liftweb.util.SimpleInjector


object CustomerMessages extends SimpleInjector {

  val customerMessageProvider = new Inject(buildOne _) {}

  def buildOne: CustomerMessageProvider = MappedCustomerMessageProvider

}

trait CustomerMessageProvider {

  //TODO: pagination? is this sorted by date?
  def getMessages(user : User, bankId : BankId) : List[CustomerMessage]

  def addMessage(user : User, bankId : BankId, message : String, fromDepartment : String, fromPerson : String) : CustomerMessage
  
  def createCustomerMessage(customer : Customer, bankId : BankId, transport : String, message : String, fromDepartment : String, fromPerson : String) : CustomerMessage
  
  def getCustomerMessages(customer : Customer, bankId : BankId) : List[CustomerMessage]

}


