package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.localization.AppStrings
import com.example.ui.components.AppNavigationDrawer
import com.example.ui.components.AppTopBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.LitigationViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                LitigationApp()
            }
        }
    }
}

@Composable
fun LitigationApp(viewModel: LitigationViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val language by viewModel.language.collectAsState()
    val cases by viewModel.repository.cases.collectAsState()
    val hearings by viewModel.repository.hearings.collectAsState()
    val drafts by viewModel.repository.drafts.collectAsState()
    val documents by viewModel.repository.documents.collectAsState()
    val authorities by viewModel.repository.authorities.collectAsState()
    val clients by viewModel.repository.clients.collectAsState()
    val invoices by viewModel.repository.invoices.collectAsState()
    val limitations by viewModel.repository.limitationItems.collectAsState()
    val team by viewModel.repository.team.collectAsState()
    val selectedCase by viewModel.selectedCase.collectAsState()
    val demoModeActive by viewModel.repository.demoModeActive.collectAsState()
    val currentPlan by viewModel.repository.currentPlan.collectAsState()
    val dashboardMetrics by viewModel.dashboardMetrics.collectAsState()

    // AI Assistant state
    val aiWorkflow by viewModel.selectedAiWorkflow.collectAsState()
    val aiPrompt by viewModel.aiInputPrompt.collectAsState()
    val aiResult by viewModel.aiOutputResult.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Back handling: If not on dashboard, return to dashboard
    BackHandler(enabled = currentScreen != AppScreen.DASHBOARD) {
        viewModel.navigateTo(AppScreen.DASHBOARD)
    }

    val screenTitle = when (currentScreen) {
        AppScreen.DASHBOARD -> AppStrings.get("nav_dashboard", language)
        AppScreen.CASES -> AppStrings.get("nav_cases", language)
        AppScreen.CASE_INTELLIGENCE -> AppStrings.get("nav_intelligence", language)
        AppScreen.AI_ASSISTANT -> AppStrings.get("nav_ai_assistant", language)
        AppScreen.DRAFTING_STUDIO -> AppStrings.get("nav_drafting", language)
        AppScreen.LEGAL_RESEARCH -> AppStrings.get("nav_research", language)
        AppScreen.COURT_DIARY -> AppStrings.get("nav_diary", language)
        AppScreen.DOCUMENT_ROOM -> AppStrings.get("nav_documents", language)
        AppScreen.CLIENT_MANAGEMENT -> AppStrings.get("nav_clients", language)
        AppScreen.LIMITATION_CALCULATOR -> AppStrings.get("nav_limitation", language)
        AppScreen.COURTROOM_PREP -> AppStrings.get("nav_court_prep", language)
        AppScreen.PDF_WORKSPACE -> AppStrings.get("nav_pdf_tools", language)
        AppScreen.FEES_BILLING -> AppStrings.get("nav_billing", language)
        AppScreen.TEAM_WORKSPACE -> AppStrings.get("nav_team", language)
        AppScreen.REPORTS -> AppStrings.get("nav_reports", language)
        AppScreen.SUBSCRIPTION -> AppStrings.get("nav_subscription", language)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppNavigationDrawer(
                currentScreen = currentScreen,
                language = language,
                onScreenSelected = { screen ->
                    viewModel.navigateTo(screen)
                },
                onCloseDrawer = {
                    coroutineScope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    currentTitle = screenTitle,
                    language = language,
                    demoModeActive = demoModeActive,
                    onMenuClick = {
                        coroutineScope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    },
                    onLanguageToggle = { viewModel.toggleLanguage() },
                    onDemoToggle = { viewModel.repository.toggleDemoMode() },
                    onSearchClick = { viewModel.navigateTo(AppScreen.CASES) }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            val contentModifier = Modifier.padding(innerPadding)

            when (currentScreen) {
                AppScreen.DASHBOARD -> {
                    DashboardScreen(
                        metrics = dashboardMetrics,
                        cases = cases,
                        hearings = hearings,
                        language = language,
                        onNavigate = { viewModel.navigateTo(it) },
                        onSelectCase = { viewModel.selectCase(it) },
                        modifier = contentModifier
                    )
                }
                AppScreen.CASES -> {
                    CaseManagementScreen(
                        cases = cases,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        onOpenCaseIntelligence = {
                            viewModel.selectCase(it)
                            viewModel.navigateTo(AppScreen.CASE_INTELLIGENCE)
                        },
                        onAddCase = { viewModel.repository.addCase(it) },
                        onUpdateCase = { viewModel.repository.updateCase(it) },
                        onDeleteCase = { viewModel.repository.deleteCase(it) },
                        modifier = contentModifier
                    )
                }
                AppScreen.CASE_INTELLIGENCE -> {
                    val activeCaseId = selectedCase?.id ?: "case_1"
                    val currentAnalysis = viewModel.repository.getIntelligenceAnalysis(activeCaseId)
                    CaseIntelligenceScreen(
                        courtCase = selectedCase,
                        cases = cases,
                        documents = documents.filter { it.caseId == activeCaseId },
                        authorities = authorities.filter { it.caseId == activeCaseId },
                        analysisData = currentAnalysis,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        onUpdateAnalysis = { updated ->
                            viewModel.repository.updateIntelligenceAnalysis(activeCaseId, updated)
                        },
                        onNavigate = { viewModel.navigateTo(it) },
                        modifier = contentModifier
                    )
                }
                AppScreen.AI_ASSISTANT -> {
                    AiAssistantScreen(
                        workflows = viewModel.aiWorkflows,
                        selectedWorkflow = aiWorkflow,
                        promptText = aiPrompt,
                        resultText = aiResult,
                        isLoading = isAiLoading,
                        cases = cases,
                        selectedCase = selectedCase,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        onSelectWorkflow = { viewModel.selectAiWorkflow(it) },
                        onPromptChange = { viewModel.setAiPrompt(it) },
                        onRunAnalysis = { viewModel.runAiAnalysis() },
                        onSaveAuthority = { viewModel.repository.addAuthority(it) },
                        onOpenDraftingStudio = { viewModel.navigateTo(AppScreen.DRAFTING_STUDIO) },
                        modifier = contentModifier
                    )
                }
                AppScreen.DRAFTING_STUDIO -> {
                    DraftingStudioScreen(
                        drafts = drafts,
                        cases = cases,
                        selectedCaseId = selectedCase?.id,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        onSaveDraft = { viewModel.repository.addDraft(it) },
                        modifier = contentModifier
                    )
                }
                AppScreen.LEGAL_RESEARCH -> {
                    LegalResearchScreen(
                        authorities = authorities,
                        cases = cases,
                        selectedCaseId = selectedCase?.id,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        onAddAuthority = { viewModel.repository.addAuthority(it) },
                        modifier = contentModifier
                    )
                }
                AppScreen.COURT_DIARY -> {
                    CourtDiaryScreen(
                        hearings = hearings,
                        cases = cases,
                        selectedCaseId = selectedCase?.id,
                        language = language,
                        onAddHearing = { viewModel.repository.addHearing(it) },
                        onUpdateHearing = { viewModel.repository.updateHearing(it) },
                        onSelectCase = { viewModel.selectCase(it) },
                        modifier = contentModifier
                    )
                }
                AppScreen.DOCUMENT_ROOM -> {
                    DocumentRoomScreen(
                        documents = documents,
                        cases = cases,
                        selectedCaseId = selectedCase?.id,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        onAddDocument = { viewModel.repository.addDocument(it) },
                        modifier = contentModifier
                    )
                }
                AppScreen.CLIENT_MANAGEMENT -> {
                    ClientManagementScreen(
                        clients = clients,
                        cases = cases,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        onAddClient = { viewModel.repository.addClient(it) },
                        modifier = contentModifier
                    )
                }
                AppScreen.LIMITATION_CALCULATOR -> {
                    LimitationCalculatorScreen(
                        items = limitations,
                        cases = cases,
                        selectedCaseId = selectedCase?.id,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        onAddCalculation = { viewModel.repository.addLimitationItem(it) },
                        onUpdateCaseLimitation = { caseId, date ->
                            viewModel.repository.updateCaseLimitation(caseId, date)
                        },
                        modifier = contentModifier
                    )
                }
                AppScreen.COURTROOM_PREP -> {
                    CourtroomPrepScreen(
                        courtCase = selectedCase,
                        cases = cases,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        onUpdateKit = { updated ->
                            selectedCase?.let {
                                viewModel.repository.updateCourtroomPrepKit(it.id, updated)
                            }
                        },
                        modifier = contentModifier
                    )
                }
                AppScreen.PDF_WORKSPACE -> {
                    PdfWorkspaceScreen(
                        cases = cases,
                        documents = documents,
                        selectedCaseId = selectedCase?.id,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        modifier = contentModifier
                    )
                }
                AppScreen.FEES_BILLING -> {
                    BillingScreen(
                        invoices = invoices,
                        cases = cases,
                        clients = clients,
                        selectedCaseId = selectedCase?.id,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        onAddInvoice = { viewModel.repository.addInvoice(it) },
                        onUpdateInvoice = { viewModel.repository.updateInvoice(it) },
                        modifier = contentModifier
                    )
                }
                AppScreen.TEAM_WORKSPACE -> {
                    TeamScreen(
                        team = team,
                        cases = cases,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        onAssignCase = { memberId, caseId ->
                            viewModel.repository.assignCaseToMember(memberId, caseId)
                        },
                        modifier = contentModifier
                    )
                }
                AppScreen.REPORTS -> {
                    ReportsScreen(
                        cases = cases,
                        hearings = hearings,
                        invoices = invoices,
                        clients = clients,
                        language = language,
                        onSelectCase = { viewModel.selectCase(it) },
                        modifier = contentModifier
                    )
                }
                AppScreen.SUBSCRIPTION -> {
                    SubscriptionScreen(
                        currentPlan = currentPlan,
                        language = language,
                        onSelectPlan = { viewModel.repository.setPlan(it) },
                        modifier = contentModifier
                    )
                }
            }
        }
    }
}
