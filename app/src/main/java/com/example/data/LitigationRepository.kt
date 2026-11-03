package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object DemoDataProvider {

    fun getSampleCases(): List<CourtCase> = listOf(
        CourtCase(
            id = "case_1",
            title = "M/s Apex Infra Projects Pvt. Ltd. v. Northern Energy Transmission Corp. & Anr.",
            caseNumber = "CS(COMM) 412/2024",
            cnrNumber = "DLDH010045232024",
            courtName = "High Court of Delhi (New Delhi)",
            courtEstablishment = "Commercial Division, Original Side",
            category = CaseCategory.COMMERCIAL,
            caseType = "CS(COMM) - Commercial Civil Suit",
            filingNumber = "FIL/DEL/2024/09841",
            filingDate = "2024-04-18",
            registrationDate = "2024-04-22",
            petitioner = "M/s Apex Infra Projects Pvt. Ltd.",
            respondent = "Northern Energy Transmission Corp. & State Bank of India",
            advocateName = "Adv. Rajesh Malhotra (D/1420/2008)",
            opposingCounsel = "Adv. K. G. Nambiar, Senior Standing Counsel",
            judgeCourtroom = "Hon'ble Mr. Justice Sanjeev Narula / Court No. 14",
            currentStage = CaseStage.ARGUMENTS,
            status = CaseStatus.ACTIVE,
            nextHearingDate = "2026-11-05", // Approaching!
            nextHearingPurpose = "Arguments on I.A. 8412/2024 (Ex-Parte Ad-Interim Injunction against invocation of Bank Guarantee)",
            limitationDate = "2026-11-20",
            clientName = "M/s Apex Infra Projects Pvt. Ltd.",
            clientId = "client_1",
            notes = "Stay granted ad-interim on 24.04.2024 subject to deposit of Rs. 1.5 Cr. Reply filed by D-1. Rejoinder filed. Rejoinder arguments list ready.",
            isDemo = true
        ),
        CourtCase(
            id = "case_2",
            title = "Rakesh Kumar Sharma v. State (NCT of Delhi)",
            caseNumber = "BAIL APPLN. 1824/2024",
            cnrNumber = "DLDH020089122024",
            courtName = "High Court of Delhi",
            courtEstablishment = "Criminal Jurisdiction (Single Bench)",
            category = CaseCategory.CRIMINAL,
            caseType = "BAIL APPLN. u/s 439 CrPC / 483 BNSS",
            filingNumber = "FIL/DEL/2024/11204",
            filingDate = "2024-07-10",
            registrationDate = "2024-07-12",
            petitioner = "Rakesh Kumar Sharma",
            respondent = "State (Govt. of NCT of Delhi)",
            advocateName = "Adv. Rajesh Malhotra (D/1420/2008)",
            opposingCounsel = "Mr. Satish Aggarwal, Learned APP for State",
            judgeCourtroom = "Hon'ble Ms. Justice Neena Bansal Krishna / Court No. 31",
            currentStage = CaseStage.ARGUMENTS,
            status = CaseStatus.ACTIVE,
            nextHearingDate = "2026-11-03", // Today!
            nextHearingPurpose = "Arguments on Regular Bail. Status report filed by Investigating Officer.",
            limitationDate = "N/A",
            clientName = "Rakesh Kumar Sharma",
            clientId = "client_2",
            notes = "Co-accused granted bail by Supreme Court on 15.09.2024. Main ground is parity and completion of investigation.",
            isDemo = true
        ),
        CourtCase(
            id = "case_3",
            title = "Citizens Coalition for Urban Rights v. Union of India & Ors.",
            caseNumber = "W.P.(C) 9821/2024",
            cnrNumber = "DLDH010123982024",
            courtName = "High Court of Delhi",
            courtEstablishment = "Division Bench - Public Interest Litigation",
            category = CaseCategory.CONSTITUTIONAL,
            caseType = "W.P.(C) - Writ Petition (Civil) Art. 226",
            filingNumber = "FIL/DEL/2024/05401",
            filingDate = "2024-02-14",
            registrationDate = "2024-02-18",
            petitioner = "Citizens Coalition for Urban Rights",
            respondent = "Union of India, Ministry of Environment & GNCTD",
            advocateName = "Adv. Rajesh Malhotra & Adv. Sneha Sen",
            opposingCounsel = "Additional Solicitor General of India",
            judgeCourtroom = "Hon'ble The Chief Justice & Justice Manmeet P. S. Arora / Court No. 1",
            currentStage = CaseStage.REPLY,
            status = CaseStatus.ACTIVE,
            nextHearingDate = "2026-11-18",
            nextHearingPurpose = "Filing of comprehensive compliance affidavit by Delhi Pollution Control Committee (DPCC)",
            limitationDate = "2026-11-28",
            clientName = "Citizens Coalition for Urban Rights",
            clientId = "client_3",
            notes = "Notice issued on 22.02.2024. Committee constituted for Yamuna river basin inspection.",
            isDemo = true
        ),
        CourtCase(
            id = "case_4",
            title = "Sunil Gupta v. Ananya Tech Solutions Pvt. Ltd. & Ors.",
            caseNumber = "CC NO. 4521/2024",
            cnrNumber = "DLCT110034212024",
            courtName = "Chief Metropolitan Magistrate, Central, Tis Hazari Courts",
            courtEstablishment = "Special NI Act Court",
            category = CaseCategory.COMMERCIAL,
            caseType = "Complaint Case u/s 138 Negotiable Instruments Act",
            filingNumber = "FIL/THC/2024/03211",
            filingDate = "2024-05-02",
            registrationDate = "2024-05-08",
            petitioner = "Sunil Gupta",
            respondent = "Ananya Tech Solutions Pvt. Ltd. & Managing Director",
            advocateName = "Adv. Rajesh Malhotra",
            opposingCounsel = "Adv. Vivek Sharma",
            judgeCourtroom = "Sh. Ashish Rastogi, Ld. MM (NI Act-02), Room 28",
            currentStage = CaseStage.EVIDENCE,
            status = CaseStatus.ACTIVE,
            nextHearingDate = "2026-11-09",
            nextHearingPurpose = "Cross-examination of Complainant (CW-1). Original cheques and return memo in record.",
            limitationDate = "2026-11-30",
            clientName = "Sunil Gupta",
            clientId = "client_4",
            notes = "Dishonoured cheque amount: Rs. 42,50,000/-. Statutory notice delivered on 28.03.2024.",
            isDemo = true
        )
    )

    fun getSampleHearings(): List<Hearing> = listOf(
        Hearing(
            id = "h_1",
            caseId = "case_2",
            caseTitle = "Rakesh Kumar Sharma v. State (NCT of Delhi)",
            caseNumber = "BAIL APPLN. 1824/2024",
            courtName = "High Court of Delhi",
            courtroom = "Court No. 31 (Hon'ble Justice Neena Bansal Krishna)",
            date = "2026-11-03", // Today!
            time = "10:30 AM (Item No. 18)",
            purpose = "Final Arguments on Regular Bail under Section 439 CrPC / 483 BNSS",
            judge = "Hon'ble Ms. Justice Neena Bansal Krishna",
            previousOrderSummary = "Status report called from IO. IO filed report stating investigation complete and charge-sheet filed on 28.08.2024 without arrest.",
            nextSteps = "Highlight parity with A-2 who was enlarged on bail. Cite Satender Kumar Antil guideline.",
            adjournmentHistory = listOf("02.09.2024: Adjourned at request of APP for filing nominal roll", "24.09.2024: APP sought time to verify co-accused order"),
            reminderSet = true,
            isCompleted = false,
            hearingNotes = "Item 18 likely to reach between 11:15 AM - 11:45 AM. Keep 3 sets of Satender Kumar Antil ready."
        ),
        Hearing(
            id = "h_2",
            caseId = "case_1",
            caseTitle = "M/s Apex Infra Projects Pvt. Ltd. v. Northern Energy Transmission Corp.",
            caseNumber = "CS(COMM) 412/2024",
            courtName = "High Court of Delhi",
            courtroom = "Court No. 14 (Hon'ble Justice Sanjeev Narula)",
            date = "2026-11-05",
            time = "02:15 PM (Item No. 4)",
            purpose = "Arguments on I.A. 8412/2024 (Injunction restraining invocation of Bank Guarantee of Rs. 14.8 Cr)",
            judge = "Hon'ble Mr. Justice Sanjeev Narula",
            previousOrderSummary = "Interim protection continued. Parties directed to file brief synopsis not exceeding 3 pages along with indexed convenience compilation.",
            nextSteps = "Tender convenience compilation. Rebut allegations of fraudulent encashment.",
            adjournmentHistory = listOf("10.08.2024: Board discharged", "18.09.2024: Arguing counsel for D-1 indisposed"),
            reminderSet = true,
            isCompleted = false,
            hearingNotes = "Focus on special equities: COVID disruption clause + force majeure notice dated 12.01.2024."
        ),
        Hearing(
            id = "h_3",
            caseId = "case_4",
            caseTitle = "Sunil Gupta v. Ananya Tech Solutions Pvt. Ltd.",
            caseNumber = "CC NO. 4521/2024",
            courtName = "Tis Hazari Courts, Delhi",
            courtroom = "Room 28 (Ld. MM NI Act-02)",
            date = "2026-11-09",
            time = "11:00 AM (Item No. 7)",
            purpose = "Complainant Evidence & Cross-examination of CW-1",
            judge = "Sh. Ashish Rastogi, Ld. MM",
            previousOrderSummary = "Notice framed u/s 251 CrPC. Accused pleaded not guilty and claimed defence of security cheque.",
            nextSteps = "Produce original ledger account and invoices acknowledging delivery of software modules.",
            adjournmentHistory = listOf("12.07.2024: Presiding officer on training"),
            reminderSet = true,
            isCompleted = false,
            hearingNotes = "Cross check CW-1 affidavit with bank certificate of cheque return memo."
        ),
        Hearing(
            id = "h_4",
            caseId = "case_3",
            caseTitle = "Citizens Coalition for Urban Rights v. Union of India",
            caseNumber = "W.P.(C) 9821/2024",
            courtName = "High Court of Delhi",
            courtroom = "Court No. 1 (Division Bench - Chief Justice)",
            date = "2026-11-18",
            time = "10:30 AM (Item No. 2)",
            purpose = "Scrutiny of compliance status by Municipal Authorities",
            judge = "Hon'ble The Chief Justice",
            previousOrderSummary = "Time extended by 4 weeks for DPCC to inspect downstream industrial discharge points.",
            nextSteps = "File rejoinder to DPCC status report highlighting satellite telemetry data.",
            adjournmentHistory = listOf("14.05.2024: State sought time for inter-departmental meeting"),
            reminderSet = true,
            isCompleted = false,
            hearingNotes = "Prepare brief 2-page note on continuing contempt if no report filed by 10.11.2026."
        )
    )

    fun getSampleDrafts(): List<LegalDraft> = listOf(
        LegalDraft(
            id = "draft_1",
            caseId = "case_1",
            title = "Application under Order 39 Rules 1 & 2 read with Section 151 CPC for Ex-Parte Ad-Interim Injunction",
            documentType = "Interim Injunction Application",
            category = CaseCategory.COMMERCIAL,
            courtName = "IN THE HIGH COURT OF DELHI AT NEW DELHI (COMMERCIAL DIVISION)",
            language = "English",
            content = """IN THE HIGH COURT OF DELHI AT NEW DELHI
(COMMERCIAL DIVISION)
COMMERCIAL SUIT NO. 412 OF 2024
IN THE MATTER OF:
M/s Apex Infra Projects Pvt. Ltd.                     ... PLAINTIFF/APPLICANT
                                    VERSUS
Northern Energy Transmission Corp. & Anr.             ... DEFENDANTS/RESPONDENTS

APPLICATION UNDER ORDER XXXIX RULES 1 AND 2 READ WITH SECTION 151 OF THE CODE OF CIVIL PROCEDURE, 1908 SEEKING EX-PARTE AD-INTERIM INJUNCTION

MOST RESPECTFULLY SHOWETH:
1. That the Applicant/Plaintiff has instituted the accompanying Commercial Suit seeking declaration, permanent injunction and damages against the illegal and fraudulent invocation of Bank Guarantee No. BG/2022/984 dated 14.02.2022 for an amount of Rs. 14,80,00,000/- (Rupees Fourteen Crores Eighty Lakhs Only).
2. That the Plaintiff is a registered infrastructure company and was awarded the EPC Contract for transmission grid package vide LOA dated 12.01.2022.
3. That as per Clause 18 of the Contract, invocation of the Performance Bank Guarantee is conditional upon a prior 30-day cure notice and a joint site measurement protocol.
4. That Defendant No. 1 has, without adhering to Clause 18 and without issuing any pre-invocation cure notice, fraudulently written to Defendant No. 2 (State Bank of India) on [INVOCATION_DATE] seeking encashment of the said Bank Guarantee.
5. That in the present case, 'irretrievable injustice' and 'egregious fraud' are clearly established inasmuch as the project milestone has been certified as 92% complete by the Independent Engineer appointed by Defendant No. 1 itself.
6. That the Plaintiff has a strong prima facie case, the balance of convenience lies overwhelmingly in favour of the Plaintiff, and the Plaintiff will suffer irreparable injury which cannot be compensated in monetary terms if the encashment is not restrained.

PRAYER:
Wherefore, in light of the above facts, it is respectfully prayed that this Hon'ble Court may be pleased to:
(a) Pass an ex-parte ad-interim injunction restraining Defendant No. 1 from invoking or encashing, and Defendant No. 2 from disbursing any sums under Bank Guarantee No. BG/2022/984;
(b) Pass such other or further order(s) as this Hon'ble Court may deem fit and proper in the interest of justice.

AND FOR THIS ACT OF KINDNESS, THE APPLICANT SHALL EVER PRAY.
THROUGH
[ADVOCATE_NAME]
COUNSEL FOR PLAINTIFF
NEW DELHI
DATED: [CURRENT_DATE]""",
            missingInformation = listOf("[INVOCATION_DATE]", "[ADVOCATE_NAME]", "[CURRENT_DATE]"),
            legalAuthorities = listOf(
                "U.P. State Sugar Corporation v. Sumac International Ltd. (1997) 1 SCC 568",
                "BSNL v. Bhurumal (2014) 7 SCC 177",
                "Standard Chartered Bank v. Heavy Engineering Corporation Ltd. (2020) 13 SCC 574"
            ),
            status = "Ready for Review",
            lastUpdated = "2026-11-02 18:30"
        ),
        LegalDraft(
            id = "draft_2",
            caseId = "case_2",
            title = "Regular Bail Application under Section 439 CrPC read with Section 483 Bharatiya Nagarik Suraksha Sanhita (BNSS), 2023",
            documentType = "Regular Bail Application",
            category = CaseCategory.CRIMINAL,
            courtName = "IN THE HIGH COURT OF DELHI AT NEW DELHI",
            language = "English",
            content = """IN THE HIGH COURT OF DELHI AT NEW DELHI
CRIMINAL MISCELLANEOUS JURISDICTION
BAIL APPLICATION NO. 1824 OF 2024
IN THE MATTER OF:
Rakesh Kumar Sharma                                   ... APPLICANT/PETITIONER
                                    VERSUS
State (Govt. of NCT of Delhi)                         ... RESPONDENT

APPLICATION UNDER SECTION 439 OF THE CODE OF CRIMINAL PROCEDURE, 1973 READ WITH SECTION 483 OF BHARATIYA NAGARIK SURAKSHA SANHITA, 2023 FOR GRANT OF REGULAR BAIL IN FIR NO. 312/2023 U/S 420/468/471/120B IPC REGISTERED AT P.S. SPECIAL CELL, NEW DELHI.

MOST RESPECTFULLY SHOWETH:
1. That the Applicant has been falsely implicated in the above-mentioned FIR and has been in judicial custody since 14.05.2024 (a period exceeding 170 days).
2. That the investigation in the matter is complete and the Investigating Agency has already submitted the Charge-Sheet before the Ld. Trial Court on 28.08.2024. No recovery or custodial interrogation is required.
3. PARITY WITH CO-ACCUSED: That the identically situated co-accused person, namely [CO_ACCUSED_NAME], has already been admitted to regular bail by the Hon'ble Supreme Court vide order dated [SUPREME_COURT_ORDER_DATE].
4. That the entire dispute is rooted in commercial transactions arising out of vendor contracts, which have been given a criminal colour to exert illegitimate pressure.
5. That the Applicant is a permanent resident of Delhi with deep roots in society, has no previous criminal antecedents, and undertakes not to tamper with any evidence or influence witnesses.

PRAYER:
It is therefore most respectfully prayed that this Hon'ble Court may graciously be pleased to:
(a) Enlarge the Applicant on regular bail in FIR No. 312/2023 registered at P.S. Special Cell, New Delhi, on such terms and conditions as this Hon'ble Court may deem just and fit;
(b) Pass any other order in the interest of justice.

FILED BY:
[COUNSEL_FOR_APPLICANT]
NEW DELHI
DATED: [DATE]""",
            missingInformation = listOf("[CO_ACCUSED_NAME]", "[SUPREME_COURT_ORDER_DATE]", "[COUNSEL_FOR_APPLICANT]", "[DATE]"),
            legalAuthorities = listOf(
                "Satender Kumar Antil v. Central Bureau of Investigation (2022) 10 SCC 51",
                "Sanjay Chandra v. CBI (2012) 1 SCC 40",
                "Manish Sisodia v. Directorate of Enforcement 2024 INSC 595"
            ),
            status = "Draft",
            lastUpdated = "2026-11-01 14:15"
        )
    )

    fun getSampleDocuments(): List<CaseDocument> = listOf(
        CaseDocument(
            id = "doc_1",
            caseId = "case_1",
            title = "Plaint with Statement of Truth and Affidavits under Commercial Courts Act",
            category = DocumentCategory.PLEADINGS,
            exhibitNumber = "Pleading-1",
            dateOfDocument = "2024-04-18",
            uploadDate = "2024-04-19",
            source = "Advocate Office (Original Signed)",
            tags = listOf("Plaint", "Statement of Truth", "Commercial Suit"),
            notes = "Duly notarised and stamped as per High Court Original Side Rules.",
            ocrReady = true,
            ocrSnippet = "Plaint instituted under Section 2(1)(c) of the Commercial Courts Act, 2015 along with pre-institution mediation non-starter report dated 10.04.2024.",
            fileSizeKb = 1240
        ),
        CaseDocument(
            id = "doc_2",
            caseId = "case_1",
            title = "Performance Bank Guarantee No. BG/2022/984 issued by State Bank of India",
            category = DocumentCategory.EVIDENCE,
            exhibitNumber = "Ex. P-1",
            dateOfDocument = "2022-02-14",
            uploadDate = "2024-04-19",
            source = "State Bank of India, CAG Branch, New Delhi",
            tags = listOf("Bank Guarantee", "Primary Evidence", "Contract Document"),
            notes = "Clause 18 stipulates pre-invocation cure period of 30 days.",
            ocrReady = true,
            ocrSnippet = "We, State Bank of India, hereby irrevocably guarantee up to an aggregate amount of Rs. 14,80,00,000/- upon receipt of written demand referencing Clause 18.",
            fileSizeKb = 850
        ),
        CaseDocument(
            id = "doc_3",
            caseId = "case_1",
            title = "Impugned Invocation Notice dated 12.04.2024 issued by Defendant No. 1",
            category = DocumentCategory.CORRESPONDENCE,
            exhibitNumber = "Ex. P-2",
            dateOfDocument = "2024-04-12",
            uploadDate = "2024-04-19",
            source = "Northern Energy Transmission Corp.",
            tags = listOf("Invocation Notice", "Fraudulent Demand"),
            notes = "Crucial document for proving non-compliance with dispute resolution mechanism.",
            ocrReady = true,
            ocrSnippet = "You are hereby requested to invoke and encash the bank guarantee forthwith without reference to the contractor.",
            fileSizeKb = 320
        ),
        CaseDocument(
            id = "doc_4",
            caseId = "case_2",
            title = "Charge-Sheet u/s 173 CrPC filed by Special Cell in FIR 312/2023",
            category = DocumentCategory.ORDERS,
            exhibitNumber = "Annexure P-3",
            dateOfDocument = "2024-08-28",
            uploadDate = "2024-08-30",
            source = "Court of Ld. Chief Metropolitan Magistrate, PHC",
            tags = listOf("Charge Sheet", "Final Report", "No Custodial Need"),
            notes = "Chargesheet records that all electronic records are in custody of FSL Rohini.",
            ocrReady = true,
            ocrSnippet = "Investigation against accused Rakesh Kumar Sharma completed. No further custodial interrogation is necessitated at this juncture.",
            fileSizeKb = 4200
        )
    )

    fun getSampleAuthorities(): List<LegalAuthority> = listOf(
        LegalAuthority(
            id = "auth_1",
            caseId = "case_1",
            title = "Standard Chartered Bank v. Heavy Engineering Corporation Ltd. & Anr.",
            citation = "(2020) 13 SCC 574",
            court = "Supreme Court of India",
            year = 2020,
            bench = "Hon'ble Banumathi, Bopanna and Hrishikesh Roy, JJ.",
            ratioDecidendi = "Bank Guarantee encashment can be injuncted only in cases of egregious fraud of a thriving nature to the knowledge of the bank or irretrievable harm/injustice creating special equities.",
            relevantParagraphs = "Paras 19, 21: 'The exception of special equities comes into play where the invocation of bank guarantee would result in an irreversible economic injury, particularly where statutory conciliation or mediation is pending...'",
            isOverruled = false,
            statusNote = "Good law. Consistently followed by Delhi High Court Commercial Division.",
            sourceUrl = "https://main.sci.gov.in/judgment/2020"
        ),
        LegalAuthority(
            id = "auth_2",
            caseId = "case_2",
            title = "Satender Kumar Antil v. Central Bureau of Investigation & Anr.",
            citation = "(2022) 10 SCC 51",
            court = "Supreme Court of India",
            year = 2022,
            bench = "Hon'ble S.K. Kaul and M.M. Sundresh, JJ.",
            ratioDecidendi = "Category A, B, C, D classification for bail. When an accused was not arrested during investigation and cooperated, bail should ordinarily be granted on filing of chargesheet.",
            relevantParagraphs = "Paras 28-34: 'Bail is the rule and jail is the exception. In offences punishable with less than 7 years or where custodial investigation was not deemed necessary by the IO, trial court should grant bail upon appearance.'",
            isOverruled = false,
            statusNote = "Binding Pan-India Guideline for Criminal Courts.",
            sourceUrl = "https://main.sci.gov.in/judgment/2022"
        ),
        LegalAuthority(
            id = "auth_3",
            caseId = "case_3",
            title = "M.C. Mehta v. Union of India & Ors. (Yamuna Pollution Matter)",
            citation = "(2004) 12 SCC 118",
            court = "Supreme Court of India",
            year = 2004,
            bench = "Hon'ble Y.K. Sabharwal and H.K. Sema, JJ.",
            ratioDecidendi = "Strict application of Public Trust Doctrine and Polluter Pays Principle for non-conforming industrial units discharging effluents into river ecosystems.",
            relevantParagraphs = "Paras 42-45: Mandates closure of unregistered industrial units failing zero liquid discharge norms.",
            isOverruled = false,
            statusNote = "Constitutional Benchmark Precedent.",
            sourceUrl = "https://main.sci.gov.in"
        )
    )

    fun getSampleClients(): List<Client> = listOf(
        Client(
            id = "client_1",
            name = "Apex Infra Projects Pvt. Ltd.",
            organization = "Apex Infrastructure Group",
            email = "legal@apexinfra.co.in",
            phone = "+91 98110 44210",
            address = "704, Barakhamba Road, Connaught Place, New Delhi 110001",
            clientType = "Corporate Entity",
            activeCases = 1,
            outstandingFees = 75000.0,
            consultationNotes = "Retainer agreement renewed on 01.01.2024. Next hearing fee to be billed on Nov 05, 2026."
        ),
        Client(
            id = "client_2",
            name = "Rakesh Kumar Sharma",
            organization = "Individual",
            email = "rakesh.sharma.consult@gmail.com",
            phone = "+91 98731 29401",
            address = "C-42, Hauz Khas, New Delhi 110016",
            clientType = "Individual Litigant",
            activeCases = 1,
            outstandingFees = 45000.0,
            consultationNotes = "Family contacted regarding bail hearing. Advance received: Rs. 50,000/-. Balance on argument conclusion."
        ),
        Client(
            id = "client_3",
            name = "Citizens Coalition for Urban Rights",
            organization = "Registered Society / Trust",
            email = "contact@citizenscoalition.org",
            phone = "+91 94120 88203",
            address = "12, Institutional Area, Lodhi Road, New Delhi 110003",
            clientType = "NGO / Public Trust",
            activeCases = 1,
            outstandingFees = 0.0,
            consultationNotes = "Pro bono PIL representation. Only out of pocket court fees and translation expenses reimbursed."
        ),
        Client(
            id = "client_4",
            name = "Sunil Gupta",
            organization = "Proprietor, Gupta Trading Co.",
            email = "sunilgupta.del@gmail.com",
            phone = "+91 99991 18274",
            address = "B-14, Chawri Bazar, Delhi 110006",
            clientType = "Commercial Individual",
            activeCases = 1,
            outstandingFees = 20000.0,
            consultationNotes = "Section 138 NI Act proceeding. Full original cheque records safe in chamber vault."
        )
    )

    fun getSampleInvoices(): List<Invoice> = listOf(
        Invoice(
            id = "inv_101",
            invoiceNumber = "INV/LEX/2026/042",
            clientId = "client_1",
            clientName = "Apex Infra Projects Pvt. Ltd.",
            caseId = "case_1",
            caseNumber = "CS(COMM) 412/2024",
            description = "Drafting & filing of Rejoinder Affidavit and Appearance for Interim Stay arguments before Delhi High Court",
            baseAmount = 150000.0,
            gstRatePercent = 18.0,
            totalAmount = 177000.0,
            paidAmount = 102000.0,
            date = "2026-10-15",
            dueDate = "2026-11-10",
            status = InvoiceStatus.PARTIAL
        ),
        Invoice(
            id = "inv_102",
            invoiceNumber = "INV/LEX/2026/043",
            clientId = "client_2",
            clientName = "Rakesh Kumar Sharma",
            caseId = "case_2",
            caseNumber = "BAIL APPLN. 1824/2024",
            description = "Professional fee for drafting Regular Bail Application and Senior Counsel briefing conference",
            baseAmount = 80000.0,
            gstRatePercent = 18.0,
            totalAmount = 94400.0,
            paidAmount = 49400.0,
            date = "2026-10-22",
            dueDate = "2026-11-05",
            status = InvoiceStatus.PARTIAL
        ),
        Invoice(
            id = "inv_103",
            invoiceNumber = "INV/LEX/2026/044",
            clientId = "client_4",
            clientName = "Sunil Gupta",
            caseId = "case_4",
            caseNumber = "CC NO. 4521/2024",
            description = "Drafting Statutory Legal Notice under Section 138 NI Act and drafting Complaint with Affidavit",
            baseAmount = 35000.0,
            gstRatePercent = 18.0,
            totalAmount = 41300.0,
            paidAmount = 41300.0,
            date = "2026-09-18",
            dueDate = "2026-10-02",
            status = InvoiceStatus.PAID
        )
    )

    fun getSampleLimitationItems(): List<LimitationItem> = listOf(
        LimitationItem(
            id = "lim_1",
            eventTitle = "Filing Written Statement in Commercial Suit",
            eventDate = "2026-10-10",
            proceedingType = "Written Statement / Defence",
            act = "Commercial Courts Act, 2015 read with CPC Order VIII Rule 1",
            provision = "Proviso to Order VIII Rule 1 CPC (Commercial Courts Amendment)",
            statutoryPeriod = "30 Days (Extendable up to 120 days with costs, forfeiture thereafter)",
            computedDeadline = "2026-11-09",
            daysRemaining = 6,
            exclusionChecklist = listOf(
                "30-day statutory window expires on 09.11.2026",
                "Beyond 120 days is non-condonable forfeiture (SC in SCG Contracts)",
                "Ensure affidavit of admission/denial is appended"
            ),
            verified = true
        ),
        LimitationItem(
            id = "lim_2",
            eventTitle = "Statutory Complaint u/s 138 Negotiable Instruments Act",
            eventDate = "2026-10-16",
            proceedingType = "Criminal Complaint under Special Act",
            act = "Negotiable Instruments Act, 1881",
            provision = "Section 142(1)(b) read with Section 138 proviso (c)",
            statutoryPeriod = "1 Month (30 Days) from expiry of 15 days notice period",
            computedDeadline = "2026-11-30",
            daysRemaining = 27,
            exclusionChecklist = listOf(
                "Statutory notice served on 16.10.2026",
                "15 days cure period ends on 31.10.2026",
                "Cause of action arose on 01.11.2026",
                "30-day window to file complaint closes on 30.11.2026"
            ),
            verified = true
        ),
        LimitationItem(
            id = "lim_3",
            eventTitle = "Section 34 Petition to Challenge Arbitral Award",
            eventDate = "2026-08-20",
            proceedingType = "Arbitration Petition",
            act = "Arbitration & Conciliation Act, 1996",
            provision = "Section 34(3) with proviso (Strict 3 months + 30 days condonation)",
            statutoryPeriod = "3 Months (90 Days) + max 30 days discretionary condonation",
            computedDeadline = "2026-11-20",
            daysRemaining = 17,
            exclusionChecklist = listOf(
                "Signed arbitral award received on 20.08.2026",
                "3-month period expires on 20.11.2026",
                "Section 5 Limitation Act does not apply (Simplex Infrastructure SC)",
                "Must file within 90 days to avoid threshold risk"
            ),
            verified = true
        )
    )

    fun getSampleTeam(): List<TeamMember> = listOf(
        TeamMember(
            id = "team_1",
            name = "Adv. Rajesh Malhotra",
            role = "Senior Advocate / Chamber Head",
            email = "malhotra@litigationos.law",
            activeCasesCount = 18,
            assignedSpeciality = "High Court Commercial Division & Arbitrations"
        ),
        TeamMember(
            id = "team_2",
            name = "Adv. Ananya Roy",
            role = "Principal Associate",
            email = "ananya.roy@litigationos.law",
            activeCasesCount = 12,
            assignedSpeciality = "Civil & Constitutional Writs"
        ),
        TeamMember(
            id = "team_3",
            name = "Adv. Sneha Sen",
            role = "Associate Advocate",
            email = "sneha.sen@litigationos.law",
            activeCasesCount = 9,
            assignedSpeciality = "Criminal Law & Special Courts"
        ),
        TeamMember(
            id = "team_4",
            name = "Rohan Verma",
            role = "Paralegal & Research Clerk",
            email = "rohan.v@litigationos.law",
            activeCasesCount = 24,
            assignedSpeciality = "Registry Filings, Limitation Audits & E-Courts Tracking"
        )
    )

    fun getSampleIntelligenceAnalysis(caseId: String): CaseIntelligenceAnalysis = when (caseId) {
        "case_2" -> CaseIntelligenceAnalysis(
            caseId = caseId,
            executiveSummary = "Bail Application under Section 439 CrPC read with Section 483 BNSS in FIR No. 312/2023 u/s 420/468/471/120B IPC registered by Special Cell. The investigation is complete, chargesheet has been filed before the Ld. CMM without arrest, and identical co-accused has been enlarged on regular bail by the Supreme Court.",
            materialFacts = listOf(
                "FIR registered on 14.10.2023 alleging vendor contract financial irregularities.",
                "Applicant was not arrested during investigation and cooperated on 8 distinct dates.",
                "Charge-sheet submitted before Ld. Trial Court on 28.08.2024.",
                "Identical co-accused Director granted regular bail by Supreme Court on 15.09.2024.",
                "Applicant has spent over 170 days in pre-trial judicial custody."
            ),
            proceduralHistory = "Bail application dismissed by Sessions Court on 10.06.2024 prior to chargesheet. High Court approached post-chargesheet in view of substantial change in circumstances.",
            chronology = listOf(
                "14.10.2023" to "FIR No. 312/2023 registered at PS Special Cell.",
                "14.05.2024" to "Applicant remanded to judicial custody upon surrender.",
                "28.08.2024" to "Charge-sheet filed u/s 173 CrPC without requiring police custody.",
                "15.09.2024" to "Supreme Court grants bail to co-accused on parity.",
                "03.11.2026" to "Listed for final arguments before High Court (Item 18, Court 31)."
            ),
            undisputedFacts = listOf(
                "Investigation is concluded and chargesheet is already on record.",
                "All electronic servers and ledgers are sealed with FSL Rohini.",
                "Applicant has clean antecedents with no prior convictions."
            ),
            disputedFacts = listOf(
                "Extent of knowledge regarding sub-contractor invoicing inaccuracies.",
                "Whether Applicant exercised executive signing authority."
            ),
            issuesForDetermination = listOf(
                "1. Whether Applicant is entitled to regular bail on doctrine of parity with co-accused?",
                "2. Whether continued incarceration post-chargesheet violates Article 21 rights?",
                "3. Whether Category 'A' parameters in Satender Kumar Antil apply?"
            ),
            applicableProvisions = listOf(
                "Section 439, Code of Criminal Procedure, 1973",
                "Section 483, Bharatiya Nagarik Suraksha Sanhita (BNSS), 2023",
                "Articles 14 and 21 of the Constitution of India",
                "Sections 420, 468, 471, 120B Indian Penal Code, 1860"
            ),
            petitionerCase = "Investigation completed without arrest. Co-accused granted bail. Continued incarceration is purely punitive.",
            respondentDefence = "State APP opposes bail on magnitude of commercial transactions and apprehension of witness influence.",
            potentialWeaknesses = listOf(
                "Section 420/467 carries serious penalties.",
                "Need to refute flight risk conclusively with passport surrender undertaking."
            ),
            recommendedArguments = listOf(
                "Invoke Satender Kumar Antil (2022) 10 SCC 51 Category A guidelines.",
                "Emphasize parity with co-accused enlarged by Supreme Court on 15.09.2024.",
                "Point out trial will take years with 48 prosecution witnesses."
            ),
            reliefSought = "Grant of regular bail on furnishing personal bond with two local sureties.",
            missingInformation = listOf(
                "Certified copy of Supreme Court co-accused bail order dated 15.09.2024.",
                "Nominal Roll certificate from Jail Superintendent Tihar."
            ),
            questionsForResearch = listOf(
                "Delhi HC rulings granting bail where chargesheet was filed without arrest.",
                "Precedents on economic offences where custodial trial is unnecessary."
            )
        )
        "case_3" -> CaseIntelligenceAnalysis(
            caseId = caseId,
            executiveSummary = "Public Interest Litigation W.P.(C) 9821/2024 under Article 226 seeking writ of mandamus against untreated industrial effluent discharges into the Yamuna river basin by non-conforming industrial clusters in Delhi NCR.",
            materialFacts = listOf(
                "Satellite imagery reveals toxic foam formation at downstream barrages.",
                "18 industrial clusters operating without mandatory Zero Liquid Discharge (ZLD) plants.",
                "CPCB inspection notes biochemical oxygen demand (BOD) exceeding permissible thresholds."
            ),
            proceduralHistory = "Writ admitted on 18.02.2024. Notice issued to DPCC and Ministry of Environment. Court directed joint inspection committee to file compliance affidavit.",
            chronology = listOf(
                "14.02.2024" to "Writ Petition filed before Division Bench.",
                "22.02.2024" to "Notice issued with interim directions.",
                "18.11.2026" to "Scheduled for scrutiny of DPCC inspection status report."
            ),
            undisputedFacts = listOf(
                "River water parameters exceed safe public bathing standards.",
                "Statutory responsibility of DPCC under Water Act 1974."
            ),
            disputedFacts = listOf(
                "Whether industrial clusters or domestic sewage is the primary pollutant source."
            ),
            issuesForDetermination = listOf(
                "1. Whether statutory agencies are liable for continuing contempt under Water Act?",
                "2. Whether strict closure orders under Public Trust Doctrine should be issued?"
            ),
            applicableProvisions = listOf(
                "Article 226 and Article 21, Constitution of India",
                "Water (Prevention and Control of Pollution) Act, 1974",
                "Environment (Protection) Act, 1986"
            ),
            petitionerCase = "Right to clean environment is a fundamental right under Article 21.",
            respondentDefence = "Agencies claim shortage of effluent treatment infrastructure capacity.",
            potentialWeaknesses = listOf(
                "Need independent water testing lab reports to counter official departmental affidavits."
            ),
            recommendedArguments = listOf(
                "Cite M.C. Mehta v. Union of India (2004) 12 SCC 118 on Polluter Pays Principle.",
                "Seek court-monitored oversight committee."
            ),
            reliefSought = "Mandamus directing closure of non-compliant units and time-bound commissioning of CETPs.",
            missingInformation = listOf(
                "Independent NABL-accredited laboratory water analysis report.",
                "Current sewage treatment plant (STP) capacity utilization figures."
            ),
            questionsForResearch = listOf(
                "Recent Supreme Court directions regarding Yamuna river rejuvenation."
            )
        )
        "case_4" -> CaseIntelligenceAnalysis(
            caseId = caseId,
            executiveSummary = "Criminal Complaint under Section 138 Negotiable Instruments Act, 1881 for dishonour of Cheque No. 441029 dated 15.02.2024 for ₹42,50,000/- with remark 'Funds Insufficient'.",
            materialFacts = listOf(
                "Complainant supplied customized enterprise software modules against PO dated 10.11.2023.",
                "Accused issued Cheque No. 441029 drawn on HDFC Bank.",
                "Cheque dishonoured on presentation vide Return Memo dated 12.03.2024.",
                "Statutory demand notice issued on 28.03.2024 and delivered on 02.04.2024.",
                "Accused failed to make payment within 15 days."
            ),
            proceduralHistory = "Pre-summoning evidence led. Summons issued on 20.06.2024. Notice framed u/s 251 CrPC. Matter listed for Complainant Cross-examination (CW-1).",
            chronology = listOf(
                "15.02.2024" to "Cheque issued for ₹42,50,000/-.",
                "12.03.2024" to "Bank Return Memo received: Funds Insufficient.",
                "28.03.2024" to "Statutory Demand Notice dispatched via Speed Post.",
                "02.04.2024" to "Notice delivered as per India Post tracking report.",
                "02.05.2024" to "Criminal Complaint filed in Tis Hazari Courts."
            ),
            undisputedFacts = listOf(
                "Issuance of cheque and signature of Accused.",
                "Receipt of Bank Return Memo confirming insufficiency of funds."
            ),
            disputedFacts = listOf(
                "Accused claims cheque was issued as advance security without software delivery."
            ),
            issuesForDetermination = listOf(
                "1. Whether statutory presumption under Section 139 NI Act is successfully rebutted?",
                "2. Whether legally enforceable debt existed on date of cheque presentation?"
            ),
            applicableProvisions = listOf(
                "Sections 138, 139, 141, 142 Negotiable Instruments Act, 1881",
                "Section 65B Indian Evidence Act / Section 63 BSA 2023"
            ),
            petitionerCase = "Statutory presumption under Section 139 stands in Complainant's favour. Delivery accepted via signed acceptance protocol.",
            respondentDefence = "Security cheque defence and alleged technical bugs in software.",
            potentialWeaknesses = listOf(
                "Ensure Section 65B certificate for electronic emails is exhibited."
            ),
            recommendedArguments = listOf(
                "Rely on Basalingappa (2019) 5 SCC 418: Mere denial is insufficient to rebut Section 139.",
                "Produce signed client delivery receipts."
            ),
            reliefSought = "Conviction of Accused and award of compensation double the cheque amount.",
            missingInformation = listOf(
                "Certified Speed Post tracking delivery certificate stamped by Post Master.",
                "Original bank statement reflecting initial payment advances."
            ),
            questionsForResearch = listOf(
                "Recent rulings on Section 138 security cheque defence."
            )
        )
        else -> CaseIntelligenceAnalysis(
            caseId = caseId,
            executiveSummary = "M/s Apex Infra Projects has filed CS(COMM) 412/2024 to prevent Northern Energy Transmission Corp. from encashing a ₹14.8 Cr Performance Bank Guarantee. The primary legal ground is egregious fraud and special equities arising from COVID force majeure and 92% milestone completion certified by the joint inspection team.",
            materialFacts = listOf(
                "Contract executed on 12.01.2022 for EPC Sub-station grid package value ₹86 Crores.",
                "Performance Bank Guarantee BG/2022/984 furnished by State Bank of India on 14.02.2022.",
                "Clause 18 mandates a mandatory 30-day cure notice prior to invoking any security instruments.",
                "Defendant No. 1 issued unilateral invocation letter to SBI without serving cure notice on Plaintiff.",
                "Independent Engineer certified 92.4% physical completion on 28.02.2024."
            ),
            proceduralHistory = "Suit filed on 18.04.2024. Ex-parte ad-interim protection granted on 24.04.2024 by Single Bench subject to ₹1.5 Cr security deposit (duly complied). Written Statement filed by D-1 on 10.07.2024. Rejoinder filed on 12.08.2024. Matter now at stage of final arguments on I.A. 8412/2024.",
            chronology = listOf(
                "12.01.2022" to "EPC Contract executed between Apex Infra and Northern Energy.",
                "14.02.2022" to "Bank Guarantee furnished by State Bank of India.",
                "15.01.2024" to "Plaintiff served Force Majeure notice due to severe supply chain embargoes.",
                "28.02.2024" to "Joint Measurement Protocol certifies 92.4% milestone completion.",
                "12.04.2024" to "Defendant No. 1 writes directly to SBI demanding full invocation of ₹14.8 Cr.",
                "18.04.2024" to "Commercial Suit CS(COMM) 412/2024 instituted along with Order 39 Appln.",
                "24.04.2024" to "Hon'ble Delhi High Court grants interim stay against disbursement."
            ),
            undisputedFacts = listOf(
                "Execution of EPC contract and valid issuance of Bank Guarantee.",
                "Plaintiff deposited ₹1.5 Crores pursuant to interim order dated 24.04.2024.",
                "No termination notice of the substantive EPC contract has been issued to date."
            ),
            disputedFacts = listOf(
                "Whether delay in final testing commissioning was attributable to Plaintiff or grid unavailability.",
                "Whether invocation letter conforms strictly with terms of Bank Guarantee."
            ),
            issuesForDetermination = listOf(
                "1. Whether invocation of Bank Guarantee No. BG/2022/984 is tainted by egregious fraud and irretrievable injustice?",
                "2. Whether Defendant No. 1 is barred from encashing the guarantee without exhausting contractual cure mechanisms under Clause 18?",
                "3. Whether the interim injunction granted on 24.04.2024 ought to be made absolute pending trial?"
            ),
            applicableProvisions = listOf(
                "Order XXXIX Rules 1 and 2, Code of Civil Procedure, 1908",
                "Section 2(1)(c) and Section 12A, Commercial Courts Act, 2015",
                "Sections 126 and 127, Indian Contract Act, 1872",
                "Specific Relief Act, 1963 - Sections 38 and 41"
            ),
            petitionerCase = "Plaintiff asserts that invocation without 30-day notice is ultra vires the contract. Invocation of ₹14.8 Cr for a project with 92% completion when only ₹3.2 Cr minor works remain is extortionate and amounts to fraud of an egregious nature.",
            respondentDefence = "Defendant No. 1 contends that Bank Guarantee is an independent and unconditional contract between the issuing Bank and the beneficiary. Contractual disputes cannot be imported into guarantee encashment unless fraud in execution is proved.",
            potentialWeaknesses = listOf(
                "Supreme Court has consistently held in Sumac International and Standard Chartered that unconditional bank guarantees should not be stayed routinely.",
                "Need to ensure that fraud alleged is of a thriving and egregious nature to the knowledge of the bank."
            ),
            recommendedArguments = listOf(
                "Rely on 'Special Equities' carved out in Standard Chartered Bank (2020) 13 SCC 574 and Gangotri Enterprises.",
                "Argue that encashment when 92% work is completed and project is already in operational trial run will lead to financial ruin and project stalling.",
                "Highlight that D-1 suppressed the Joint Site Report dated 28.02.2024 when demanding money from SBI."
            ),
            reliefSought = "Confirmation of interim injunction restraining invocation and encashment of BG/2022/984 pending final disposal of the commercial suit.",
            missingInformation = listOf(
                "Original copy of Independent Engineer's final commissioning sign-off certificate.",
                "Certified bank statement showing ₹1.5 Cr deposit in the Registry of High Court of Delhi."
            ),
            questionsForResearch = listOf(
                "Has Delhi High Court granted permanent injunction on bank guarantee where substantive work was over 90%? Check recent 2023-2024 precedents.",
                "Interpretation of conditional vs unconditional text in SBI standard format BG."
            )
        )
    }

    fun getSampleCourtroomPrep(caseId: String): CourtroomPrepKit = when (caseId) {
        "case_2" -> CourtroomPrepKit(
            caseId = caseId,
            onePageBrief = """BAIL APPLN. 1824/2024 — COURT NO. 31 (HIGH COURT OF DELHI)
BEFORE: HON'BLE MS. JUSTICE NEENA BANSAL KRISHNA
MATTER: ARGUMENTS ON REGULAR BAIL (S. 439 CrPC / 483 BNSS) — FIR 312/2023

KEY PROPOSITIONS:
1. Investigation Complete: Charge-sheet filed on 28.08.2024 without custodial arrest.
2. Parity: Main co-accused granted bail by Supreme Court on 15.09.2024.
3. Pre-trial Detention: 172 days undergone. 48 witnesses listed; trial will take considerable time.
4. Authorities: Satender Kumar Antil (2022) 10 SCC 51; Manish Sisodia (2024 INSC 595).""",
            oralArgumentOutline = listOf(
                "1. State custody duration: 172 days in custody since 14.05.2024.",
                "2. Highlight chargesheet status: Filed on 28.08.2024 without arrest during investigation.",
                "3. Present Parity: Tender Supreme Court order dated 15.09.2024 granting bail to co-accused.",
                "4. Address State objections: Undertake to surrender passport and attend all trial dates.",
                "5. Prayer: Enlarge on regular bail on terms deemed fit."
            ),
            benchQuestionsPredicted = listOf(
                "Was any recovery of funds or incriminating material made from the applicant?" to "No, My Lord. The status report of the IO expressly confirms no recovery was made from the Applicant.",
                "What is the role of the applicant compared to the co-accused who was granted bail?" to "My Lord, the co-accused was the primary signing Director, whereas the Applicant was a salaried financial manager acting on instructions. Parity applies squarely."
            ),
            opposingCounselPoints = listOf(
                "APP will argue serious economic offences should not be viewed leniently.",
                "APP will raise apprehension of witness tampering."
            ),
            rejoinderPoints = listOf(
                "All witnesses are government officials and bank managers; no tampering possible.",
                "Economic offence is not an absolute bar to bail (Sanjay Chandra (2012))."
            ),
            documentsToCarry = listOf(
                "1. Certified copy of Supreme Court co-accused bail order dated 15.09.2024",
                "2. Chargesheet excerpt (Page 14 showing Applicant not arrested during investigation)",
                "3. Jail Nominal Roll Certificate showing unblemished conduct",
                "4. Passport copy and local surety verification documents"
            ),
            hearingDayChecklist = listOf(
                "Verify item number 18 on Delhi HC display board" to true,
                "IO and APP present in Court No. 31" to true,
                "Applicant's brother present with surety papers" to true,
                "Convenience copies of Satender Antil handed over to Court Master" to true
            )
        )
        "case_3" -> CourtroomPrepKit(
            caseId = caseId,
            onePageBrief = """W.P.(C) 9821/2024 — COURT NO. 1 (DELHI HIGH COURT)
BEFORE: HON'BLE THE CHIEF JUSTICE & HON'BLE MR. JUSTICE MANMEET P. S. ARORA
MATTER: PUBLIC INTEREST LITIGATION (ART. 226) — YAMUNA BASIN INDUSTRIAL DISCHARGE

KEY PROPOSITIONS:
1. Continuing Contempt: DPCC failed to install online effluent monitoring systems across 14 drains despite court order dated 14.11.2023.
2. Statutory Breach: Violations of Water (Prevention and Control of Pollution) Act, 1974 Section 24 and 25.
3. Relief Sought: Directions to constitute a court-monitored high-powered inspection committee and impose environmental compensation.
4. Authorities: Vellore Citizens' Welfare Forum (1996) 5 SCC 647 (Precautionary Principle); M.C. Mehta v. Union of India (2004) 12 SCC 118.""",
            oralArgumentOutline = listOf(
                "1. Refer to DPCC compliance affidavit: Concedes 6 out of 14 drains are operating without functioning CETPs.",
                "2. Place CPCB water sample report: Biological Oxygen Demand (BOD) exceeds permissible limit by 1800%.",
                "3. Urge immediate interim order: Restrain illegal industrial discharge into Najafgarh drain.",
                "4. Pray for appointment of independent Court Commissioner to inspect CETP at Mayapuri."
            ),
            benchQuestionsPredicted = listOf(
                "Why shouldn't this matter be relegated to the National Green Tribunal (NGT) under the NGT Act 2010?" to "My Lord, the Division Bench has retained supervisory PIL jurisdiction since 2017 to ensure multi-agency coordination between CPCB, DPCC, and Municipal Corporation.",
                "Has DPCC levied environmental compensation on defaulting units?" to "My Lord, only paper notices were issued; zero recovery has been effected as shown in Annexure P-8."
            ),
            opposingCounselPoints = listOf(
                "ASG will submit that government has sanctioned ₹2,400 Cr under Yamuna Action Plan III.",
                "DPCC will state that upgrades to 3 STPs are scheduled for completion by March 2027."
            ),
            rejoinderPoints = listOf(
                "Sanctions on paper cannot substitute for real-time compliance while untreated sewage flows unabated.",
                "Precautionary principle mandates immediate closure of non-compliant dye factories."
            ),
            documentsToCarry = listOf(
                "1. Compilation of CPCB monthly water quality test reports (Jan-Oct 2026)",
                "2. Photographs of drain discharge points with GPS coordinates and date stamps",
                "3. Copy of DPCC order dated 14.11.2023",
                "4. Vellore Citizens compendium of environmental precedents"
            ),
            hearingDayChecklist = listOf(
                "Item 3 on Chief Justice Division Bench list verified" to true,
                "Independent environmental expert present in visitors gallery" to true,
                "Convenience copies handed over to Court Master" to true,
                "Colored satellite map placed in Bench compilation" to true
            )
        )
        "case_4" -> CourtroomPrepKit(
            caseId = caseId,
            onePageBrief = """CC NO. 4521/2024 — ROOM 28 (TIS HAZARI COURTS, DELHI)
BEFORE: SH. ASHISH RASTOGI, LD. MM (NI ACT-02), CENTRAL DISTRICT
MATTER: CW-1 CROSS-EXAMINATION (SECTION 138 NEGOTIABLE INSTRUMENTS ACT)

KEY FACTS:
1. Cheque No. 492014 for ₹42,50,000/- drawn on HDFC Bank dishonoured for 'Funds Insufficient'.
2. Statutory Demand Notice dispatched on 24.03.2024, delivered on 28.03.2024. Complaint filed on 02.05.2024 (Within limitation).
3. Statutory Presumption: Sections 118 & 139 NI Act apply. Accused must establish probable defence.
4. Authorities: Bir Singh v. Mukesh Kumar (2019) 4 SCC 197; Kalamani Tex (2021) 5 SCC 283; Tedhi Singh (2022) 6 SCC 735.""",
            oralArgumentOutline = listOf(
                "1. Affirm execution of cheque: Signature on cheque is admitted by accused in plea recorded u/s 251 CrPC.",
                "2. Rely on Section 139 presumption: Burden is entirely on accused to rebut legally enforceable debt.",
                "3. Cross-examination defense anticipated: Accused claims cheque was given as 'security' for software contract.",
                "4. Counter-defense: Security cheque dishonour attracts Section 138 once debt crystallizes (Sampelly Satyanarayana Rao (2016) 10 SCC 458)."
            ),
            benchQuestionsPredicted = listOf(
                "Did complainant show the loan or transaction in income tax returns?" to "Yes, Your Honour. The audited balance sheet of Complainant for FY 2023-24 reflects the outstanding receivable of ₹42.50 Lakhs under Trade Debtors (Ex. CW-1/5).",
                "Was postal tracking report proved with Section 65B certificate?" to "Yes, Your Honour. The certified postal track result accompanied by Complainant's Section 65B affidavit is already exhibited as Ex. CW-1/4."
            ),
            opposingCounselPoints = listOf(
                "Defence counsel will attempt to ask roving questions on complainant's source of funds.",
                "Defence will allege notice was not delivered at the correct corporate office address."
            ),
            rejoinderPoints = listOf(
                "Source of funds cannot be questioned when transaction is supported by commercial invoices and GST filings.",
                "Notice sent to address registered on Ministry of Corporate Affairs (MCA) Master Data satisfies Section 27 General Clauses Act (C.C. Alavi Haji (2007) 6 SCC 555)."
            ),
            documentsToCarry = listOf(
                "1. Original Cheque No. 492014 and Bank Return Memo (kept in sealed court envelope)",
                "2. Original Postal Receipts and Certified Tracking Report",
                "3. Complainant's Audited Balance Sheet and Ledger Account",
                "4. Section 65B Certificate under Indian Evidence Act / Section 63 BSA"
            ),
            hearingDayChecklist = listOf(
                "Complainant (CW-1) present in witness box" to true,
                "Original cheque extracted from safe and verified" to true,
                "Case called at Item 6 in pre-lunch session" to true,
                "Counsel ready with cross-examination objections" to true
            )
        )
        else -> CourtroomPrepKit(
            caseId = caseId,
            onePageBrief = """CS(COMM) 412/2024 — COURT NO. 14 (DELHI HIGH COURT)
BEFORE: HON'BLE MR. JUSTICE SANJEEV NARULA
MATTER: ARGUMENTS ON I.A. 8412/2024 (O.39 R.1&2 CPC - BANK GUARANTEE INJUNCTION)

KEY SUMMARY:
Plaintiff seeks to restrain invocation of ₹14.8 Cr PBG. Ad-interim stay granted on 24.04.2024 is in operation. Plaintiff deposited ₹1.5 Cr in Court.
92.4% physical progress achieved. Invocation attempted without mandatory 30-day cure notice under Clause 18.

CORE PROPOSITIONS:
1. Special Equities & Irretrievable Injustice: Encashment will force Plaintiff into insolvency while project is generating power.
2. Suppression of Fact: Beneficiary concealed 92.4% Joint Measurement Report from Bank.
3. Authorities: Standard Chartered Bank (2020) 13 SCC 574 (Paras 19, 21); Hindustan Construction Co. (2020) 17 SCC 324.""",
            oralArgumentOutline = listOf(
                "1. Open with status of interim order: Stay operating since 24.04.2024. ₹1.5 Cr deposited in Court Registry.",
                "2. Direct Court to Clause 18: Invocation is conditioned upon a joint inspection and 30-day notice. Neither done.",
                "3. Show Joint Inspection Report (Ex. P-2, PDF Page 44): D-1's own chief engineer certified 92.4% completion.",
                "4. Distinguish unconditional bank guarantee doctrine using 'Special Equities' in Standard Chartered Bank (2020).",
                "5. Conclude with Prayer (a): Confirm injunction till arbitral tribunal is constituted."
            ),
            benchQuestionsPredicted = listOf(
                "Is the bank guarantee conditional or unconditional on its face?" to "My Lord, the BG itself incorporates Clause 18 of the main agreement by express reference in Para 2, making compliance with the cure protocol a condition precedent.",
                "Why shouldn't the parties agitate this before the Arbitral Tribunal under Section 9 or Section 17?" to "We have already triggered Section 21 notice on 14.05.2024. We only seek protection till the Section 17 application is taken up by the Tribunal, as permitted in ArcelorMittal Nippon Steel (2022) 1 SCC 712.",
                "Will your client keep the Bank Guarantee alive during arbitration?" to "Yes, My Lord. We give an unconditional undertaking to keep the Bank Guarantee renewed 30 days prior to expiry till final arbitral award."
            ),
            opposingCounselPoints = listOf(
                "Opposing counsel will rely on U.P. State Sugar Corporation (1997) that courts must not interfere with bank guarantees.",
                "They will claim Plaintiff failed to meet contractual commercial operation date (COD)."
            ),
            rejoinderPoints = listOf(
                "Rebut COD delay by pointing to D-1's own failure to provide grid connectivity bay until November 2023.",
                "Show that non-compliance with the express terms of the guarantee is an established exception."
            ),
            documentsToCarry = listOf(
                "1. Convenience Compilation (2 indexed copies for Bench and Court Master)",
                "2. Original certified Bank Guarantee BG/2022/984",
                "3. Registry Receipt of ₹1.5 Cr deposit dated 02.05.2024",
                "4. Authorities compendium containing Standard Chartered Bank (2020) and Satender Antil (2022)",
                "5. Stamped Vakalatnama with welfare stamp"
            ),
            hearingDayChecklist = listOf(
                "Verify cause list item number on Delhi High Court portal" to true,
                "Court Master sets delivered in Court No. 14" to true,
                "Client representative present in Courtroom" to true,
                "Audio/Hybrid VC link tested in case of remote link" to false,
                "Spare copies of convenience synopsis in file" to true
            )
        )
    }
}

