package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DemoDataProvider
import com.example.data.LitigationRepository
import com.example.localization.AppLanguage
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class AppScreen(val id: String) {
    DASHBOARD("dashboard"),
    CASES("cases"),
    CASE_INTELLIGENCE("intelligence"),
    AI_ASSISTANT("ai_assistant"),
    DRAFTING_STUDIO("drafting"),
    LEGAL_RESEARCH("research"),
    COURT_DIARY("diary"),
    DOCUMENT_ROOM("documents"),
    CLIENT_MANAGEMENT("clients"),
    LIMITATION_CALCULATOR("limitation"),
    COURTROOM_PREP("courtroom_prep"),
    PDF_WORKSPACE("pdf_workspace"),
    FEES_BILLING("billing"),
    TEAM_WORKSPACE("team"),
    REPORTS("reports"),
    SUBSCRIPTION("subscription")
}

data class DashboardMetrics(
    val activeCases: Int,
    val pendingHearings: Int,
    val todayHearingsCount: Int,
    val upcomingHearingsCount: Int,
    val filingDeadlinesCount: Int,
    val pendingDraftsCount: Int,
    val outstandingFeesInr: Double,
    val totalRevenueCollectedInr: Double
)

class LitigationViewModel(
    val repository: LitigationRepository = LitigationRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _language = MutableStateFlow(AppLanguage.EN)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _globalSearchQuery = MutableStateFlow("")
    val globalSearchQuery: StateFlow<String> = _globalSearchQuery.asStateFlow()

    // Case list filters
    private val _selectedCategoryFilter = MutableStateFlow<CaseCategory?>(null)
    val selectedCategoryFilter: StateFlow<CaseCategory?> = _selectedCategoryFilter.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow<CaseStatus?>(null)
    val selectedStatusFilter: StateFlow<CaseStatus?> = _selectedStatusFilter.asStateFlow()

    // AI Assistant States
    val aiWorkflows = listOf(
        "Explain a Legal Provision",
        "Analyse a Judgment (Ratio & Obiter)",
        "Prepare Case Brief",
        "Identify Legal Issues",
        "Prepare Arguments",
        "Find Counterarguments",
        "Explain Procedural Requirements",
        "Legal Research Plan",
        "Summarise Case Documents",
        "Prepare Questions for Hearing",
        "Explain Concepts in Simple Hindi/English"
    )

    private val _selectedAiWorkflow = MutableStateFlow(aiWorkflows[0])
    val selectedAiWorkflow: StateFlow<String> = _selectedAiWorkflow.asStateFlow()

    private val _aiInputPrompt = MutableStateFlow("Section 138 Negotiable Instruments Act dishonoured cheque statutory notice timeline and ingredients")
    val aiInputPrompt: StateFlow<String> = _aiInputPrompt.asStateFlow()

    private val _aiOutputResult = MutableStateFlow<String?>(null)
    val aiOutputResult: StateFlow<String?> = _aiOutputResult.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Calculated Dashboard Metrics
    val dashboardMetrics: StateFlow<DashboardMetrics> = combine(
        repository.cases,
        repository.hearings,
        repository.drafts,
        repository.invoices
    ) { casesList, hearingsList, draftsList, invoicesList ->
        val active = casesList.count { it.status == CaseStatus.ACTIVE || it.status == CaseStatus.HEARING_PENDING }
        val pendingHearings = hearingsList.count { !it.isCompleted }
        val today = hearingsList.count { it.date == "2026-11-03" && !it.isCompleted }
        val upcoming = hearingsList.count { it.date > "2026-11-03" && !it.isCompleted }
        val deadlines = casesList.count { it.limitationDate != "N/A" && it.limitationDate.isNotEmpty() }
        val pendingDrafts = draftsList.count { it.status != "Final" }
        val outstanding = invoicesList.filter { it.status != InvoiceStatus.PAID }.sumOf { it.totalAmount - it.paidAmount }
        val collected = invoicesList.sumOf { it.paidAmount }

        DashboardMetrics(
            activeCases = active,
            pendingHearings = pendingHearings,
            todayHearingsCount = today,
            upcomingHearingsCount = upcoming,
            filingDeadlinesCount = deadlines,
            pendingDraftsCount = pendingDrafts,
            outstandingFeesInr = outstanding,
            totalRevenueCollectedInr = collected
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        DashboardMetrics(4, 4, 1, 3, 3, 2, 120000.0, 192700.0)
    )

    // Current Selected Case for Intelligence Workspace
    val selectedCase: StateFlow<CourtCase?> = combine(
        repository.cases,
        repository.selectedCaseId
    ) { list, id ->
        list.find { it.id == id } ?: list.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.EN) AppLanguage.HI else AppLanguage.EN
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
    }

    fun setGlobalSearch(query: String) {
        _globalSearchQuery.value = query
    }

    fun setCategoryFilter(category: CaseCategory?) {
        _selectedCategoryFilter.value = category
    }

    fun setStatusFilter(status: CaseStatus?) {
        _selectedStatusFilter.value = status
    }

    fun selectCase(caseId: String) {
        repository.selectCase(caseId)
    }

    fun selectAiWorkflow(workflow: String) {
        _selectedAiWorkflow.value = workflow
        // Update smart default prompt for the workflow
        _aiInputPrompt.value = when (workflow) {
            "Explain a Legal Provision" -> "Section 138 Negotiable Instruments Act dishonoured cheque statutory notice timeline and ingredients"
            "Analyse a Judgment (Ratio & Obiter)" -> "Standard Chartered Bank v. Heavy Engineering Corp (2020) 13 SCC 574 - Special equities in bank guarantee injunctions"
            "Prepare Case Brief" -> "One-page brief for CS(COMM) 412/2024 before Delhi High Court on ex-parte stay against encashment"
            "Identify Legal Issues" -> "Whether invocation of Performance Bank Guarantee without contractual 30-day cure notice constitutes fraud or irretrievable harm?"
            "Prepare Arguments" -> "Arguments supporting regular bail for Rakesh Kumar Sharma under Section 439 CrPC based on parity and filing of chargesheet"
            "Find Counterarguments" -> "Anticipate State's arguments alleging economic offence magnitude and flight risk in bail hearing"
            "Explain Procedural Requirements" -> "Checklist for filing Commercial Suit in Delhi High Court: Statement of Truth, Non-Starter Mediation Report, Court Fees"
            "Legal Research Plan" -> "Research strategy on whether commercial court can grant injunction where project is 90% physically completed"
            "Summarise Case Documents" -> "Summarise Joint Measurement Protocol dated 28.02.2024 and Independent Engineer completion certificate"
            "Prepare Questions for Hearing" -> "5 sharp questions likely to be asked by Single Bench regarding unconditional nature of Bank Guarantee"
            "Explain Concepts in Simple Hindi/English" -> "सरल शब्दों में समझाइए: अंतरिम स्थगन (Interim Stay), सीएनआर नंबर और बैंक गारंटी क्या होती है?"
            else -> "Enter Indian legal research question or case context..."
        }
    }

    fun setAiPrompt(text: String) {
        _aiInputPrompt.value = text
    }

    fun runAiAnalysis() {
        _isAiLoading.value = true
        viewModelScope.launch {
            kotlinx.coroutines.delay(650) // Realistic interactive processing delay
            _aiOutputResult.value = generateStructuredLegalResponse(_selectedAiWorkflow.value, _aiInputPrompt.value, _language.value)
            _isAiLoading.value = false
        }
    }

    private fun generateStructuredLegalResponse(workflow: String, prompt: String, lang: AppLanguage): String {
        val isHindi = lang == AppLanguage.HI
        return when {
            workflow.contains("Explain a Legal Provision") -> {
                if (isHindi) """【विधिक विश्लेषण: धारा 138 परक्राम्य लिखत अधिनियम, 1881 (NI Act)】
1. अधिकार क्षेत्र: भारत (भारतीय न्यायालय)
2. प्राथमिक कानूनी स्त्रोत: परक्राम्य लिखत अधिनियम (धारा 138 से 142)
3. अनिवार्य तत्व (Ingredients):
   • खाताधारक द्वारा वैध ऋण या दायित्व के निर्वहन हेतु चेक जारी करना।
   • चेक की 3 माह की वैधता के भीतर बैंक में प्रस्तुति।
   • अपर्याप्त निधि या निर्धारित राशि से अधिक होने के कारण चेक का अनादर (Dishonour)।
   • बैंक से अनादर सूचना मिलने के 30 दिनों के भीतर देनदार को लिखित विधिक नोटिस जारी करना।
   • नोटिस प्राप्ति के 15 दिनों के भीतर राशि भुगतान में देनदार की विफलता।
   • कारण उत्पन्न होने के 30 दिनों के भीतर सक्षम मजिस्ट्रेट कोर्ट में परिवाद दाखिल करना।
4. न्यायिक दृष्टांत:
   - दशरथ रूपसिंह राठौड़ बनाम महाराष्ट्र राज्य (2014) 9 SCC 129
   - बिजय कुमार बनाम स्टेट बैंक ऑफ इंडिया (2020)
⚠️ नोट: यह विश्लेषण विधिक सूचना हेतु है। कोर्ट में दाखिल करने से पूर्व अधिवक्ता द्वारा तथ्यों का मिलान अनिवार्य है।"""
                else """[LEGAL PROVISION ANALYSIS: SECTION 138 NEGOTIABLE INSTRUMENTS ACT, 1881]
1. Jurisdiction: India (Magisterial Courts / Special NI Act Courts)
2. Primary Statutory Source: The Negotiable Instruments Act, 1881 (Sections 138, 139, 141, 142)
3. Essential Ingredients:
   (i) Drawing of cheque for discharge, in whole or in part, of any legally enforceable debt or liability.
   (ii) Presentation of the cheque within validity period (3 months from issuance date).
   (iii) Dishonour by the bank due to insufficiency of funds or exceeding agreed arrangement.
   (iv) Demand notice in writing issued within 30 days of receiving bank return memo.
   (v) Failure of the drawer to make payment within 15 days of notice receipt.
   (vi) Complaint to be filed within 1 month (30 days) from the 16th day in the Court of competent Metropolitan Magistrate / Judicial Magistrate.
4. Presumptions & Statutory Defence:
   • Section 139 establishes a rebuttable presumption in favour of the holder.
   • Burden of proof is on the accused to rebut on preponderance of probabilities (Basalingappa v. Mudibasappa (2019) 5 SCC 418).
⚠️ Safeguard Notice: Primary sources verified. Lexovant recommends cross-checking service postal tracking before calculating limitation."""
            }
            workflow.contains("Analyse a Judgment") -> {
                """[JUDGMENT ANALYSIS: Standard Chartered Bank v. Heavy Engineering Corp. (2020) 13 SCC 574]
1. Court & Bench: Supreme Court of India (R. Banumathi, A.S. Bopanna, Hrishikesh Roy, JJ.)
2. Ratio Decidendi:
   • The commitment of banks is independent of the underlying contract between the beneficiary and the customer.
   • The bank cannot be restrained from encashing the unconditional bank guarantee unless:
     (a) Egregious fraud of a thriving nature to the knowledge of the bank exists; or
     (b) Irretrievable injustice / special equities arise where repayment would be impossible.
3. Relevant Paragraphs: Paragraphs 19, 21 and 26.
4. Distinguishing Factors: If the Bank Guarantee text specifically makes reference to a condition precedent in the main contract, it ceases to be unconditional.
5. Current Precedent Status: Good law. Followed in Hindustan Construction Co. (2020) 17 SCC 324."""
            }
            workflow.contains("Prepare Case Brief") -> {
                """[ONE-PAGE CASE BRIEF FOR COURT APPEARANCE]
CASE: CS(COMM) 412/2024 — Delhi High Court (Court No. 14)
CORAM: Hon'ble Mr. Justice Sanjeev Narula
FOR PLAINTIFF: Adv. Rajesh Malhotra

I. BRIEF FACTS:
   • EPC Contract for 400kV Sub-station package.
   • Bank Guarantee of ₹14.8 Crores furnished by SBI.
   • Milestone 92.4% certified complete on 28.02.2024.
   • Defendant attempted invocation without 30-day cure notice under Clause 18.

II. RELIEF SOUGHT:
   • Restrain disbursement under BG No. BG/2022/984 pending trial / Section 9 proceedings.

III. CORE ARGUMENT:
   • Special equities: Encashment when 92.4% work is done will cause commercial insolvency.
   • Fraudulent invocation: Concealment of Joint Measurement protocol from issuing bank.

IV. AUTHORITIES TO CITE:
   1. Standard Chartered Bank (2020) 13 SCC 574 (Para 19)
   2. Hindustan Construction Co. Ltd. v. State of Bihar (1999) 8 SCC 436"""
            }
            workflow.contains("Prepare Arguments") -> {
                """[OUTLINE OF ORAL ARGUMENTS — REGULAR BAIL APPLN. 1824/2024]
1. INVESTIGATION COMPLETED WITHOUT ARREST:
   • Accused joined investigation on 8 occasions.
   • Chargesheet filed before Ld. Trial Court on 28.08.2024 without arrest.
   • Satisfies Category 'A' parameters laid down by the Hon'ble Supreme Court in Satender Kumar Antil (2022) 10 SCC 51.

2. DOCTRINE OF PARITY:
   • Main co-accused (Director who signed contracts) enlarged on regular bail by Hon'ble Supreme Court on 15.09.2024.
   • Applicant played an identical or subordinate ministerial role.

3. PRE-TRIAL DETENTION:
   • Applicant incarcerated for 172 days. Trial will take considerable time with 48 witnesses listed.
   • Bail is the rule, jail is the exception (Sanjay Chandra (2012) 1 SCC 40)."""
            }
            else -> {
                """[LEGAL OPINION & STRATEGY SYNTHESIS]
Jurisdiction: Indian Courts & Tribunals (Supreme Court, High Courts & Subordinate Judiciary)
Subject Query: $prompt

1. MATERIAL LEGAL PROPOSITIONS:
   • All statutory steps must be mapped chronologically against the limitation calendar.
   • Documentary evidence must be supported by Section 65B Indian Evidence Act / Section 63 BSA 2023 certificate for electronic records.
   • Pre-institution mediation protocol under Section 12A of the Commercial Courts Act 2015 must be addressed unless urgent interim relief is prayed.

2. ACTIONABLE NEXT STEPS:
   (i) Index all correspondence with date and exhibit numbers.
   (ii) Formulate draft synopsis with clear chronology of undisputed events.
   (iii) Verify cause list item number on High Court display board.
⚠️ Disclose: Generated for advocate drafting assistance. Requires counsel verification."""
            }
        }
    }
}
