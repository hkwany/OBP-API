package code.api.v4_0_0

import com.openbankproject.commons.model.ErrorMessage
import code.api.ResourceDocs1_4_0.SwaggerDefinitionsJSON
import code.api.util.APIUtil.OAuth._
import code.api.util.APIUtil.extractErrorMessageCode
import code.api.util.ApiRole.CanCreateDirectDebitAtOneBank
import com.openbankproject.commons.util.ApiVersion
import code.api.util.ErrorMessages.{NoViewPermission, UserHasMissingRoles, UserNotLoggedIn}
import code.api.v4_0_0.OBPAPI4_0_0.Implementations4_0_0
import com.github.dwickern.macros.NameOf.nameOf
import net.liftweb.json.Serialization.write
import org.scalatest.Tag

class DirectDebitTest extends V400ServerSetup {
  /**
    * Test tags
    * Example: To run tests with tag "getPermissions":
    * 	mvn test -D tagsToInclude
    *
    *  This is made possible by the scalatest maven plugin
    */
  object VersionOfApi extends Tag(ApiVersion.v4_0_0.toString)
  object ApiEndpoint1 extends Tag(nameOf(Implementations4_0_0.createDirectDebit))
  object ApiEndpoint2 extends Tag(nameOf(Implementations4_0_0.createDirectDebitManagement))

  lazy val postDirectDebitJsonV400 = SwaggerDefinitionsJSON.postDirectDebitJsonV400
  lazy val bankId = randomBankId
  lazy val bankAccount = randomPrivateAccountViaEndpoint(bankId)
  lazy val view = randomOwnerViewPermalinkViaEndpoint(bankId, bankAccount)

  feature(s"test $ApiEndpoint1 version $VersionOfApi - Unauthorized access") {
    scenario("We will call the endpoint without user credentials", ApiEndpoint1, VersionOfApi) {
      When("We make a request v4.0.0")
      val request400 = (v4_0_0_Request / "banks" / bankId / "accounts" / bankAccount.id / view / "direct-debit").POST
      val response400 = makePostRequest(request400, write(postDirectDebitJsonV400))
      Then("We should get a 401")
      response400.code should equal(401)
      response400.body.extract[ErrorMessage].message should equal(UserNotLoggedIn)
    }
  }
  feature(s"test $ApiEndpoint1 version $VersionOfApi - Authorized access") {
    scenario("We will call the endpoint without user credentials", ApiEndpoint1, VersionOfApi) {
      When("We make a request v4.0.0")
      val request400 = (v4_0_0_Request / "banks" / bankId / "accounts" / bankAccount.id / view / "direct-debit").POST <@(user1)
      val response400 = makePostRequest(request400, write(postDirectDebitJsonV400))
      Then("We should get a 400")
      response400.code should equal(400)
      response400.body.extract[ErrorMessage].message contains extractErrorMessageCode(NoViewPermission) should be (true)
    }
  }
  
  
  feature(s"test $ApiEndpoint2 version $VersionOfApi - Unauthorized access") {
    scenario("We will call the endpoint without user credentials", ApiEndpoint2, VersionOfApi) {
      When("We make a request v4.0.0")
      val request400 = (v4_0_0_Request / "management" / "banks" / bankId / "accounts" / bankAccount.id / "direct-debit").POST
      val response400 = makePostRequest(request400, write(postDirectDebitJsonV400))
      Then("We should get a 401")
      response400.code should equal(401)
      response400.body.extract[ErrorMessage].message should equal(UserNotLoggedIn)
    }
  }
  feature(s"test $ApiEndpoint2 version $VersionOfApi - Authorized access") {
    scenario("We will call the endpoint without user credentials", ApiEndpoint2, VersionOfApi) {
      When("We make a request v4.0.0")
      val request400 = (v4_0_0_Request / "management" / "banks" / bankId / "accounts" / bankAccount.id / "direct-debit").POST <@(user1)
      val response400 = makePostRequest(request400, write(postDirectDebitJsonV400))
      Then("We should get a 403")
      response400.code should equal(403)
      val errorMessage = response400.body.extract[ErrorMessage].message
      errorMessage contains (UserHasMissingRoles) should be (true)
      errorMessage contains (CanCreateDirectDebitAtOneBank.toString()) should be (true)
    }
  }
  
  
}