class LitigationRepository {
    private val _cases = MutableStateFlow(DemoDataProvider.getSampleCases())
    val cases: StateFlow<List<CourtCase>> = _cases.asStateFlow()

    private val _hearings = MutableStateFlow(DemoDataProvider.getSampleHearings())
    val hearings: StateFlow<List<Hearing>> = _hearings.asStateFlow()

    private val _drafts = MutableStateFlow(DemoDataProvider.getSampleDrafts())
    val drafts: StateFlow<List<LegalDraft>> = _drafts.asStateFlow()

    private val _documents = MutableStateFlow(DemoDataProvider.getSampleDocuments())
    val documents: StateFlow<List<CaseDocument>> = _documents.asStateFlow()

    private val _authorities = MutableStateFlow(DemoDataProvider.getSampleAuthorities())
    val authorities: StateFlow<List<LegalAuthority>> = _authorities.asStateFlow()

    private val _clients = MutableStateFlow(DemoDataProvider.getSampleClients())
    val clients: StateFlow<List<Client>> = _clients.asStateFlow()

    private val _invoices = MutableStateFlow(DemoDataProvider.getSampleInvoices())
    val invoices: StateFlow<List<Invoice>> = _invoices.asStateFlow()

    private val _limitationItems = MutableStateFlow(DemoDataProvider.getSampleLimitationItems())
    val limitationItems: StateFlow<List<LimitationItem>> = _limitationItems.asStateFlow()

