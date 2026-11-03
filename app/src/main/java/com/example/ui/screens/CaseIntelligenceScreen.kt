package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseIntelligenceScreen(
    courtCase: CourtCase?,
    cases: List<CourtCase>,
    documents: List<CaseDocument>,
    authorities: List<LegalAuthority>,
    analysisData: CaseIntelligenceAnalysis,
    language: AppLanguage,
    onSelectCase: (String) -> Unit,
    onUpdateAnalysis: (CaseIntelligenceAnalysis) -> Unit,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    if (courtCase == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No case selected. Please select a case from Case Management.")
        }
        return
    }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val tabs = listOf(
        "Overview", "Facts", "Chronology", "Issues", "Applicable Laws",
        "Judgments", "Evidence", "Arguments", "Counterarguments", "Orders",
        "Documents", "Hearing Prep"
    )

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showFullReportDialog by remember { mutableStateOf(false) }
    var showEditAnalysisDialog by remember { mutableStateOf(false) }
    var showCasePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
    ) {
        // Case Header Summary Bar with Case Switcher
        Surface(
            color = DeepNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = GoldContainer
                    ) {
                        Text(
                            text = "CASE INTELLIGENCE WORKSPACE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Interactive Case Switcher Chip
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = NavyLight,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { showCasePicker = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "CNR: ${courtCase.cnrNumber}",
                                fontSize = 11.sp,
                                color = IvoryBackground,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Switch Case",
                                tint = MutedGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = courtCase.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "${courtCase.caseNumber} • ${courtCase.courtName} • Stage: ${courtCase.currentStage.label}",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Missing Information Warning Strip
                if (analysisData.missingInformation.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LegalAmberContainer.copy(alpha = 0.9f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = LegalAmber, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Missing Information Warning: ${analysisData.missingInformation.first()}",
                                fontSize = 10.sp,
                                color = DeepNavy,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showFullReportDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MutedGold),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_view_ai_report")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = DeepNavy,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "14-Point AI Report",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    }

                    OutlinedButton(
                        onClick = { showEditAnalysisDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit Analysis", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = { onNavigate(AppScreen.DRAFTING_STUDIO) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Drafting", fontSize = 11.sp)
                    }
                }
            }
        }

        // 12 Tabs Scrollable Row
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = IvorySurface,
            contentColor = DeepNavy,
            edgePadding = 12.dp,
            modifier = Modifier.fillMaxWidth().testTag("case_intelligence_tabs")
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        // Active Tab Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            when (selectedTabIndex) {
                0 -> TabOverview(courtCase, analysisData)
                1 -> TabFacts(analysisData)
                2 -> TabChronology(analysisData)
                3 -> TabIssues(analysisData)
                4 -> TabApplicableLaws(analysisData)
                5 -> TabJudgments(authorities)
                6 -> TabEvidence(documents)
                7 -> TabArguments(analysisData)
                8 -> TabCounterarguments(analysisData)
                9 -> TabOrders(courtCase)
                10 -> TabDocuments(documents, onNavigate)
                11 -> TabHearingPreparation(courtCase, onNavigate)
            }
        }
    }

    // Case Selector Modal
    if (showCasePicker) {
        AlertDialog(
            onDismissRequest = { showCasePicker = false },
            title = { Text("Select Active Case", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    cases.forEach { c ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (c.id == courtCase.id) GoldContainer else IvorySurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectCase(c.id)
                                    showCasePicker = false
                                }
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(c.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                Text("${c.caseNumber} • ${c.courtName}", fontSize = 10.sp, color = SlateGrey)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                OutlinedButton(onClick = { showCasePicker = false }) { Text("Close") }
            }
        )
    }

    // Edit Analysis Modal
    if (showEditAnalysisDialog) {
        var summary by remember { mutableStateOf(analysisData.executiveSummary) }
        var relief by remember { mutableStateOf(analysisData.reliefSought) }
        var petitionerCase by remember { mutableStateOf(analysisData.petitionerCase) }
        var respondentDefence by remember { mutableStateOf(analysisData.respondentDefence) }

        val scroll = rememberScrollState()

        AlertDialog(
            onDismissRequest = { showEditAnalysisDialog = false },
            title = { Text("Edit Case Intelligence Analysis", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                        .verticalScroll(scroll),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = summary,
                        onValueChange = { summary = it },
                        label = { Text("Executive Summary") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = relief,
                        onValueChange = { relief = it },
                        label = { Text("Relief Sought") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = petitionerCase,
                        onValueChange = { petitionerCase = it },
                        label = { Text("Petitioner's Case / Grounds") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = respondentDefence,
                        onValueChange = { respondentDefence = it },
                        label = { Text("Respondent's Likely Defence") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = analysisData.copy(
                            executiveSummary = summary,
                            reliefSought = relief,
                            petitionerCase = petitionerCase,
                            respondentDefence = respondentDefence
                        )
                        onUpdateAnalysis(updated)
                        showEditAnalysisDialog = false
                        Toast.makeText(context, "Case Intelligence updated", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEditAnalysisDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Full 14-Point Analysis Modal with Source Verification & Missing Info Warnings
    if (showFullReportDialog) {
        AlertDialog(
            onDismissRequest = { showFullReportDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = MutedGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "14-Point AI Case Analysis Report",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                val scroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                        .verticalScroll(scroll),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Source categorization legend
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = GoldContainer
                    ) {
                        Text(
                            text = "LEGAL SOURCE AUDIT: [USER] User-supplied facts • [DOC] Pleadings extraction • [STATUTE] Primary Law • [AI] Strategic Inference",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldDark,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    if (analysisData.missingInformation.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = LegalRedContainer.copy(alpha = 0.5f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("⚠️ MISSING INFORMATION WARNINGS:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = LegalRed)
                                analysisData.missingInformation.forEach { Text("• $it", fontSize = 10.sp, color = DeepNavy) }
                            }
                        }
                    }

                    ReportSectionItem("1. Executive Summary [AI]", analysisData.executiveSummary)
                    ReportSectionItem("2. Material Facts [USER + DOC]", analysisData.materialFacts.joinToString("\n• "))
                    ReportSectionItem("3. Procedural History [DOC]", analysisData.proceduralHistory)
                    ReportSectionItem("4. Key Chronology [DOC]", analysisData.chronology.joinToString("\n") { "${it.first}: ${it.second}" })
                    ReportSectionItem("5. Disputed vs Undisputed Facts [DOC]", "Undisputed:\n• " + analysisData.undisputedFacts.joinToString("\n• ") + "\n\nDisputed:\n• " + analysisData.disputedFacts.joinToString("\n• "))
                    ReportSectionItem("6. Issues for Determination [AI]", analysisData.issuesForDetermination.joinToString("\n• "))
                    ReportSectionItem("7. Applicable Provisions [STATUTE]", analysisData.applicableProvisions.joinToString("\n• "))
                    ReportSectionItem("8. Petitioner's Case [USER]", analysisData.petitionerCase)
                    ReportSectionItem("9. Respondent's Defence [DOC]", analysisData.respondentDefence)
                    ReportSectionItem("10. Potential Weaknesses [AI]", analysisData.potentialWeaknesses.joinToString("\n• "))
                    ReportSectionItem("11. Recommended Arguments [AI]", analysisData.recommendedArguments.joinToString("\n• "))
                    ReportSectionItem("12. Relief Sought [USER]", analysisData.reliefSought)
                    ReportSectionItem("13. Missing Information [AUDIT]", analysisData.missingInformation.joinToString("\n• "))
                    ReportSectionItem("14. Questions for Research [AI]", analysisData.questionsForResearch.joinToString("\n• "))
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = {
                            val reportText = """LEXOVANT LITIGATIONOS — CASE INTELLIGENCE REPORT
Case: ${courtCase.title} (${courtCase.caseNumber})
CNR: ${courtCase.cnrNumber} | Court: ${courtCase.courtName}

1. EXECUTIVE SUMMARY [AI]:
${analysisData.executiveSummary}

2. MATERIAL FACTS [USER + DOC]:
${analysisData.materialFacts.joinToString("\n")}

3. RECOMMENDED ARGUMENTS [AI]:
${analysisData.recommendedArguments.joinToString("\n")}

4. APPLICABLE PROVISIONS [STATUTE]:
${analysisData.applicableProvisions.joinToString("\n")}

5. MISSING INFORMATION WARNINGS:
${analysisData.missingInformation.joinToString("\n")}

(Verified by Advocate on Record - Generated in Lexovant Practice Workspace)"""
                            clipboardManager.setText(AnnotatedString(reportText))
                            Toast.makeText(context, "Full 14-Point Report copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Export / Copy")
                    }
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showFullReportDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun ReportSectionItem(heading: String, body: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = IvorySurface),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = heading,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = body,
                fontSize = 11.sp,
                color = SlateGrey,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun TabOverview(courtCase: CourtCase, analysis: CaseIntelligenceAnalysis) {
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IntelligenceCard(
            title = "Executive Summary [AI Inference]",
            badge = "Verified Summary",
            content = analysis.executiveSummary
        )

        IntelligenceCard(
            title = "Relief Sought in Suit [User Ground]",
            badge = "Prayer Clause",
            content = analysis.reliefSought
        )

        IntelligenceCard(
            title = "Case Registry Coordinates [Official Record]",
            badge = "eCourts Verified",
            content = "Filing No: ${courtCase.filingNumber} (Dated: ${courtCase.filingDate})\nRegistration: ${courtCase.registrationDate}\nPetitioner Counsel: ${courtCase.advocateName}\nOpposing Counsel: ${courtCase.opposingCounsel}\nCourtroom Coram: ${courtCase.judgeCourtroom}"
        )
    }
}

@Composable
private fun TabFacts(analysis: CaseIntelligenceAnalysis) {
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IntelligenceCard(
            title = "Material Facts of Controversy [Extracted from Pleadings]",
            badge = "Source: Pleadings",
            content = analysis.materialFacts.joinToString("\n\n• ") { it }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = LegalGreenContainer.copy(alpha = 0.5f)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Undisputed Facts", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = LegalGreen)
                    Spacer(modifier = Modifier.height(4.dp))
                    analysis.undisputedFacts.forEach { Text("• $it", fontSize = 11.sp, color = DeepNavy) }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = LegalRedContainer.copy(alpha = 0.5f)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Disputed Facts", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = LegalRed)
                    Spacer(modifier = Modifier.height(4.dp))
                    analysis.disputedFacts.forEach { Text("• $it", fontSize = 11.sp, color = DeepNavy) }
                }
            }
        }
    }
}

@Composable
private fun TabChronology(analysis: CaseIntelligenceAnalysis) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Chronological Chain of Events (List of Dates) [Document Backed]",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )
        }
        items(analysis.chronology) { pair ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = NavyLight
                    ) {
                        Text(
                            text = pair.first,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedGold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = pair.second,
                        fontSize = 12.sp,
                        color = DeepNavy,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TabIssues(analysis: CaseIntelligenceAnalysis) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Substantive Legal Issues for Determination [Court Framing]",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )
        }
        items(analysis.issuesForDetermination) { issue ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(8.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = null,
                        tint = AccentBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = issue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = DeepNavy
                    )
                }
            }
        }
    }
}

@Composable
private fun TabApplicableLaws(analysis: CaseIntelligenceAnalysis) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Statutory Framework & Indian Legislation [Primary Law]",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )
        }
        items(analysis.applicableProvisions) { prov ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MutedGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = prov,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepNavy
                    )
                }
            }
        }
    }
}

@Composable
private fun TabJudgments(authorities: List<LegalAuthority>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Relevant Binding Judicial Precedents [SCC / AIR Citations]",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )
        }
        items(authorities) { auth ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = auth.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = LegalGreenContainer
                        ) {
                            Text(
                                text = "Good Law",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = LegalGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "${auth.citation} • ${auth.court} (${auth.year})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AccentBlue
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ratio: ${auth.ratioDecidendi}",
                        fontSize = 11.sp,
                        color = SlateGrey
                    )
                }
            }
        }
    }
}

