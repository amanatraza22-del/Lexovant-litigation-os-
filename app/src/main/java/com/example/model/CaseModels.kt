package com.example.model

enum class CaseCategory(val displayName: String, val hiName: String) {
    COMMERCIAL("Commercial", "वाणिज्यिक"),
    CIVIL("Civil", "दीवानी"),
    CRIMINAL("Criminal", "फौजदारी"),
    CONSTITUTIONAL("Constitutional", "संवैधानिक"),
    FAMILY("Family", "पारिवारिक"),
    CONSUMER("Consumer", "उपभोक्ता"),
    LABOUR("Labour & Employment", "श्रम व रोज़गार"),
    PROPERTY("Property", "संपत्ति"),
    ADMINISTRATIVE("Administrative", "प्रशासनिक"),
    OTHER("Other", "अन्य")
}

enum class CaseStatus(val label: String, val hiLabel: String) {
    ACTIVE("Active", "सक्रिय"),
    HEARING_PENDING("Hearing Pending", "सुनवाई लंबित"),
    STAYED("Stayed", "स्थगित"),
    DISPOSED("Disposed", "निस्तारित"),
    ARCHIVED("Archived", "संग्रहीत")
}

enum class CaseStage(val label: String, val hiLabel: String) {
    ADMISSION("Admission / Preliminary Hearing", "प्रवेश / प्रारंभिक सुनवाई"),
    NOTICE("Notice Issued / Service Awaited", "नोटिस जारी / तामील प्रतीक्षारत"),
    REPLY("Reply / Written Statement", "जवाब / लिखित कथन"),
    REPLICATION("Replication / Rejoinder", "प्रत्युत्तर"),
    FRAMING_ISSUES("Framing of Issues / Charge", "विवादक / आरोप तय"),
    EVIDENCE("Plaintiff / Prosecution Evidence", "साक्ष्य"),
    ARGUMENTS("Final Arguments", "अंतिम बहस"),
    ORDERS("Reserved for Orders / Judgment", "आदेश / निर्णय हेतु सुरक्षित")
}

data class CourtCase(
    val id: String,
    val title: String,
    val caseNumber: String,
    val cnrNumber: String,
    val courtName: String,
    val courtEstablishment: String,
    val category: CaseCategory,
    val caseType: String,
    val filingNumber: String,
    val filingDate: String,
    val registrationDate: String,
    val petitioner: String,
    val respondent: String,
    val advocateName: String,
    val opposingCounsel: String,
    val judgeCourtroom: String,
    val currentStage: CaseStage,
    val status: CaseStatus,
    val nextHearingDate: String,
    val nextHearingPurpose: String,
    val limitationDate: String,
    val clientName: String,
    val clientId: String,
    val notes: String,
    val isDemo: Boolean = true
)

data class Hearing(
    val id: String,
    val caseId: String,
    val caseTitle: String,
    val caseNumber: String,
    val courtName: String,
    val courtroom: String,
    val date: String,
    val time: String,
    val purpose: String,
    val judge: String,
    val previousOrderSummary: String,
    val nextSteps: String,
    val adjournmentHistory: List<String> = emptyList(),
    val reminderSet: Boolean = true,
    val isCompleted: Boolean = false,
    val hearingNotes: String = ""
)

data class LegalDraft(
    val id: String,
    val caseId: String,
    val title: String,
    val documentType: String,
    val category: CaseCategory,
    val courtName: String,
    val language: String,
    val content: String,
    val missingInformation: List<String>,
    val legalAuthorities: List<String>,
    val status: String, // Draft, Review, Final
    val lastUpdated: String
)

enum class DocumentCategory(val label: String, val hiLabel: String) {
    PLEADINGS("Pleadings", "अभिवचन"),
    ORDERS("Orders & Judgments", "आदेश व निर्णय"),
    EVIDENCE("Evidence & Exhibits", "साक्ष्य व प्रदर्श"),
    AFFIDAVITS("Affidavits", "शपथ पत्र"),
    CORRESPONDENCE("Correspondence / Notices", "पत्राचार व नोटिस"),
    CONTRACTS("Contracts & Agreements", "अनुबंध"),
    WITNESS("Witness Statements", "गवाहों के बयान"),
    RESEARCH("Research Materials", "शोध सामग्री"),
    MISCELLANEOUS("Miscellaneous", "विविध")
}

data class CaseDocument(
    val id: String,
    val caseId: String,
    val title: String,
    val category: DocumentCategory,
    val exhibitNumber: String, // e.g., "Ex. P-1"
    val dateOfDocument: String,
    val uploadDate: String,
    val source: String,
    val tags: List<String>,
    val notes: String,
    val ocrReady: Boolean = true,
    val ocrSnippet: String = "",
    val fileSizeKb: Int = 420
)