    private val _team = MutableStateFlow(DemoDataProvider.getSampleTeam())
    val team: StateFlow<List<TeamMember>> = _team.asStateFlow()

    private val _selectedCaseId = MutableStateFlow("case_1")
    val selectedCaseId: StateFlow<String> = _selectedCaseId.asStateFlow()

    private val _demoModeActive = MutableStateFlow(true)
    val demoModeActive: StateFlow<Boolean> = _demoModeActive.asStateFlow()

    private val _currentPlan = MutableStateFlow(SubscriptionPlan.PROFESSIONAL)
    val currentPlan: StateFlow<SubscriptionPlan> = _currentPlan.asStateFlow()

    fun selectCase(caseId: String) {
        _selectedCaseId.value = caseId
    }

    fun setPlan(plan: SubscriptionPlan) {
        _currentPlan.value = plan
    }

    fun toggleDemoMode() {
        val next = !_demoModeActive.value
        _demoModeActive.value = next
        if (!next) {
            // Keep user created or clear demo
            _cases.value = _cases.value.filter { !it.isDemo }
        } else {
            _cases.value = DemoDataProvider.getSampleCases()
            _hearings.value = DemoDataProvider.getSampleHearings()
            _drafts.value = DemoDataProvider.getSampleDrafts()
            _documents.value = DemoDataProvider.getSampleDocuments()
            _authorities.value = DemoDataProvider.getSampleAuthorities()
            _clients.value = DemoDataProvider.getSampleClients()
            _invoices.value = DemoDataProvider.getSampleInvoices()
            _limitationItems.value = DemoDataProvider.getSampleLimitationItems()
            _team.value = DemoDataProvider.getSampleTeam()
        }
    }