@Composable
private fun TabEvidence(documents: List<CaseDocument>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Marked Exhibits & Primary Evidence [Admitted in Court]",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )
        }
        items(documents) { doc ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = GoldContainer
                    ) {
                        Text(
                            text = doc.exhibitNumber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(doc.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                        Text("Date: ${doc.dateOfDocument} • Source: ${doc.source}", fontSize = 10.sp, color = SlateLight)
                    }
                }
            }
        }
    }
}

@Composable
private fun TabArguments(analysis: CaseIntelligenceAnalysis) {
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        IntelligenceCard(
            title = "Petitioner's Case / Submissions [User Strategy]",
            badge = "Plaintiff Counsel",
            content = analysis.petitionerCase
        )

        Text(
            text = "Recommended Legal Arguments for Bench [AI Strategy]",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = DeepNavy
        )

        analysis.recommendedArguments.forEach { arg ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LegalGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(arg, fontSize = 12.sp, color = DeepNavy)
                }
            }
        }
    }
}

@Composable
private fun TabCounterarguments(analysis: CaseIntelligenceAnalysis) {
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        IntelligenceCard(
            title = "Anticipated Opposing Counsel Strategy [Defence]",
            badge = "Defendant Grounds",
            content = analysis.respondentDefence
        )

        Text(
            text = "Potential Case Weaknesses to Address [Guard Points]",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = LegalRed
        )

        analysis.potentialWeaknesses.forEach { weakness ->
            Card(
                colors = CardDefaults.cardColors(containerColor = LegalRedContainer.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = LegalRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(weakness, fontSize = 12.sp, color = DeepNavy)
                }
            }
        }
    }
}

