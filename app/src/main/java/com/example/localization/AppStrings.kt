package com.example.localization

enum class AppLanguage(val code: String, val displayName: String) {
    EN("en", "English"),
    HI("hi", "हिन्दी")
}

object AppStrings {
    fun get(key: String, lang: AppLanguage): String {
        val entry = strings[key] ?: return key
        return if (lang == AppLanguage.HI) entry.hi else entry.en
    }

    private data class Loc(val en: String, val hi: String)

    private val strings = mapOf(
        // Brand & Header
        "brand_name" to Loc("LEXOVANT", "लेक्सोवेंट"),
        "product_name" to Loc("LitigationOS", "लिटिगेशन ओएस"),
        "tagline" to Loc("Your Complete Litigation Practice Workspace", "आपका संपूर्ण वाद-विवाद अभ्यास कार्यक्षेत्र"),
        "jurisdiction_badge" to Loc("Jurisdiction: India", "अधिकार क्षेत्र: भारत"),
        "demo_mode" to Loc("Demo Mode", "डेमो मोड"),
        "demo_mode_desc" to Loc("Sample Indian litigation data active", "नमूना भारतीय वाद डेटा सक्रिय है"),
        "global_search_hint" to Loc("Search cases, CNR, party, sections...", "मुकदमे, सीएनआर, पक्षकार, धाराएं खोजें..."),

        // Navigation Modules
        "nav_dashboard" to Loc("Dashboard", "डैशबोर्ड"),
        "nav_cases" to Loc("Case Management", "मुकदमा प्रबंधन"),
        "nav_intelligence" to Loc("Case Intelligence", "केस इंटेलिजेंस"),
        "nav_ai_assistant" to Loc("AI Legal Assistant", "एआई विधिक सहायक"),
        "nav_drafting" to Loc("Drafting Studio", "ड्राफ्टिंग स्टूडियो"),
        "nav_research" to Loc("Legal Research", "विधिक शोध"),
        "nav_diary" to Loc("Court Diary", "कोर्ट डायरी"),
        "nav_documents" to Loc("Document Room", "दस्तावेज़ कक्ष"),
        "nav_clients" to Loc("Clients", "मुवक्किल (क्लाइंट्स)"),
        "nav_limitation" to Loc("Limitation Calculator", "सीमितता कैलकुलेटर"),
        "nav_court_prep" to Loc("Courtroom Prep", "न्यायालय तैयारी"),
        "nav_pdf_tools" to Loc("PDF & Tools", "पीडीएफ व टूल्स"),
        "nav_billing" to Loc("Fees & Billing", "फीस व बिलिंग"),
        "nav_team" to Loc("Law Firm Team", "फर्म टीम"),
        "nav_reports" to Loc("Reports & Analytics", "रिपोर्ट्स"),
        "nav_subscription" to Loc("Subscription & Plans", "सदस्यता योजनाएं"),

        // Common Actions
        "action_add" to Loc("Add", "जोड़ें"),
        "action_create" to Loc("Create", "बनाएं"),
        "action_save" to Loc("Save", "सुरक्षित करें"),
        "action_edit" to Loc("Edit", "संपादित करें"),
        "action_delete" to Loc("Delete", "हटाएं"),
        "action_archive" to Loc("Archive", "संग्रहीत करें"),
        "action_cancel" to Loc("Cancel", "रद्द करें"),
        "action_close" to Loc("Close", "बंद करें"),
        "action_copy" to Loc("Copy", "कॉपी करें"),
        "action_share" to Loc("Share", "साझा करें"),
        "action_export" to Loc("Export", "निर्यात करें"),
        "action_search" to Loc("Search", "खोजें"),
        "action_filter" to Loc("Filter", "फ़िल्टर"),
        "action_view" to Loc("View", "देखें"),
        "action_download" to Loc("Download", "डाउनलोड करें"),
        "action_run_ai" to Loc("Run AI Analysis", "एआई विश्लेषण चलाएं"),
        "action_switch_lang" to Loc("Language", "भाषा"),

        // Dashboard Metrics
        "stat_active_cases" to Loc("Active Cases", "सक्रिय मुकदमे"),
        "stat_pending_hearings" to Loc("Pending Hearings", "लंबित सुनवाई"),
        "stat_today_hearings" to Loc("Today's Hearings", "आज की सुनवाई"),
        "stat_upcoming_hearings" to Loc("Upcoming (7 Days)", "आगामी (7 दिन)"),
        "stat_filing_deadlines" to Loc("Filing Deadlines", "दाखिल करने की अंतिम तिथि"),
        "stat_pending_drafts" to Loc("Pending Drafts", "लंबित मसौदे"),
        "stat_outstanding_fees" to Loc("Outstanding Fees", "बकाया फीस"),
        "stat_weekly_workload" to Loc("Weekly Workload", "साप्ताहिक कार्यभार"),

        // Quick Actions
        "quick_add_case" to Loc("Quick Add Case", "शीघ्र केस जोड़ें"),
        "quick_create_draft" to Loc("Create Draft", "मसौदा तैयार करें"),
        "quick_add_hearing" to Loc("Add Hearing", "सुनवाई जोड़ें"),
        "quick_upload_doc" to Loc("Upload Document", "दस्तावेज़ अपलोड करें"),
        "quick_open_ai" to Loc("Open AI Assistant", "एआई सहायक खोलें"),

        // Case Fields
        "field_case_title" to Loc("Case Title", "मुकदमे का शीर्षक"),
        "field_case_number" to Loc("Case Number", "केस संख्या"),
        "field_cnr_number" to Loc("CNR Number (16 digits)", "सीएनआर संख्या (16 अंक)"),
        "field_court_name" to Loc("Court Name", "न्यायालय का नाम"),
        "field_court_establishment" to Loc("Court Establishment", "अदालत परिसर/शाखा"),
        "field_category" to Loc("Case Category", "मुकदमे की श्रेणी"),
        "field_case_type" to Loc("Case Type", "केस का प्रकार"),
        "field_filing_number" to Loc("Filing Number", "दाखिल संख्या"),
        "field_filing_date" to Loc("Filing Date", "दाखिल तिथि"),
        "field_registration_date" to Loc("Registration Date", "पंजीकरण तिथि"),
        "field_petitioner" to Loc("Petitioner / Applicant", "याचिकाकर्ता / प्रार्थी"),
        "field_respondent" to Loc("Respondent / Opposite Party", "प्रतिवादी / विपक्षी"),
        "field_advocate_name" to Loc("Advocate on Record / Counsel", "अधिवक्ता / काउंसिल"),
        "field_opposing_counsel" to Loc("Opposing Counsel", "विपक्षी वकील"),
        "field_judge" to Loc("Judge / Courtroom", "न्यायाधीश / कोर्ट रूम"),
        "field_stage" to Loc("Current Stage", "वर्तमान चरण"),
        "field_status" to Loc("Status", "स्थिति"),
        "field_next_hearing" to Loc("Next Hearing Date", "अगली सुनवाई तिथि"),
        "field_notes" to Loc("Notes / Remarks", "टिप्पणी / विवरण"),

        // Legal Safeguards & Notice
        "ai_safeguard_notice" to Loc(
            "Notice: AI-generated drafts & analyses require independent advocate verification before filing. Lexovant LitigationOS does not replace professional legal counsel.",
            "सूचना: एआई-जनित मसौदों और विश्लेषणों को कोर्ट में दाखिल करने से पहले स्वतंत्र अधिवक्ता द्वारा सत्यापन आवश्यक है। लेक्सोवेंट पेशेवर कानूनी परामर्श का विकल्प नहीं है।"
        ),
        "official_sources_notice" to Loc(
            "Legal research draws on authentic Supreme Court of India, High Courts & India Code statutes. Live court API requires court registry credentials.",
            "विधिक शोध भारत के सर्वोच्च न्यायालय, उच्च न्यायालयों और इंडिया कोड अधिनियमों पर आधारित है। लाइव कोर्ट एपीआई हेतु आधिकारिक क्रेडेंशियल आवश्यक हैं।"
        )
    )
}