    fun addCase(newCase: CourtCase) {
        _cases.value = listOf(newCase) + _cases.value
        _selectedCaseId.value = newCase.id
    }

    fun updateCase(updated: CourtCase) {
        _cases.value = _cases.value.map { if (it.id == updated.id) updated else it }
    }

    fun deleteCase(caseId: String) {
        _cases.value = _cases.value.filter { it.id != caseId }
        if (_selectedCaseId.value == caseId && _cases.value.isNotEmpty()) {
            _selectedCaseId.value = _cases.value.first().id
        }
    }

    fun addHearing(hearing: Hearing) {
        _hearings.value = listOf(hearing) + _hearings.value
        _cases.value = _cases.value.map { c ->
            if (c.id == hearing.caseId) {
                c.copy(nextHearingDate = hearing.date, nextHearingPurpose = hearing.purpose)
            } else c
        }
    }

    fun updateHearing(updated: Hearing) {
        _hearings.value = _hearings.value.map { if (it.id == updated.id) updated else it }
        _cases.value = _cases.value.map { c ->
            if (c.id == updated.caseId && !updated.isCompleted) {
                c.copy(nextHearingDate = updated.date, nextHearingPurpose = updated.purpose)
            } else c
        }
    }

    fun addDraft(draft: LegalDraft) {
        _drafts.value = listOf(draft) + _drafts.value
    }