@Composable
private fun TabOrders(courtCase: CourtCase) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Chronological Court Orders & Interim Directions [Certified Record]", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
        Card(
            colors = CardDefaults.cardColors(containerColor = IvorySurface),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Order Dated 24.04.2024 — High Court of Delhi", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text("Coram: ${courtCase.judgeCourtroom}", fontSize = 11.sp, color = SlateLight)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Interim injunction granted restraining encashment of Bank Guarantee No. BG/2022/984 subject to Plaintiff depositing ₹1.5 Crores with Registrar General within two weeks.", fontSize = 11.sp, color = SlateGrey)
            }
        }
    }
}

@Composable
private fun TabDocuments(documents: List<CaseDocument>, onNavigate: (AppScreen) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Linked Case Documents & Evidence (${documents.size})", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
            TextButton(onClick = { onNavigate(AppScreen.DOCUMENT_ROOM) }) {
                Text("Open Document Room", fontSize = 11.sp, color = AccentBlue)
            }
        }
        documents.forEach { doc ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = DeepNavy, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(doc.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                        Text("${doc.category.label} • ${doc.fileSizeKb} KB • OCR Ready", fontSize = 10.sp, color = SlateLight)
                    }
                }
            }
        }
    }
}

@Composable
private fun TabHearingPreparation(courtCase: CourtCase, onNavigate: (AppScreen) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Hearing Readiness Kit", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
            Button(
                onClick = { onNavigate(AppScreen.COURTROOM_PREP) },
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
            ) {
                Text("Open Full Court Brief", fontSize = 11.sp)
            }
        }
        IntelligenceCard(
            title = "Next Hearing Listing",
            badge = courtCase.nextHearingDate,
            content = "Listing Purpose: ${courtCase.nextHearingPurpose}\nCourt: ${courtCase.courtName} (${courtCase.judgeCourtroom})\nOpposing Counsel: ${courtCase.opposingCounsel}"
        )
    }
}

@Composable
private fun IntelligenceCard(title: String, badge: String, content: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = IvorySurface),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = GoldContainer
                ) {
                    Text(
                        text = badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                fontSize = 12.sp,
                color = SlateGrey,
                lineHeight = 17.sp
            )
        }
    }
}
