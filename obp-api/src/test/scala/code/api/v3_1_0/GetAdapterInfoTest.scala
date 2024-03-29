/**
Open Bank Project - API
Copyright (C) 2011-2019, TESOBE GmbH

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Email: contact@tesobe.com
TESOBE GmbH
Osloerstrasse 16/17
Berlin 13359, Germany

This product includes software developed at
TESOBE (http://www.tesobe.com/)
  */
package code.api.v3_1_0

import com.openbankproject.commons.util.ApiVersion
import code.api.v3_0_0.AdapterInfoJsonV300
import code.api.util.APIUtil.OAuth._
import code.api.util.ApiRole.{CanCreateAccountAttributeAtOneBank, canGetAdapterInfo}
import code.api.util.ErrorMessages.{UserHasMissingRoles, UserNotLoggedIn}
import code.setup.{APIResponse, DefaultUsers}
import code.api.v3_1_0.OBPAPI3_1_0.Implementations3_1_0
import code.entitlement.Entitlement
import com.github.dwickern.macros.NameOf.nameOf
import com.openbankproject.commons.model.ErrorMessage
import org.scalatest.Tag

class GetAdapterInfoTest extends V310ServerSetup with DefaultUsers {

  /**
    * Test tags
    * Example: To run tests with tag "getPermissions":
    * 	mvn test -D tagsToInclude
    *
    *  This is made possible by the scalatest maven plugin
    */
  object VersionOfApi extends Tag(ApiVersion.v3_1_0.toString)
  object ApiEndpoint extends Tag(nameOf(Implementations3_1_0.getAdapterInfo))

  feature("Get Adapter Info v3.1.0")
  {
    scenario(s"$UserNotLoggedIn error case", ApiEndpoint, VersionOfApi) {
      When("We make a request v3.1.0")
      val request310 = (v3_1_0_Request / "adapter").GET
      val response310 = makeGetRequest(request310)
      Then("We should get a 401")
      response310.code should equal(401)
      And("error should be " + UserNotLoggedIn)
      response310.body.extract[ErrorMessage].message should equal (UserNotLoggedIn)
    }
    scenario(s"$UserHasMissingRoles error case", ApiEndpoint, VersionOfApi) {
      When("We make a request v3.1.0")
      val request310 = (v3_1_0_Request / "adapter").GET <@ (user1)
      val response310 = makeGetRequest(request310)
      Then("We should get a 403")
      response310.code should equal(403)
      And("error should be " + UserHasMissingRoles + canGetAdapterInfo)
      response310.body.extract[ErrorMessage].message should equal (UserHasMissingRoles + canGetAdapterInfo)
    }
    scenario("We will try to get adapter info", ApiEndpoint, VersionOfApi) {
      Entitlement.entitlement.vend.addEntitlement("", resourceUser1.userId, canGetAdapterInfo.toString)
      When("We make a request v3.1.0")
      val request310 = (v3_1_0_Request / "adapter").GET <@ (user1)
      val response310 = makeGetRequest(request310)
      Then("We should get a 200")
      response310.code should equal(200)
      Then("We should get a 200")
      response310.code should equal(200)
      response310.body.extract[AdapterInfoJsonV300].name should equal("LocalMappedConnector")
    }
  }


}