    fun updateDraft(updated: LegalDraft) {
        _drafts.value = _drafts.value.map { if (it.id == updated.id) updated else it }
    }

    fun addDocument(doc: CaseDocument) {
        _documents.value = listOf(doc) + _documents.value
    }

    fun addAuthority(auth: LegalAuthority) {
        _authorities.value = listOf(auth) + _authorities.value
    }

    fun addClient(client: Client) {
        _clients.value = listOf(client) + _clients.value
    }

    fun addInvoice(invoice: Invoice) {
        _invoices.value = listOf(invoice) + _invoices.value
        val balance = invoice.totalAmount - invoice.paidAmount
        if (balance > 0) {
            _clients.value = _clients.value.map { client ->
                if (client.id == invoice.clientId || client.name.contains(invoice.clientName, ignoreCase = true)) {
                    client.copy(outstandingFees = client.outstandingFees + balance)
                } else client
            }
        }
    }

    fun updateInvoice(updated: Invoice) {
        val old = _invoices.value.find { it.id == updated.id }
        _invoices.value = _invoices.value.map { if (it.id == updated.id) updated else it }
        if (old != null) {
            val oldBalance = old.totalAmount - old.paidAmount
            val newBalance = updated.totalAmount - updated.paidAmount
            val diff = newBalance - oldBalance
            if (diff != 0.0) {
                _clients.value = _clients.value.map { client ->
                    if (client.id == updated.clientId || client.name.contains(updated.clientName, ignoreCase = true)) {
                        client.copy(outstandingFees = (client.outstandingFees + diff).coerceAtLeast(0.0))
                    } else client
                }
            }
        }
    }

