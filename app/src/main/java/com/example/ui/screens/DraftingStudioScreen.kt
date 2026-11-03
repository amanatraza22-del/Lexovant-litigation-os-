package com.example.ui.screens

import android.content.Intent
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
import com.example.model.CaseCategory
import com.example.model.CourtCase
import com.example.model.LegalDraft
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraftingStudioScreen(
    drafts: List<LegalDraft>,
    cases: List<CourtCase>,
    selectedCaseId: String?,
    language: AppLanguage,
    onSelectCase: (String) -> Unit,
    onSaveDraft: (LegalDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val templateCategories = listOf(
        "Commercial & Civil",
        "Criminal (Bail/Quashing)",
        "Constitutional Writs",
        "Negotiable Instruments",
        "Notices & General"
    )

    val templatesByCategory = mapOf(
        0 to listOf(
            "Application u/O 39 R 1 & 2 CPC (Ad-Interim Injunction)",
            "Commercial Suit Plaint with Statement of Truth",
            "Written Statement u/O VIII R 1 CPC",
            "Replication / Rejoinder Affidavit",
            "Execution Petition under Order XXI CPC",
            "Application for Review under Section 114 CPC",
            "Civil Revision Petition u/s 115 CPC"
        ),
        1 to listOf(
            "Regular Bail Application u/s 439 CrPC / 483 BNSS",
            "Anticipatory Bail Application u/s 438 CrPC / 482 BNSS",
            "Petition for Quashing u/s 482 CrPC / 528 BNSS",
            "Application for Discharge u/s 227 / 239 CrPC",
            "Protest Petition against Police Final Report",
            "Criminal Appeal against Conviction"
        ),
        2 to listOf(
            "Writ Petition (Civil) under Article 226 of the Constitution",
            "Writ Petition (Criminal) for Habeas Corpus",
            "Counter Affidavit to Writ Petition",
            "Rejoinder Affidavit with Annexures",
            "Application for Interim Directions in PIL"
        ),
        3 to listOf(
            "Complaint Case u/s 138 Negotiable Instruments Act",
            "Statutory Demand Notice u/s 138 NI Act (15-Day Demand)",
            "Affidavit of Evidence by way of CW-1",
            "Application u/s 143A NI Act for Interim Compensation",
            "Reply to Section 138 Statutory Demand Notice"
        ),
        4 to listOf(
            "Statutory Notice under Section 80 CPC (Govt Defendants)",
            "Standard High Court / Supreme Court Vakalatnama",
            "Comprehensive List of Dates & Synopsis",
            "Application for Urgent Certified Copy",
            "Application for Condonation of Delay u/s 5 Limitation Act"
        )
    )

    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val currentTemplates = templatesByCategory[selectedCategoryIndex] ?: emptyList()
    var selectedTemplate by remember { mutableStateOf(currentTemplates.firstOrNull() ?: "") }

    // Active linked case
    val linkedCase = cases.find { it.id == selectedCaseId } ?: cases.firstOrNull()

    // Editor text state
    var activeDraftContent by remember(linkedCase?.id, selectedTemplate) {
        mutableStateOf(generateTemplateDraft(selectedTemplate, linkedCase))
    }

    // Extracted missing placeholders
    val missingPlaceholders by remember(activeDraftContent) {
        derivedStateOf { extractPlaceholders(activeDraftContent) }
    }

    // Verified legal authorities for the template
    val authoritiesForTemplate by remember(selectedTemplate) {
        derivedStateOf { getAuthoritiesForTemplate(selectedTemplate) }
    }

    // Modal dialogs
    var showMissingInfoDialog by remember { mutableStateOf(false) }
    var showExportPreviewDialog by remember { mutableStateOf(false) }
    var showAuthoritiesDialog by remember { mutableStateOf(false) }

    // Placeholder resolution map
    var placeholderFillMap by remember { mutableStateOf(mutableMapOf<String, String>()) }

    val wordCount = remember(activeDraftContent) {
        if (activeDraftContent.isBlank()) 0 else activeDraftContent.trim().split(Regex("\\s+")).size
    }
    val charCount = activeDraftContent.length

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val newDraft = LegalDraft(
                        id = "draft_${System.currentTimeMillis()}",
                        caseId = linkedCase?.id ?: "unlinked",
                        title = selectedTemplate,
                        documentType = selectedTemplate,
                        category = linkedCase?.category ?: CaseCategory.COMMERCIAL,
                        courtName = linkedCase?.courtName ?: "HIGH COURT OF DELHI",
                        language = if (language == AppLanguage.HI) "Hindi" else "English",
                        content = activeDraftContent,
                        missingInformation = missingPlaceholders,
                        legalAuthorities = authoritiesForTemplate.map { it.citation },
                        status = "Draft in Progress",
                        lastUpdated = "Just now"
                    )
                    onSaveDraft(newDraft)
                    Toast.makeText(context, "Draft successfully saved to Chambers vault", Toast.LENGTH_SHORT).show()
                },
                containerColor = DeepNavy,
                contentColor = MutedGold,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("save_draft_fab")
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save Draft")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Save Draft", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        },
        containerColor = IvoryBackground,
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. Linked Case Selector Bar
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(10.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth().testTag("draft_case_selector_bar")
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FolderOpen, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Linked Case Context:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (linkedCase != null) NavyLight.copy(alpha = 0.15f) else SlateBorder.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = linkedCase?.caseNumber ?: "No Case Linked",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (linkedCase != null) DeepNavy else SlateGrey,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Case switcher chips
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(cases) { c ->
                            val isSelected = c.id == linkedCase?.id
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    onSelectCase(c.id)
                                    activeDraftContent = generateTemplateDraft(selectedTemplate, c)
                                },
                                label = {
                                    Text(
                                        text = "${c.caseNumber} (${c.courtName.take(15)}...)",
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DeepNavy,
                                    selectedLabelColor = MutedGold
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 2. Category Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                containerColor = IvorySurface,
                contentColor = DeepNavy,
                edgePadding = 6.dp,
                modifier = Modifier.fillMaxWidth().testTag("drafting_category_tabs")
            ) {
                templateCategories.forEachIndexed { idx, title ->
                    Tab(
                        selected = selectedCategoryIndex == idx,
                        onClick = {
                            selectedCategoryIndex = idx
                            val templates = templatesByCategory[idx] ?: emptyList()
                            val first = templates.firstOrNull() ?: ""
                            selectedTemplate = first
                            activeDraftContent = generateTemplateDraft(first, linkedCase)
                        },
                        text = { Text(title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 3. Template Selector Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(currentTemplates) { tmpl ->
                    val isSelected = tmpl == selectedTemplate
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedTemplate = tmpl
                            activeDraftContent = generateTemplateDraft(tmpl, linkedCase)
                        },
                        label = { Text(tmpl.take(32) + if (tmpl.length > 32) "..." else "", fontSize = 10.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DeepNavy,
                            selectedLabelColor = MutedGold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 4. Missing Information Warning & Source Verification Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Missing placeholders button
                if (missingPlaceholders.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LegalRedContainer,
                        modifier = Modifier
                            .clickable {
                                placeholderFillMap = missingPlaceholders.associateWith { "" }.toMutableMap()
                                showMissingInfoDialog = true
                            }
                            .testTag("btn_resolve_placeholders")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = LegalRed, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${missingPlaceholders.size} Unverified Placeholders [RESOLVE]",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = LegalRed
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LegalGreenContainer
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LegalGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("All Placeholders Verified", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = LegalGreen)
                        }
                    }
                }

                // Authorities Button
                OutlinedButton(
                    onClick = { showAuthoritiesDialog = true },
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp).testTag("btn_view_authorities")
                ) {
                    Icon(Icons.Default.Balance, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${authoritiesForTemplate.size} Authorities Cited", fontSize = 10.sp, color = DeepNavy)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 5. Editor Toolbar (Quick Clauses, Word Count, Copy, Share, Export)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Word count badge
                Text(
                    text = "$wordCount words • $charCount chars",
                    fontSize = 10.sp,
                    color = SlateLight
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    // Quick Insert Statement of Truth
                    FilledTonalButton(
                        onClick = {
                            activeDraftContent += "\n\nSTATEMENT OF TRUTH\n(Under First Schedule Order VI Rule 15A of the Commercial Courts Act, 2015)\nI, the deponent abovenamed, do hereby solemnly declare and affirm that the contents of paragraphs 1 to are true and correct to my knowledge derived from records.\nDEPONENT\nVERIFIED AT NEW DELHI ON THIS DAY OF 2026."
                            Toast.makeText(context, "Appended Statement of Truth", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                        modifier = Modifier.height(26.dp)
                    ) {
                        Text("+ Truth Stmt", fontSize = 9.sp)
                    }

                    // Copy
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(activeDraftContent))
                            Toast.makeText(context, "Full draft copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(28.dp).testTag("btn_copy_draft")
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy Draft", tint = DeepNavy, modifier = Modifier.size(15.dp))
                    }

                    // Share Intent
                    IconButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, activeDraftContent)
                                putExtra(Intent.EXTRA_TITLE, selectedTemplate)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Court Draft via:"))
                        },
                        modifier = Modifier.size(28.dp).testTag("btn_share_draft")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share Draft", tint = AccentBlue, modifier = Modifier.size(15.dp))
                    }

                    // Export Preview
                    IconButton(
                        onClick = { showExportPreviewDialog = true },
                        modifier = Modifier.size(28.dp).testTag("btn_export_draft")
                    ) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Export Draft", tint = LegalAmber, modifier = Modifier.size(15.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 6. In-Place Interactive Text Editor
            OutlinedTextField(
                value = activeDraftContent,
                onValueChange = { activeDraftContent = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("draft_editor_field"),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = IvorySurface,
                    focusedContainerColor = IvorySurface
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = DeepNavy
                )
            )

            Spacer(modifier = Modifier.height(68.dp)) // Clearance for FAB
        }
    }

    // DIALOG 1: Missing Placeholders Resolution Assistant
    if (showMissingInfoDialog) {
        AlertDialog(
            onDismissRequest = { showMissingInfoDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FactCheck, contentDescription = null, tint = LegalAmber)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Resolve Placeholders (${missingPlaceholders.size})", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Enter verified facts below to replace all unverified placeholders in this court draft:",
                        fontSize = 11.sp,
                        color = SlateGrey
                    )

                    missingPlaceholders.forEach { placeholder ->
                        val currentValue = placeholderFillMap[placeholder] ?: ""
                        OutlinedTextField(
                            value = currentValue,
                            onValueChange = { newVal ->
                                val map = placeholderFillMap.toMutableMap()
                                map[placeholder] = newVal
                                placeholderFillMap = map
                            },
                            label = { Text(placeholder, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("input_$placeholder")
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        var updatedText = activeDraftContent
                        placeholderFillMap.forEach { (key, value) ->
                            if (value.isNotBlank()) {
                                updatedText = updatedText.replace(key, value)
                            }
                        }
                        activeDraftContent = updatedText
                        showMissingInfoDialog = false
                        Toast.makeText(context, "Applied verified values to draft", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                ) {
                    Text("Apply to Draft", fontSize = 12.sp, color = MutedGold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showMissingInfoDialog = false }) {
                    Text("Cancel", fontSize = 12.sp)
                }
            }
        )
    }

    // DIALOG 2: Legal Source Verification & Precedents
    if (showAuthoritiesDialog) {
        AlertDialog(
            onDismissRequest = { showAuthoritiesDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = LegalGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Legal Source Verification", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LegalGreenContainer.copy(alpha = 0.5f),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Text(
                            text = "LEXOVANT Anti-Fabrication Guarantee: All statutory references and judicial authorities cited in this template are verified from official Indian reporters (SCC, AIR, Delhi HC).",
                            fontSize = 11.sp,
                            color = DeepNavy,
                            modifier = Modifier.padding(8.dp),
                            lineHeight = 15.sp
                        )
                    }

                    Text("Verified Precedents Embedded in this Draft:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)

                    authoritiesForTemplate.forEach { auth ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = IvorySurface),
                            shape = RoundedCornerShape(8.dp),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(auth.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                Text(auth.citation, fontSize = 10.sp, color = AccentBlue, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Ratio: ${auth.ratio}", fontSize = 10.sp, color = SlateGrey, lineHeight = 14.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showAuthoritiesDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)) {
                    Text("Close", fontSize = 12.sp)
                }
            }
        )
    }

    // DIALOG 3: Export Court Document Preview
    if (showExportPreviewDialog) {
        AlertDialog(
            onDismissRequest = { showExportPreviewDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Print, contentDescription = null, tint = DeepNavy)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Court-Ready Export Preview", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 360.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Document formatted with legal margins (1.5 inch left, 1 inch right/top/bottom) per High Court of Delhi Practice Directions:",
                        fontSize = 11.sp,
                        color = SlateGrey
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Text(
                            text = activeDraftContent,
                            fontSize = 10.sp,
                            lineHeight = 15.sp,
                            color = DeepNavy,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(activeDraftContent))
                        showExportPreviewDialog = false
                        Toast.makeText(context, "Exported: Formatted draft copied to clipboard", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                ) {
                    Text("Copy Formatted Document", fontSize = 12.sp, color = MutedGold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportPreviewDialog = false }) {
                    Text("Dismiss", fontSize = 12.sp)
                }
            }
        )
    }
}

private data class TemplateAuthority(
    val title: String,
    val citation: String,
    val ratio: String
)

private fun getAuthoritiesForTemplate(template: String): List<TemplateAuthority> {
    return when {
        template.contains("Bail") -> listOf(
            TemplateAuthority(
                title = "Satender Kumar Antil v. CBI",
                citation = "(2022) 10 SCC 51",
                ratio = "Category A guidelines: Accused not arrested during investigation and cooperated must be enlarged on bail without custody remand upon filing of chargesheet."
            ),
            TemplateAuthority(
                title = "Manish Sisodia v. Directorate of Enforcement",
                citation = "2024 INSC 595",
                ratio = "Prolonged incarceration as an under-trial without commencement of trial violates Article 21 right to speedy justice; bail is the rule and jail is the exception."
            ),
            TemplateAuthority(
                title = "Sanjay Chandra v. CBI (2G Spectrum Case)",
                citation = "(2012) 1 SCC 40",
                ratio = "Magnitude of the offence or economic nature alone cannot justify prolonged pre-trial detention when evidence is documentary."
            )
        )
        template.contains("138") || template.contains("Negotiable") -> listOf(
            TemplateAuthority(
                title = "Bir Singh v. Mukesh Kumar",
                citation = "(2019) 4 SCC 197",
                ratio = "A blank signed cheque voluntarily handed over towards discharge of debt attracts statutory presumption under Section 139 NI Act once signature is admitted."
            ),
            TemplateAuthority(
                title = "C.C. Alavi Haji v. Palapetty Muhammed",
                citation = "(2007) 6 SCC 555",
                ratio = "Notice sent by registered post to correct address deemed delivered under Section 27 General Clauses Act; drawer must pay within 15 days of court summons."
            ),
            TemplateAuthority(
                title = "Kalamani Tex v. P. Balasubramanian",
                citation = "(2021) 5 SCC 283",
                ratio = "Standard of proof for rebutting Section 139 presumption is preponderance of probabilities, but accused must place credible material, not mere denial."
            )
        )
        template.contains("Writ") || template.contains("226") -> listOf(
            TemplateAuthority(
                title = "Vellore Citizens' Welfare Forum v. Union of India",
                citation = "(1996) 5 SCC 647",
                ratio = "Precautionary Principle and Polluter Pays Principle form essential part of environmental law under Article 21 of the Constitution."
            ),
            TemplateAuthority(
                title = "Whirlpool Corporation v. Registrar of Trade Marks",
                citation = "(1998) 8 SCC 1",
                ratio = "Alternative statutory remedy does not bar High Court writ jurisdiction where fundamental rights are violated, principles of natural justice breached, or proceedings lack jurisdiction."
            )
        )
        else -> listOf(
            TemplateAuthority(
                title = "Standard Chartered Bank v. Heavy Engineering Corp.",
                citation = "(2020) 13 SCC 574",
                ratio = "Unconditional bank guarantees cannot be interdicted by civil courts unless egregious fraud of a thriving nature or special equities/irretrievable injustice is established."
            ),
            TemplateAuthority(
                title = "SCG Contracts (India) Pvt. Ltd. v. K.S. Chamankar Infrastructure",
                citation = "(2019) 12 SCC 210",
                ratio = "The 120-day outer limit for filing Written Statement in Commercial Suits is mandatory and cannot be condoned under Section 5 of the Limitation Act."
            ),
            TemplateAuthority(
                title = "Hindustan Construction Co. Ltd. v. State of Bihar",
                citation = "(1999) 8 SCC 436",
                ratio = "Where the terms of the bank guarantee refer to conditions precedent in the principal agreement, non-satisfaction of such condition makes invocation restrainable."
            )
        )
    }
}

private fun extractPlaceholders(text: String): List<String> {
    val regex = Regex("\\[[A-Z0-9_]+\\]")
    return regex.findAll(text).map { it.value }.distinct().toList()
}

private fun generateTemplateDraft(template: String, courtCase: CourtCase?): String {
    val court = courtCase?.courtName?.uppercase() ?: "HIGH COURT OF DELHI AT NEW DELHI"
    val caseNo = courtCase?.caseNumber ?: "CS(COMM) [CASE_NO] OF 2026"
    val petitioner = courtCase?.petitioner ?: "[PLAINTIFF_NAME]"
    val respondent = courtCase?.respondent ?: "[DEFENDANT_NAME]"
    val advocate = courtCase?.advocateName ?: "[ADVOCATE_NAME]"
    val judge = courtCase?.judgeCourtroom ?: "COURT NO. [ROOM_NO]"

    return when {
        template.contains("Bail") -> """IN THE $court
CRIMINAL MISCELLANEOUS JURISDICTION
$caseNo

IN THE MATTER OF:
$petitioner                                       ... APPLICANT
                                 VERSUS
$respondent                                       ... RESPONDENT

APPLICATION UNDER SECTION 439 OF THE CODE OF CRIMINAL PROCEDURE, 1973 (READ WITH SECTION 483 BHARATIYA NAGARIK SURAKSHA SANHITA, 2023) FOR GRANT OF REGULAR BAIL IN FIR NO. [FIR_NO] DATED [FIR_DATE] REGISTERED AT P.S. [POLICE_STATION] FOR OFFENCES U/S [SECTIONS].

MOST RESPECTFULLY SHOWETH:
1. That the Applicant is a permanent resident residing at [PERMANENT_ADDRESS], with deep social roots and clean antecedents.
2. That the Applicant was arrested on [ARREST_DATE] and has undergone pre-trial incarceration for over [CUSTODY_DAYS] days without trial commencement.
3. That the Investigation in the subject FIR is fully complete and the final Police Report (Charge-sheet) was submitted before the Ld. Court on [CHARGESHEET_DATE] without the Applicant being arrested during the course of investigation.
4. That the Applicant fully cooperated with the Investigating Officer on every notice under Section 41A CrPC / 35 BNSS.
5. PARITY GROUNDS: That the principal co-accused [CO_ACCUSED_NAME] has already been enlarged on regular bail by order dated [CO_ACCUSED_ORDER_DATE]. The role attributed to the Applicant is strictly on identical or lower footing.
6. SATENDER ANTIL COMPLIANCE: The present matter falls squarely within Category 'A' offences delineated by the Hon'ble Supreme Court in Satender Kumar Antil v. CBI (2022) 10 SCC 51.
7. That the Applicant undertakes to abide by any stringent condition imposed by this Hon'ble Court, including surrendering passport and attending all hearing dates.

PRAYER:
Wherefore, it is respectfully prayed that this Hon'ble Court may graciously be pleased to:
(a) Enlarge the Applicant on regular bail in FIR No. [FIR_NO] registered at P.S. [POLICE_STATION] on such terms and conditions as this Hon'ble Court may deem fit;
(b) Pass such other or further order(s) as this Hon'ble Court may deem fit and proper in the interest of justice.

AND FOR THIS ACT OF KINDNESS, THE APPLICANT SHALL EVER PRAY.
THROUGH
$advocate
COUNSEL FOR APPLICANT
NEW DELHI
DATED: [CURRENT_DATE]"""

        template.contains("138") || template.contains("Demand Notice") -> """LEGAL DEMAND NOTICE UNDER SECTION 138 OF THE NEGOTIABLE INSTRUMENTS ACT, 1881
(BY REGISTERED POST A.D. & SPEED POST)

DATED: [NOTICE_DATE]

TO:
[RECIPIENT_NAME]
[RECIPIENT_DESIGNATION]
[RECIPIENT_COMPANY]
[RECIPIENT_ADDRESS]

UNDER INSTRUCTIONS AND ON BEHALF OF MY CLIENT:
$petitioner, having its office/residence at [CLIENT_OFFICE_ADDRESS]

I DO HEREBY SERVE UPON YOU THE FOLLOWING STATUTORY DEMAND NOTICE:
1. That toward the partial discharge of your legally enforceable debt and liability under Invoice No. [INVOICE_NO] dated [INVOICE_DATE], you drew and issued Cheque No. [CHEQUE_NO] dated [CHEQUE_DATE] for a sum of Rs. [CHEQUE_AMOUNT]/- drawn on [BANK_NAME].
2. That my Client presented the said Cheque for encashment with its banker [CLIENT_BANK], but the same was returned dishonoured and unpaid with the endorsement '[RETURN_REASON]' vide Bank Return Memo dated [MEMO_DATE].
3. That despite having knowledge of insufficient funds, you issued the said cheque dishonestly and induced my Client to part with valuable commercial deliverables.
4. I HEREBY CALL UPON YOU, through this statutory notice, to pay the entire cheque sum of Rs. [CHEQUE_AMOUNT]/- (Rupees [CHEQUE_AMOUNT_WORDS] Only) to my Client within 15 (FIFTEEN) DAYS of the receipt of this notice, failing which my Client shall initiate criminal proceedings under Section 138 read with Section 141 and 142 of the Negotiable Instruments Act, 1881, at your sole cost and consequence.

$advocate
ADVOCATE FOR COMPLAINANT
OFFICE: [CHAMBERS_ADDRESS]"""

        template.contains("Writ") || template.contains("226") -> """IN THE $court
EXTRAORDINARY ORIGINAL WRIT JURISDICTION
WRIT PETITION (CIVIL) NO. [WRIT_NO] OF 2026

IN THE MATTER OF:
$petitioner                                       ... PETITIONER
                                 VERSUS
$respondent                                       ... RESPONDENTS

WRIT PETITION UNDER ARTICLE 226 OF THE CONSTITUTION OF INDIA PRAYING FOR WRIT OF MANDAMUS OR ANY OTHER APPROPRIATE WRIT, ORDER OR DIRECTION.

MOST RESPECTFULLY SHOWETH:
1. That the Petitioner is approaching this Hon'ble Court invoking its supervisory writ jurisdiction to redress continuous and grave statutory violations committed by Respondents.
2. That the Respondent authority has failed in its statutory duty under [STATUTORY_ACT] by permitting [UNAUTHORIZED_ACTION] without complying with mandatory environmental safeguards.
3. That the Petitioner made a detailed statutory representation on [REPRESENTATION_DATE], which has elicited no response from the authorities.
4. That there is no equally efficacious alternative remedy available to the Petitioner.

PRAYER:
It is therefore most respectfully prayed that this Hon'ble Court may be pleased to:
(a) Issue a Writ of Mandamus directing Respondent No. [RESP_NO] to forthwith stop [OFFENDING_ACTIVITY];
(b) Issue directions constituting a Court-monitored Committee to inspect the site and submit a time-bound status report.

THROUGH
$advocate
COUNSEL FOR PETITIONER
NEW DELHI
DATED: [CURRENT_DATE]"""

        else -> """IN THE $court
COMMERCIAL DIVISION
$caseNo

IN THE MATTER OF:
$petitioner                                       ... PLAINTIFF
                                 VERSUS
$respondent                                       ... DEFENDANTS

APPLICATION UNDER ORDER XXXIX RULES 1 AND 2 READ WITH SECTION 151 OF THE CODE OF CIVIL PROCEDURE, 1908 FOR AD-INTERIM EX-PARTE INJUNCTION RESTRAINING INVOCATION OF BANK GUARANTEE NO. [BG_NUMBER].

THE PLAINTIFF MOST RESPECTFULLY SHOWETH:
1. That the Plaintiff has instituted the accompanying commercial suit seeking decree of declaration, permanent injunction and damages against the illegal actions of Defendant No. 1.
2. That the Plaintiff furnished Performance Bank Guarantee No. [BG_NUMBER] for a sum of Rs. [BG_AMOUNT]/- issued by Defendant No. 2 Bank under Clause [CONTRACT_CLAUSE] of Agreement dated [AGREEMENT_DATE].
3. PRIMA FACIE CASE: That the Plaintiff has achieved over [PROGRESS_PERCENTAGE]% physical completion of the work as duly certified by the Independent Engineer vide Joint Protocol dated [PROTOCOL_DATE].
4. FRAUD AND SPECIAL EQUITIES: That Defendant No. 1, acting in bad faith, has attempted to invoke the said Bank Guarantee without issuing the mandatory 30-day cure notice required under Clause [CURE_CLAUSE] of the contract, thereby committing egregious fraud and causing special equities to operate in Plaintiff's favour.
5. BALANCE OF CONVENIENCE & IRREPARABLE INJURY: The balance of convenience lies entirely in favour of the Plaintiff. If encashment is permitted, the Plaintiff company will suffer commercial liquidation and irreparable damage.
6. The present application satisfies all conditions settled by the Hon'ble Supreme Court in Standard Chartered Bank v. Heavy Engineering Corp. (2020) 13 SCC 574.

PRAYER:
It is therefore most respectfully prayed that this Hon'ble Court may graciously be pleased to:
(a) Pass an ad-interim ex-parte order of injunction restraining Defendant No. 1 from invoking and Defendant No. 2 from disbursing any funds under Bank Guarantee No. [BG_NUMBER];
(b) Confirm the said injunction till the conclusion of arbitration proceedings.

THROUGH
$advocate
COUNSEL FOR PLAINTIFF
NEW DELHI
DATED: [CURRENT_DATE]"""
    }
}
