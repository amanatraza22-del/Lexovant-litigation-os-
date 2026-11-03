package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.LitigationRepository
import com.example.model.CaseCategory
import com.example.model.CourtCase
import com.example.model.CaseStage
import com.example.model.CaseStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("LitigationOS", appName)
  }

  @Test
  fun `repository contains seeded Indian litigation cases`() {
    val repo = LitigationRepository()
    val cases = repo.cases.value
    assertTrue("Should have initial demo cases", cases.isNotEmpty())
    val commercialCase = cases.find { it.caseNumber == "CS(COMM) 412/2024" }
    assertTrue("Should contain Commercial Suit CS(COMM) 412/2024", commercialCase != null)
    assertEquals("High Court of Delhi (New Delhi)", commercialCase?.courtName)
    assertEquals(CaseCategory.COMMERCIAL, commercialCase?.category)
  }

  @Test
  fun `repository can register new case`() {
    val repo = LitigationRepository()
    val initialCount = repo.cases.value.size
    val testCase = CourtCase(
      id = "test_case_99",
      title = "Test Petitioner v. Test Respondent",
      caseNumber = "ARB.P. 55/2026",
      cnrNumber = "DLDH010099992026",
      courtName = "High Court of Delhi",
      courtEstablishment = "Commercial Division",
      category = CaseCategory.COMMERCIAL,
      caseType = "ARB.P.",
      filingNumber = "FIL/2026/01",
      filingDate = "2026-11-03",
      registrationDate = "2026-11-03",
      petitioner = "Test Petitioner",
      respondent = "Test Respondent",
      advocateName = "Adv. Rajesh Malhotra",
      opposingCounsel = "Adv. Respondent",
      judgeCourtroom = "Court 14",
      currentStage = CaseStage.ADMISSION,
      status = CaseStatus.ACTIVE,
      nextHearingDate = "2026-12-01",
      nextHearingPurpose = "Notice",
      limitationDate = "2026-12-30",
      clientName = "Test Client",
      clientId = "client_test",
      notes = "Test arbitration petition",
      isDemo = false
    )
    repo.addCase(testCase)
    assertEquals(initialCount + 1, repo.cases.value.size)
    assertEquals("test_case_99", repo.selectedCaseId.value)
  }

  @Test
  fun `courtroom prep kits generated for all active cases with bench briefs`() {
    val repo = LitigationRepository()
    val kit1 = repo.getCourtroomPrepKit("case_1")
    assertTrue(kit1.onePageBrief.contains("CS(COMM) 412/2024"))
    assertTrue(kit1.benchQuestionsPredicted.isNotEmpty())

    val kit2 = repo.getCourtroomPrepKit("case_2")
    assertTrue(kit2.onePageBrief.contains("BAIL APPLN. 1824/2024"))

    val kit3 = repo.getCourtroomPrepKit("case_3")
    assertTrue(kit3.onePageBrief.contains("W.P.(C) 9821/2024"))

    val kit4 = repo.getCourtroomPrepKit("case_4")
    assertTrue(kit4.onePageBrief.contains("CC NO. 4521/2024"))
  }

  @Test
  fun `case intelligence analysis contains missing information warnings`() {
    val repo = LitigationRepository()
    val analysis = repo.getIntelligenceAnalysis("case_1")
    assertTrue("Should have missing information items", analysis.missingInformation.isNotEmpty())
    assertTrue("Should contain recommended arguments", analysis.recommendedArguments.isNotEmpty())
  }

  @Test
  fun `all modules link to case records properly`() {
    val repo = LitigationRepository()

    // 1. Court Diary hearings linked to case records
    val hearings = repo.hearings.value
    assertTrue("Hearings must be non-empty", hearings.isNotEmpty())
    assertTrue("Every hearing must be tied to a caseId", hearings.all { it.caseId.isNotBlank() })

    // 2. Evidence Room documents linked to case records
    val docs = repo.documents.value
    assertTrue("Documents must be non-empty", docs.isNotEmpty())
    assertTrue("Every document must be tied to a caseId", docs.all { it.caseId.isNotBlank() })

    // 3. Billing Invoices tied to both case and client
    val invoices = repo.invoices.value
    assertTrue("Invoices must be non-empty", invoices.isNotEmpty())
    assertTrue("Invoices tied to caseId", invoices.all { it.caseId.isNotBlank() })
    assertTrue("Invoices tied to clientId", invoices.all { it.clientId.isNotBlank() })

    // 4. Limitation calculation updates case deadline
    repo.updateCaseLimitation("case_1", "2026-11-25")
    val updatedCase = repo.cases.value.find { it.id == "case_1" }
    assertEquals("2026-11-25", updatedCase?.limitationDate)

    // 5. Team member case assignment
    val initialMember = repo.team.value.first()
    val initialCount = initialMember.activeCasesCount
    repo.assignCaseToMember(initialMember.id, "case_4")
    val updatedMember = repo.team.value.find { it.id == initialMember.id }
    assertEquals(initialCount + 1, updatedMember?.activeCasesCount)
  }

  @Test
  fun `legal research authorities are linked to cases and support additions`() {
    val repo = LitigationRepository()
    val initialAuthorities = repo.authorities.value
    assertTrue("Should have seeded legal authorities", initialAuthorities.isNotEmpty())
    assertTrue("All authorities must be linked to a caseId", initialAuthorities.all { it.caseId.isNotBlank() })

    val newAuth = com.example.model.LegalAuthority(
      id = "test_auth_1",
      caseId = "case_2",
      title = "P. Chidambaram v. Directorate of Enforcement",
      citation = "(2020) 13 SCC 791",
      court = "Supreme Court of India",
      year = 2020,
      bench = "R. Banumathi, A.S. Bopanna, Hrishikesh Roy, JJ.",
      ratioDecidendi = "Gravity of offence alone cannot be a ground to deny bail if triple test is satisfied.",
      relevantParagraphs = "Paras 21-24",
      isOverruled = false
    )
    repo.addAuthority(newAuth)
    val updatedAuthorities = repo.authorities.value
    assertTrue("New authority added", updatedAuthorities.any { it.id == "test_auth_1" })
  }

  @Test
  fun `dashboard metrics correctly calculate dynamic counts from repository`() {
    val repo = LitigationRepository()
    val cases = repo.cases.value
    val hearings = repo.hearings.value
    val drafts = repo.drafts.value
    val invoices = repo.invoices.value

    val metrics = com.example.viewmodel.DashboardMetrics(
      activeCases = cases.count { it.status == CaseStatus.ACTIVE || it.status == CaseStatus.HEARING_PENDING },
      pendingHearings = hearings.count { !it.isCompleted },
      todayHearingsCount = hearings.count { it.date == "2026-11-03" && !it.isCompleted },
      upcomingHearingsCount = hearings.count { it.date > "2026-11-03" && !it.isCompleted },
      filingDeadlinesCount = cases.count { it.limitationDate.isNotBlank() && it.limitationDate != "N/A" },
      pendingDraftsCount = drafts.count { it.status != "Final" },
      outstandingFeesInr = invoices.filter { it.status != com.example.model.InvoiceStatus.PAID }.sumOf { it.totalAmount - it.paidAmount },
      totalRevenueCollectedInr = invoices.sumOf { it.paidAmount }
    )

    assertEquals(4, metrics.activeCases)
    assertTrue("Outstanding fees must be calculated from unpaid invoices", metrics.outstandingFeesInr > 0.0)
    assertTrue("Filing deadlines calculated from cases", metrics.filingDeadlinesCount > 0)
  }
}