    fun updateClient(updated: Client) {
        _clients.value = _clients.value.map { if (it.id == updated.id) updated else it }
    }

    fun assignCaseToMember(memberId: String, caseId: String) {
        _team.value = _team.value.map { member ->
            if (member.id == memberId) {
                member.copy(activeCasesCount = member.activeCasesCount + 1)
            } else member
        }
    }

    fun updateCaseLimitation(caseId: String, limitationDate: String) {
        _cases.value = _cases.value.map {
            if (it.id == caseId) it.copy(limitationDate = limitationDate) else it
        }
    }

    fun addLimitationItem(item: LimitationItem) {
        _limitationItems.value = listOf(item) + _limitationItems.value
    }

    private val _intelligenceAnalyses = mutableMapOf<String, CaseIntelligenceAnalysis>()
    private val _courtroomPrepKits = mutableMapOf<String, CourtroomPrepKit>()

    fun getIntelligenceAnalysis(caseId: String): CaseIntelligenceAnalysis {
        return _intelligenceAnalyses.getOrPut(caseId) {
            DemoDataProvider.getSampleIntelligenceAnalysis(caseId)
        }
    }

    fun updateIntelligenceAnalysis(caseId: String, updated: CaseIntelligenceAnalysis) {
        _intelligenceAnalyses[caseId] = updated
    }

    fun updateIntelligenceAnalysis(updated: CaseIntelligenceAnalysis) {
        _intelligenceAnalyses[updated.caseId] = updated
    }

    fun getCourtroomPrepKit(caseId: String): CourtroomPrepKit {
        return _courtroomPrepKits.getOrPut(caseId) {
            DemoDataProvider.getSampleCourtroomPrep(caseId)
        }
    }

    fun updateCourtroomPrepKit(caseId: String, updated: CourtroomPrepKit) {
        _courtroomPrepKits[caseId] = updated
    }

    fun updateCourtroomPrepKit(updated: CourtroomPrepKit) {
        _courtroomPrepKits[updated.caseId] = updated
    }
}