data class LegalAuthority(
    val id: String,
    val caseId: String,
    val title: String,
    val citation: String,
    val court: String,
    val year: Int,
    val bench: String,
    val ratioDecidendi: String,
    val relevantParagraphs: String,
    val isOverruled: Boolean = false,
    val statusNote: String = "Good law (Cited with approval)",
    val sourceUrl: String = "https://main.sci.gov.in"
)

data class Client(
    val id: String,
    val name: String,
    val organization: String,
    val email: String,
    val phone: String,
    val address: String,
    val clientType: String, // Individual, Corporate, Govt
    val activeCases: Int,
    val outstandingFees: Double,
    val consultationNotes: String
)

enum class InvoiceStatus(val label: String, val hiLabel: String) {
    PAID("Paid", "भुगतान पूर्ण"),
    PARTIAL("Partial", "आंशिक"),
    OUTSTANDING("Outstanding", "बकाया")
}

data class Invoice(
    val id: String,
    val invoiceNumber: String,
    val clientId: String,
    val clientName: String,
    val caseId: String,
    val caseNumber: String,
    val description: String,
    val baseAmount: Double,
    val gstRatePercent: Double,
    val totalAmount: Double,
    val paidAmount: Double,
    val date: String,
    val dueDate: String,
    val status: InvoiceStatus
)

data class LimitationItem(
    val id: String,
    val eventTitle: String,
    val eventDate: String,
    val proceedingType: String,
    val act: String,
    val provision: String,
    val statutoryPeriod: String,
    val computedDeadline: String,
    val daysRemaining: Int,
    val exclusionChecklist: List<String>,
    val verified: Boolean
)

data class TeamMember(
    val id: String,
    val name: String,
    val role: String, // Senior Advocate, Associate Advocate, Junior Advocate, Paralegal, Org Admin
    val email: String,
    val activeCasesCount: Int,
    val assignedSpeciality: String
)

data class CaseIntelligenceAnalysis(
    val caseId: String,
    val executiveSummary: String,
    val materialFacts: List<String>,
    val proceduralHistory: String,
    val chronology: List<Pair<String, String>>, // Date to Event
    val undisputedFacts: List<String>,
    val disputedFacts: List<String>,
    val issuesForDetermination: List<String>,
    val applicableProvisions: List<String>,
    val petitionerCase: String,
    val respondentDefence: String,
    val potentialWeaknesses: List<String>,
    val recommendedArguments: List<String>,
    val reliefSought: String,
    val missingInformation: List<String>,
    val questionsForResearch: List<String>
)

data class CourtroomPrepKit(
    val caseId: String,
    val onePageBrief: String,
    val oralArgumentOutline: List<String>,
    val benchQuestionsPredicted: List<Pair<String, String>>, // Question to Proposed Answer
    val opposingCounselPoints: List<String>,
    val rejoinderPoints: List<String>,
    val documentsToCarry: List<String>,
    val hearingDayChecklist: List<Pair<String, Boolean>>
)

enum class SubscriptionPlan(
    val id: String,
    val title: String,
    val priceMonthlyInr: Int,
    val description: String,
    val maxCases: String,
    val features: List<String>
) {
    FREE("free", "Free Starter", 0, "Basic litigation diary & management", "Up to 5 Cases", listOf("Basic Case Management", "Single Device Diary", "Standard Document Indexing", "Demo Drafting Templates")),
    SOLO("solo", "Solo Advocate", 299, "Full practice toolkit for independent advocates", "Up to 50 Cases", listOf("Expanded Case Management", "Complete Drafting Library (50+ templates)", "Limitation Act Calculator", "Client Billing & Invoicing", "Courtroom Prep Briefs")),
    PROFESSIONAL("pro", "Professional Practice", 699, "AI-powered legal intelligence for litigators", "Unlimited Cases", listOf("Case Intelligence 12-Tab Workspace", "AI Legal Assistant (11 Workflows)", "Precedent & Citation Cross-referencing", "Document OCR & Index Synthesis", "Bilingual EN/HI Support")),
    LAW_FIRM("firm", "Law Firm & Chambers", 1999, "Multi-advocate chambers & collaborative practice", "Enterprise / Multi-seat", listOf("Multi-user Team Workspace (Roles & Permissions)", "Shared Case Allocation & Audit Logs", "Firm-wide Cause List & Diary", "Advanced Billing & Partner Analytics", "Priority Dedicated Cloud Storage"))
}
