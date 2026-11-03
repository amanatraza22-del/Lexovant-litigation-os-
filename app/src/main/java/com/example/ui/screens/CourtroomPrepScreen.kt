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
import com.example.data.DemoDataProvider
import com.example.localization.AppLanguage
import com.example.model.CourtCase
import com.example.model.CourtroomPrepKit
import com.example.ui.theme.*

@Composable
fun CourtroomPrepScreen(
    courtCase: CourtCase?,
    cases: List<CourtCase>,
    language: AppLanguage,
    onSelectCase: (String) -> Unit,
    onUpdateKit: ((CourtroomPrepKit) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (courtCase == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Select a case to generate Courtroom Preparation Kit.")
        }
        return
    }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    // Active courtroom prep kit
    var currentKit by remember(courtCase.id) {
        mutableStateOf(DemoDataProvider.getSampleCourtroomPrep(courtCase.id))
    }

    // Interactive checklist states
    var checklistState by remember(courtCase.id, currentKit) {
        mutableStateOf(currentKit.hearingDayChecklist.toMutableList())
    }

    // Physical files carry state
    var filesCarriedState by remember(courtCase.id, currentKit) {
        mutableStateOf(currentKit.documentsToCarry.map { it to false }.toMutableList())
    }

    // Dialog states
    var showEditBriefDialog by remember { mutableStateOf(false) }
    var tempBriefText by remember { mutableStateOf(currentKit.onePageBrief) }

    var showAddQuestionDialog by remember { mutableStateOf(false) }
    var newBenchQuestion by remember { mutableStateOf("") }
    var newCounselAnswer by remember { mutableStateOf("") }

    var showExportKitDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Case Selector & Courtroom Header
        Card(
            colors = CardDefaults.cardColors(containerColor = DeepNavy),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "COURTROOM APPEARANCE KIT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedGold
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = NavyLight
                    ) {
                        Text(
                            text = courtCase.judgeCourtroom,
                            fontSize = 10.sp,
                            color = IvoryBackground,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = courtCase.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${courtCase.caseNumber} • Stage: ${courtCase.currentStage.label} • Next Hearing: ${courtCase.nextHearingDate}",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Switch Active Case Chips
                Text(
                    text = "Switch Active Case Kit:",
                    fontSize = 10.sp,
                    color = MutedGold.copy(alpha = 0.9f),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(cases) { c ->
                        val isSelected = c.id == courtCase.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectCase(c.id) },
                            label = { Text(c.caseNumber, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MutedGold,
                                selectedLabelColor = DeepNavy,
                                containerColor = NavyLight.copy(alpha = 0.5f),
                                labelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action buttons: Copy Brief & Export Kit
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(currentKit.onePageBrief))
                            Toast.makeText(context, "1-Page Bench Brief copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MutedGold),
                        modifier = Modifier.testTag("btn_copy_bench_brief")
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = DeepNavy, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy Bench Brief", fontSize = 11.sp, color = DeepNavy, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { showExportKitDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        modifier = Modifier.testTag("btn_export_courtroom_kit")
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export Appearance Kit", fontSize = 11.sp, color = Color.White)
                    }
                }
            }
        }

        // 2. Missing Court Documents Warning Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = LegalAmberContainer.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(10.dp),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ReportProblem, contentDescription = null, tint = LegalAmber, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Procedural Prerequisites & Court Documents Warning:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = when (courtCase.id) {
                        "case_2" -> "• Check Vakalatnama: Verify Delhi High Court Welfare Stamp (₹25) and Advocate Welfare Stamp attached.\n• Ensure co-accused Supreme Court certified order has index page signed by counsel."
                        "case_4" -> "• Check Original Cheque: Ensure original cheque is in sealed court deposit envelope with exhibit marker Ex. CW-1/1.\n• Section 65B Evidence Act certificate for bank computer scroll must be original."
                        "case_3" -> "• Serve Advance Copy: Confirm receipt of advance copy served to Central Agency Section and DPCC standing counsel.\n• Colored satellite photo maps must be indexed with page numbers."
                        else -> "• Registry Deposit Receipt: ₹1.5 Cr registry deposit receipt must be attached as Annexure P-1 to convenience synopsis.\n• Ensure 2 complete convenience compilations are handed over to Court Master prior to 10:30 AM."
                    },
                    fontSize = 10.sp,
                    color = DeepNavy,
                    lineHeight = 15.sp
                )
            }
        }

        // 3. Section 1: One-Page Bench Brief (Editable In-Place)
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
                    Text(
                        text = "One-Page Bench Brief (Instant Reference)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )

                    IconButton(
                        onClick = {
                            tempBriefText = currentKit.onePageBrief
                            showEditBriefDialog = true
                        },
                        modifier = Modifier.size(28.dp).testTag("btn_edit_bench_brief")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Bench Brief", tint = AccentBlue, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentKit.onePageBrief,
                    fontSize = 11.sp,
                    color = SlateGrey,
                    lineHeight = 16.sp
                )
            }
        }

        // 4. Section 2: Predicted Bench Questions & Proposed Answers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Questions Likely to Arise from the Bench:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )

            FilledTonalButton(
                onClick = { showAddQuestionDialog = true },
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                modifier = Modifier.height(28.dp).testTag("btn_add_bench_qna")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Q&A", fontSize = 10.sp)
            }
        }

        currentKit.benchQuestionsPredicted.forEachIndexed { idx, qna ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(8.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Help, contentDescription = null, tint = LegalAmber, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Bench Query: ${qna.first}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = LegalGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Counsel Reply: ${qna.second}",
                            fontSize = 11.sp,
                            color = SlateGrey
                        )
                    }
                }
            }
        }

        // 5. Section 3: Oral Argument Sequence
        Card(
            colors = CardDefaults.cardColors(containerColor = IvorySurface),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Oral Argument Sequence (Point by Point)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                currentKit.oralArgumentOutline.forEach { point ->
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 3.dp)) {
                        Icon(Icons.Default.ArrowRight, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = point,
                            fontSize = 11.sp,
                            color = DeepNavy,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // 6. Section 4: Physical Files & Compilations to Carry (Interactive check-off)
        Card(
            colors = CardDefaults.cardColors(containerColor = IvorySurface),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Physical Files & Compilations to Carry:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Text(
                        text = "${filesCarriedState.count { it.second }}/${filesCarriedState.size} Packed",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LegalGreen
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                filesCarriedState.forEachIndexed { idx, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val updated = filesCarriedState.toMutableList()
                                updated[idx] = Pair(item.first, !item.second)
                                filesCarriedState = updated
                            }
                            .padding(vertical = 3.dp)
                    ) {
                        Checkbox(
                            checked = item.second,
                            onCheckedChange = { checked ->
                                val updated = filesCarriedState.toMutableList()
                                updated[idx] = Pair(item.first, checked)
                                filesCarriedState = updated
                            }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = item.first,
                            fontSize = 11.sp,
                            color = if (item.second) SlateLight else DeepNavy
                        )
                    }
                }
            }
        }

        // 7. Section 5: Hearing-Day Morning Checklist
        Card(
            colors = CardDefaults.cardColors(containerColor = IvorySurface),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Hearing-Day Morning Checklist:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                checklistState.forEachIndexed { idx, pair ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val updated = checklistState.toMutableList()
                                updated[idx] = Pair(pair.first, !pair.second)
                                checklistState = updated
                            }
                            .padding(vertical = 3.dp)
                    ) {
                        Checkbox(
                            checked = pair.second,
                            onCheckedChange = { checked ->
                                val updated = checklistState.toMutableList()
                                updated[idx] = Pair(pair.first, checked)
                                checklistState = updated
                            }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = pair.first,
                            fontSize = 11.sp,
                            color = if (pair.second) SlateLight else DeepNavy
                        )
                    }
                }
            }
        }
    }

    // DIALOG 1: Edit Bench Brief In-Place
    if (showEditBriefDialog) {
        AlertDialog(
            onDismissRequest = { showEditBriefDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EditNote, contentDescription = null, tint = DeepNavy)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit 1-Page Bench Brief", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Counsel can refine facts, statutory propositions or cited authorities below:", fontSize = 11.sp, color = SlateGrey)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tempBriefText,
                        onValueChange = { tempBriefText = it },
                        modifier = Modifier.fillMaxWidth().height(260.dp),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, lineHeight = 16.sp, color = DeepNavy)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = currentKit.copy(onePageBrief = tempBriefText)
                        currentKit = updated
                        onUpdateKit?.invoke(updated)
                        showEditBriefDialog = false
                        Toast.makeText(context, "Bench Brief saved", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                ) {
                    Text("Save Brief", fontSize = 12.sp, color = MutedGold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditBriefDialog = false }) {
                    Text("Cancel", fontSize = 12.sp)
                }
            }
        )
    }

    // DIALOG 2: Add Predicted Bench Question & Answer
    if (showAddQuestionDialog) {
        AlertDialog(
            onDismissRequest = { showAddQuestionDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.QuestionAnswer, contentDescription = null, tint = AccentBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Bench Q&A", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newBenchQuestion,
                        onValueChange = { newBenchQuestion = it },
                        label = { Text("Bench Query / Judge Concern") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCounselAnswer,
                        onValueChange = { newCounselAnswer = it },
                        label = { Text("Counsel's Strategic Reply / Statutory Cites") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newBenchQuestion.isNotBlank()) {
                            val updatedQuestions = currentKit.benchQuestionsPredicted + (newBenchQuestion to newCounselAnswer)
                            val updated = currentKit.copy(benchQuestionsPredicted = updatedQuestions)
                            currentKit = updated
                            onUpdateKit?.invoke(updated)
                            newBenchQuestion = ""
                            newCounselAnswer = ""
                            showAddQuestionDialog = false
                            Toast.makeText(context, "Added question to Appearance Kit", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                ) {
                    Text("Add Q&A", fontSize = 12.sp, color = MutedGold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddQuestionDialog = false }) {
                    Text("Cancel", fontSize = 12.sp)
                }
            }
        )
    }

    // DIALOG 3: Export Complete Courtroom Appearance Kit
    if (showExportKitDialog) {
        val fullKitExport = buildString {
            appendLine("==================================================")
            appendLine("LEXOVANT LITIGATIONOS — COURTROOM APPEARANCE KIT")
            appendLine("CASE: ${courtCase.caseNumber}")
            appendLine("TITLE: ${courtCase.title}")
            appendLine("COURT: ${courtCase.courtName} (${courtCase.judgeCourtroom})")
            appendLine("STAGE: ${courtCase.currentStage.label} • NEXT DATE: ${courtCase.nextHearingDate}")
            appendLine("ADVOCATE: ${courtCase.advocateName}")
            appendLine("==================================================")
            appendLine("\n[I. 1-PAGE BENCH BRIEF]\n")
            appendLine(currentKit.onePageBrief)
            appendLine("\n[II. ORAL ARGUMENTS SEQUENCE]\n")
            currentKit.oralArgumentOutline.forEach { appendLine(it) }
            appendLine("\n[III. PREDICTED BENCH Q&A]\n")
            currentKit.benchQuestionsPredicted.forEach {
                appendLine("Q: ${it.first}")
                appendLine("A: ${it.second}\n")
            }
            appendLine("[IV. COMPILATIONS & FILES TO CARRY]\n")
            currentKit.documentsToCarry.forEach { appendLine("• $it") }
            appendLine("\n==================================================")
        }

        AlertDialog(
            onDismissRequest = { showExportKitDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Print, contentDescription = null, tint = DeepNavy)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export Courtroom Kit", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Text(
                            text = fullKitExport,
                            fontSize = 10.sp,
                            lineHeight = 14.sp,
                            color = DeepNavy,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilledTonalButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, fullKitExport)
                                putExtra(Intent.EXTRA_TITLE, "Courtroom Kit - ${courtCase.caseNumber}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Courtroom Kit via:"))
                        }
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Share", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(fullKitExport))
                            showExportKitDialog = false
                            Toast.makeText(context, "Full Courtroom Kit copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                    ) {
                        Text("Copy Full Kit", fontSize = 11.sp, color = MutedGold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportKitDialog = false }) {
                    Text("Close", fontSize = 11.sp)
                }
            }
        )
    }
}
