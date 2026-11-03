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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.model.CourtCase
import com.example.model.Hearing
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtDiaryScreen(
    hearings: List<Hearing>,
    cases: List<CourtCase>,
    selectedCaseId: String?,
    language: AppLanguage,
    onAddHearing: (Hearing) -> Unit,
    onUpdateHearing: (Hearing) -> Unit,
    onSelectCase: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedCaseFilter by remember { mutableStateOf<String?>(selectedCaseId) }
    var showAddDialog by remember { mutableStateOf(false) }

    // Filter by timing (All / Today / Upcoming) and Case
    val filteredByTiming = when (selectedFilter) {
        "Today" -> hearings.filter { it.date == "2026-11-03" }
        "Upcoming" -> hearings.filter { it.date > "2026-11-03" }
        else -> hearings
    }

    val filtered = if (selectedCaseFilter == null) {
        filteredByTiming
    } else {
        filteredByTiming.filter { it.caseId == selectedCaseFilter }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = DeepNavy,
                contentColor = MutedGold,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("add_hearing_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Hearing")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Schedule Hearing", fontWeight = FontWeight.Bold, fontSize = 13.sp)
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
            Spacer(modifier = Modifier.height(10.dp))

            // 1. Timing Filter Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Today", "Upcoming").forEach { tab ->
                    val isSelected = selectedFilter == tab
                    FilledTonalButton(
                        onClick = { selectedFilter = tab },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (isSelected) DeepNavy else IvorySurface,
                            contentColor = if (isSelected) MutedGold else DeepNavy
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(tab, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 2. Case Connection Filter Row
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(8.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Filter Diary by Linked Case:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item {
                            FilterChip(
                                selected = selectedCaseFilter == null,
                                onClick = { selectedCaseFilter = null },
                                label = { Text("All Cases (${hearings.size})", fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DeepNavy,
                                    selectedLabelColor = MutedGold
                                )
                            )
                        }
                        items(cases) { c ->
                            val isSelected = selectedCaseFilter == c.id
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedCaseFilter = if (isSelected) null else c.id
                                },
                                label = { Text(c.caseNumber, fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DeepNavy,
                                    selectedLabelColor = MutedGold
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Court Cause List & Hearing Schedule (${filtered.size}):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered, key = { it.id }) { hearing ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = IvorySurface),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (hearing.date == "2026-11-03") LegalRedContainer else GoldContainer
                                ) {
                                    Text(
                                        text = "${hearing.date} • ${hearing.time}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (hearing.date == "2026-11-03") LegalRed else GoldDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (hearing.reminderSet) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                                        contentDescription = "Toggle reminder",
                                        tint = if (hearing.reminderSet) MutedGold else SlateLight,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clickable {
                                                onUpdateHearing(hearing.copy(reminderSet = !hearing.reminderSet))
                                                Toast.makeText(context, if (!hearing.reminderSet) "Hearing reminder enabled" else "Reminder disabled", Toast.LENGTH_SHORT).show()
                                            }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Checkbox(
                                        checked = hearing.isCompleted,
                                        onCheckedChange = { checked ->
                                            onUpdateHearing(hearing.copy(isCompleted = checked))
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = hearing.caseTitle,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                            Text(
                                text = "${hearing.caseNumber} • ${hearing.courtName}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AccentBlue
                            )
                            Text(
                                text = hearing.courtroom,
                                fontSize = 11.sp,
                                color = SlateGrey
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Purpose: ${hearing.purpose}",
                                fontSize = 11.sp,
                                color = DeepNavy,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Previous Order: ${hearing.previousOrderSummary}",
                                fontSize = 11.sp,
                                color = SlateLight
                            )

                            if (hearing.adjournmentHistory.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Adjournment History: ${hearing.adjournmentHistory.joinToString(" | ")}",
                                    fontSize = 10.sp,
                                    color = LegalAmber
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Case Connection Action Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        onSelectCase(hearing.caseId)
                                        Toast.makeText(context, "Opened case: ${hearing.caseNumber}", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Open Case Intelligence", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddHearingDialog(
            cases = cases,
            initialCaseId = selectedCaseFilter ?: selectedCaseId ?: cases.firstOrNull()?.id,
            onDismiss = { showAddDialog = false },
            onSave = { hearing ->
                onAddHearing(hearing)
                showAddDialog = false
                Toast.makeText(context, "Hearing scheduled for ${hearing.date}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddHearingDialog(
    cases: List<CourtCase>,
    initialCaseId: String?,
    onDismiss: () -> Unit,
    onSave: (Hearing) -> Unit
) {
    var selectedCase by remember {
        mutableStateOf(cases.find { it.id == initialCaseId } ?: cases.firstOrNull())
    }

    var caseTitle by remember(selectedCase) {
        mutableStateOf(selectedCase?.title ?: "M/s Apex Infra v. Northern Energy")
    }
    var caseNumber by remember(selectedCase) {
        mutableStateOf(selectedCase?.caseNumber ?: "CS(COMM) 412/2024")
    }
    var courtName by remember(selectedCase) {
        mutableStateOf(selectedCase?.courtName ?: "High Court of Delhi")
    }
    var courtroom by remember(selectedCase) {
        mutableStateOf(selectedCase?.judgeCourtroom ?: "Court No. 14")
    }
    var date by remember { mutableStateOf("2026-11-20") }
    var time by remember { mutableStateOf("11:30 AM (Item No. 12)") }
    var purpose by remember(selectedCase) {
        mutableStateOf(selectedCase?.nextHearingPurpose ?: "Framing of Issues / Interlocutory Arguments")
    }

    val scroll = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Event, contentDescription = null, tint = DeepNavy)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Schedule Court Hearing", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .verticalScroll(scroll),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Select Registered Case to Link:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(cases) { c ->
                        val isSelected = c.id == selectedCase?.id
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedCase = c
                                caseTitle = c.title
                                caseNumber = c.caseNumber
                                courtName = c.courtName
                                courtroom = c.judgeCourtroom
                                purpose = c.nextHearingPurpose
                            },
                            label = { Text(c.caseNumber, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DeepNavy,
                                selectedLabelColor = MutedGold
                            )
                        )
                    }
                }

                OutlinedTextField(value = caseTitle, onValueChange = { caseTitle = it }, label = { Text("Case Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = caseNumber, onValueChange = { caseNumber = it }, label = { Text("Case Number") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = courtName, onValueChange = { courtName = it }, label = { Text("Court Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = courtroom, onValueChange = { courtroom = it }, label = { Text("Courtroom / Presiding Judge") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Hearing Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Time & Cause List Item No.") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = purpose, onValueChange = { purpose = it }, label = { Text("Purpose of Listing") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newH = Hearing(
                        id = "h_${System.currentTimeMillis()}",
                        caseId = selectedCase?.id ?: "case_1",
                        caseTitle = caseTitle,
                        caseNumber = caseNumber,
                        courtName = courtName,
                        courtroom = courtroom,
                        date = date,
                        time = time,
                        purpose = purpose,
                        judge = courtroom,
                        previousOrderSummary = "Listed for further proceedings.",
                        nextSteps = "Prepare bench argument brief.",
                        reminderSet = true,
                        isCompleted = false
                    )
                    onSave(newH)
                },
                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
            ) {
                Text("Schedule Hearing", color = MutedGold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
