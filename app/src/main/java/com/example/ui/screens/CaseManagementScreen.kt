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
import com.example.localization.AppStrings
import com.example.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseManagementScreen(
    cases: List<CourtCase>,
    language: AppLanguage,
    onSelectCase: (String) -> Unit,
    onOpenCaseIntelligence: (String) -> Unit,
    onAddCase: (CourtCase) -> Unit,
    onUpdateCase: (CourtCase) -> Unit = {},
    onDeleteCase: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<CaseCategory?>(null) }
    var selectedStatus by remember { mutableStateOf<CaseStatus?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var caseToEdit by remember { mutableStateOf<CourtCase?>(null) }
    var caseToDelete by remember { mutableStateOf<CourtCase?>(null) }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val filteredCases = cases.filter { c ->
        val matchesSearch = searchQuery.isEmpty() ||
                c.title.contains(searchQuery, ignoreCase = true) ||
                c.caseNumber.contains(searchQuery, ignoreCase = true) ||
                c.cnrNumber.contains(searchQuery, ignoreCase = true) ||
                c.petitioner.contains(searchQuery, ignoreCase = true) ||
                c.respondent.contains(searchQuery, ignoreCase = true)

        val matchesCategory = selectedCategory == null || c.category == selectedCategory
        val matchesStatus = selectedStatus == null || c.status == selectedStatus

        matchesSearch && matchesCategory && matchesStatus
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = DeepNavy,
                contentColor = MutedGold,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("add_case_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add New Case")
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (language == AppLanguage.HI) "नया केस दर्ज करें" else "Register Case",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
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
            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = AppStrings.get("global_search_hint", language),
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = SlateGrey
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = IvorySurface,
                    focusedContainerColor = IvorySurface
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("case_search_field")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All (${cases.size})", fontSize = 11.sp) }
                    )
                }
                items(CaseCategory.values()) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = if (selectedCategory == category) null else category },
                        label = {
                            Text(
                                if (language == AppLanguage.HI) category.hiName else category.displayName,
                                fontSize = 11.sp
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Count header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredCases.size} Cases Listed",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SlateLight
                )
                Text(
                    text = "eCourts CNR Verified",
                    fontSize = 11.sp,
                    color = LegalGreen,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Case List
            if (filteredCases.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.FolderOff,
                            contentDescription = null,
                            tint = SlateLight,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No cases match your filters",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredCases, key = { it.id }) { courtCase ->
                        CaseCard(
                            courtCase = courtCase,
                            language = language,
                            onOpenWorkspace = {
                                onSelectCase(courtCase.id)
                                onOpenCaseIntelligence(courtCase.id)
                            },
                            onCopySummary = {
                                val summary = """CASE SUMMARY — LEXOVANT LITIGATIONOS
${courtCase.title}
Case No: ${courtCase.caseNumber} | CNR: ${courtCase.cnrNumber}
Court: ${courtCase.courtName} (${courtCase.courtEstablishment})
Stage: ${courtCase.currentStage.label}
Next Hearing: ${courtCase.nextHearingDate} (${courtCase.nextHearingPurpose})
Counsel: ${courtCase.advocateName} vs. ${courtCase.opposingCounsel}
Notes: ${courtCase.notes}"""
                                clipboardManager.setText(AnnotatedString(summary))
                                Toast.makeText(context, "Case summary copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            onEdit = { caseToEdit = courtCase },
                            onDelete = { caseToDelete = courtCase }
                        )
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    caseToDelete?.let { caseItem ->
        AlertDialog(
            onDismissRequest = { caseToDelete = null },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = LegalRed) },
            title = { Text("Confirm Deletion / Archive") },
            text = {
                Text("Are you sure you want to remove '${caseItem.caseNumber}' from your active practice list? Case records, filings, and hearing history will be archived.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteCase(caseItem.id)
                        caseToDelete = null
                        Toast.makeText(context, "Case archived", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LegalRed)
                ) {
                    Text("Archive Case")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { caseToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Case Dialog
    caseToEdit?.let { existingCase ->
        CaseFormDialog(
            dialogTitle = "Edit Litigation Case Details",
            initialCase = existingCase,
            language = language,
            onDismiss = { caseToEdit = null },
            onSave = { updated ->
                onUpdateCase(updated)
                caseToEdit = null
                Toast.makeText(context, "Case ${updated.caseNumber} updated", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Add Case Dialog
    if (showAddDialog) {
        CaseFormDialog(
            dialogTitle = if (language == AppLanguage.HI) "नया मुकदमा दर्ज करें (Register Case)" else "Register New Litigation Case",
            initialCase = null,
            language = language,
            onDismiss = { showAddDialog = false },
            onSave = { newCase ->
                onAddCase(newCase)
                showAddDialog = false
                Toast.makeText(context, "Case ${newCase.caseNumber} registered successfully", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
private fun CaseCard(
    courtCase: CourtCase,
    language: AppLanguage,
    onOpenWorkspace: () -> Unit,
    onCopySummary: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = IvorySurface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("case_card_${courtCase.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Badges
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
                        text = if (language == AppLanguage.HI) courtCase.category.hiName else courtCase.category.displayName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (courtCase.status == CaseStatus.ACTIVE) LegalGreenContainer else NavyLight
                ) {
                    Text(
                        text = if (language == AppLanguage.HI) courtCase.status.hiLabel else courtCase.status.label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (courtCase.status == CaseStatus.ACTIVE) LegalGreen else IvoryBackground,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title & Numbers
            Text(
                text = courtCase.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${courtCase.caseNumber} • CNR: ${courtCase.cnrNumber}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AccentBlue
            )
            Text(
                text = "${courtCase.courtName} • ${courtCase.judgeCourtroom}",
                fontSize = 11.sp,
                color = SlateGrey
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = SlateBorder.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // Stage and Next Hearing
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Current Stage",
                        fontSize = 10.sp,
                        color = SlateLight
                    )
                    Text(
                        text = courtCase.currentStage.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = DeepNavy
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Next Hearing",
                        fontSize = 10.sp,
                        color = SlateLight
                    )
                    Text(
                        text = courtCase.nextHearingDate,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LegalAmber
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onOpenWorkspace,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = MutedGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Intelligence",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Case",
                        tint = DeepNavy
                    )
                }

                IconButton(onClick = onCopySummary) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Case Summary",
                        tint = SlateGrey
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Archive,
                        contentDescription = "Archive Case",
                        tint = SlateLight
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CaseFormDialog(
    dialogTitle: String,
    initialCase: CourtCase?,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (CourtCase) -> Unit
) {
    var title by remember { mutableStateOf(initialCase?.title ?: "") }
    var caseNumber by remember { mutableStateOf(initialCase?.caseNumber ?: "") }
    var cnrNumber by remember { mutableStateOf(initialCase?.cnrNumber ?: "") }
    var courtName by remember { mutableStateOf(initialCase?.courtName ?: "High Court of Delhi") }
    var courtEstablishment by remember { mutableStateOf(initialCase?.courtEstablishment ?: "Original Civil / Commercial Division") }
    var category by remember { mutableStateOf(initialCase?.category ?: CaseCategory.COMMERCIAL) }
    var stage by remember { mutableStateOf(initialCase?.currentStage ?: CaseStage.ADMISSION) }
    var petitioner by remember { mutableStateOf(initialCase?.petitioner ?: "") }
    var respondent by remember { mutableStateOf(initialCase?.respondent ?: "") }
    var judge by remember { mutableStateOf(initialCase?.judgeCourtroom ?: "") }
    var nextHearing by remember { mutableStateOf(initialCase?.nextHearingDate ?: "2026-11-25") }
    var purpose by remember { mutableStateOf(initialCase?.nextHearingPurpose ?: "Admission / Framing of Issues") }
    var notes by remember { mutableStateOf(initialCase?.notes ?: "") }

    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = dialogTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 450.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Case Title (e.g., Party A v. Party B)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_case_title")
                )

                OutlinedTextField(
                    value = caseNumber,
                    onValueChange = { caseNumber = it },
                    label = { Text("Case Number (e.g., CS(COMM) 102/2026)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_case_number")
                )

                OutlinedTextField(
                    value = cnrNumber,
                    onValueChange = { cnrNumber = it },
                    label = { Text("16-Digit eCourts CNR Number") },
                    placeholder = { Text("e.g. DLDH010098762026") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_case_cnr")
                )

                OutlinedTextField(
                    value = courtName,
                    onValueChange = { courtName = it },
                    label = { Text("Court / Forum") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = petitioner,
                    onValueChange = { petitioner = it },
                    label = { Text("Petitioner / Plaintiff") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = respondent,
                    onValueChange = { respondent = it },
                    label = { Text("Respondent / Defendant") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = judge,
                    onValueChange = { judge = it },
                    label = { Text("Judge / Courtroom No.") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nextHearing,
                    onValueChange = { nextHearing = it },
                    label = { Text("Next Hearing Date (YYYY-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Case Brief & Remarks") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && caseNumber.isNotBlank()) {
                        val finalId = initialCase?.id ?: "case_${System.currentTimeMillis()}"
                        val finalCnr = if (cnrNumber.isBlank()) (initialCase?.cnrNumber ?: "DLDH0100${System.currentTimeMillis().toString().takeLast(8)}") else cnrNumber
                        val savedCase = CourtCase(
                            id = finalId,
                            title = title,
                            caseNumber = caseNumber,
                            cnrNumber = finalCnr,
                            courtName = courtName,
                            courtEstablishment = courtEstablishment,
                            category = category,
                            caseType = caseNumber.substringBefore("/"),
                            filingNumber = initialCase?.filingNumber ?: "FIL/DEL/2026/${(1000..9999).random()}",
                            filingDate = initialCase?.filingDate ?: "2026-11-03",
                            registrationDate = initialCase?.registrationDate ?: "2026-11-03",
                            petitioner = if (petitioner.isBlank()) "Petitioner" else petitioner,
                            respondent = if (respondent.isBlank()) "Respondent" else respondent,
                            advocateName = initialCase?.advocateName ?: "Adv. Rajesh Malhotra",
                            opposingCounsel = initialCase?.opposingCounsel ?: "Opposing Counsel Awaited",
                            judgeCourtroom = if (judge.isBlank()) "Court No. 12" else judge,
                            currentStage = stage,
                            status = initialCase?.status ?: CaseStatus.ACTIVE,
                            nextHearingDate = nextHearing,
                            nextHearingPurpose = purpose,
                            limitationDate = initialCase?.limitationDate ?: "2026-12-03",
                            clientName = if (petitioner.isNotBlank()) petitioner else (initialCase?.clientName ?: "Private Client"),
                            clientId = initialCase?.clientId ?: "client_custom",
                            notes = notes,
                            isDemo = false
                        )
                        onSave(savedCase)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                modifier = Modifier.testTag("btn_save_case")
            ) {
                Text(if (initialCase != null) "Update Case" else "Register Case")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
