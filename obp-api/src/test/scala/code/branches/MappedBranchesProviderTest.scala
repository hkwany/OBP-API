package code.branches

import code.api.util.OBPLimit
import code.setup.ServerSetup
import com.openbankproject.commons.model.{BankId, BranchT}
import net.liftweb.mapper.By

class MappedBranchesProviderTest extends ServerSetup {

  private def delete(): Unit = {
    MappedBranch.bulkDelete_!!()
  }

  override def beforeAll() = {
    super.beforeAll()
    delete()
  }

  override def afterEach() = {
    super.afterEach()
    delete()
  }

  def defaultSetup() = new DefaultSetup()
  
  class DefaultSetup {
      val bankIdX = "some-bank-x"
      val bankIdY = "some-bank-y"

      // 3 branches for bank X (one branch does not have a license)

      val unlicensedBranch = MappedBranch.create
        .mBankId(bankIdX)
        .mName("unlicensed")
        .mBranchId("unlicensed")
        .mCountryCode("es")
        .mPostCode("4444")
        .mLine1("a4")
        .mLine2("b4")
        .mLine3("c4")
        .mCity("d4")
        .mState("e4")
        .mlocationLatitude(2.22)
        .mlocationLongitude(3.33)
        .saveMe()
        // Note: The license is not set


      val branch1 = MappedBranch.create
        .mBankId(bankIdX)
        .mName("branch 1")
        .mBranchId("branch1")
        .mCountryCode("de")
        .mPostCode("123213213")
        .mLine1("a")
        .mLine2("b")
        .mLine3("c")
        .mCity("d")
        .mState("e")
        .mLicenseId("some-license")
        .mLicenseName("Some License")
        .mlocationLatitude(2.22)
        .mlocationLongitude(3.33).saveMe()

      val branch2 = MappedBranch.create
        .mBankId(bankIdX)
        .mName("branch 2")
        .mBranchId("branch2")
        .mCountryCode("fr")
        .mPostCode("898989")
        .mLine1("a2")
        .mLine2("b2")
        .mLine3("c2")
        .mCity("d2")
        .mState("e2")
        .mLicenseId("some-license")
        .mLicenseName("Some License")
        .mlocationLatitude(2.22)
        .mlocationLongitude(3.33).saveMe()

    }


  feature("MappedBranchesProvider") {

    scenario("We try to get branches") {

      val fixture = defaultSetup()

      // Only these have license set
      val expectedBranches =  List(fixture.branch1, fixture.branch2, fixture.unlicensedBranch)

      Given("the bank in question has branches")
      MappedBranch.find(By(MappedBranch.mBankId, fixture.bankIdX)).isDefined should equal(true)

      When("we try to get the branches for that bank")
      val branchesOpt: Option[List[BranchT]] = MappedBranchesProvider.getBranches(BankId(fixture.bankIdX),List(OBPLimit(1000))) //OBPLimit(1000) is placeholder here.

      Then("We should get a branches list")
      branchesOpt.isDefined should equal (true)
      val branches = branchesOpt.get

      And("it should contain 3 branches")
      branches.size should equal(3)

      And("they should be the licensed ones")
      branches.sortBy(_.branchId.value) should equal (expectedBranches.sortBy(_.branchId.value))
    }

    scenario("We try to get branches for a bank that doesn't have any") {

      val fixture = defaultSetup()

      Given("we don't have any branches")

      MappedBranch.find(By(MappedBranch.mBankId, fixture.bankIdY)).isDefined should equal(false)

      When("we try to get the branches for that bank")
      val branchDataOpt = MappedBranchesProvider.getBranches(BankId(fixture.bankIdY),List(OBPLimit(1000))) //OBPLimit(1000) is placeholder here.

      Then("we should get back an empty list")
      branchDataOpt.isDefined should equal(true)
      val branches = branchDataOpt.get

      branches.size should equal(0)

    }


    // TODO add test for individual items

  }
}
