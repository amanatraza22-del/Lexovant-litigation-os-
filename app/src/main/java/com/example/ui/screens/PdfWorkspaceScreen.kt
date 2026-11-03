package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.model.CaseDocument
import com.example.model.CourtCase
import com.example.ui.theme.*

@Composable
fun PdfWorkspaceScreen(
    cases: List<CourtCase>,
    documents: List<CaseDocument>,
    selectedCaseId: String?,
    language: AppLanguage,
    onSelectCase: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    var activeToolIndex by remember { mutableIntStateOf(0) }
    val tools = listOf("Court Index & Synopsis", "Document Diff Comparison", "OCR Text Extractor")

    var selectedCase by remember(selectedCaseId) {
        mutableStateOf(cases.find { it.id == selectedCaseId } ?: cases.firstOrNull())
    }

    val caseDocs = documents.filter { it.caseId == (selectedCase?.id ?: "case_1") }

    // Dynamically generated Court Filing Index Table based on real case documents
    val dynamicCourtIndexTable = remember(selectedCase, caseDocs) {
        buildString {
            appendLine("COURT OF RECORD FILING INDEX & SYNOPSIS")
            appendLine("IN THE ${selectedCase?.courtName?.uppercase() ?: "HIGH COURT OF DELHI"}")
            appendLine("${selectedCase?.caseNumber ?: "CS(COMM) 412/2024"}")
            appendLine("IN THE MATTER OF: ${selectedCase?.title ?: "M/s Apex Infra Projects v. Northern Energy"}")
            appendLine("CNR NUMBER: ${selectedCase?.cnrNumber ?: "DLDH010045232024"}")
            appendLine("\nINDEX TO PAPERS, PLEADINGS & EXHIBIT COMPILATION:")
            appendLine("--------------------------------------------------------------------------------")
            appendLine(String.format("%-4s | %-42s | %-12s | %-10s", "S.No", "Description of Document", "Date", "Page No."))
            appendLine("--------------------------------------------------------------------------------")
            appendLine(String.format("%-4s | %-42s | %-12s | %-10s", "1.", "Urgent Application for Interim Relief", "18.04.2024", "1 - 4"))
            appendLine(String.format("%-4s | %-42s | %-12s | %-10s", "2.", "Notice of Motion & Court Fee Receipts", "18.04.2024", "5 - 7"))
            appendLine(String.format("%-4s | %-42s | %-12s | %-10s", "3.", "Synopsis and Chronological List of Dates", "18.04.2024", "8 - 14"))
            appendLine(String.format("%-4s | %-42s | %-12s | %-10s", "4.", "Main Pleadings with Statement of Truth", "18.04.2024", "15 - 42"))

            var currentPage = 43
            caseDocs.forEachIndexed { idx, doc ->
                val endPage = currentPage + (doc.fileSizeKb / 100).coerceAtLeast(3)
                val sNo = "${idx + 5}."
                val desc = "${doc.exhibitNumber}: ${doc.title.take(36)}"
                appendLine(String.format("%-4s | %-42s | %-12s | %-10s", sNo, desc, doc.dateOfDocument, "$currentPage - $endPage"))
                currentPage = endPage + 1
            }

            appendLine(String.format("%-4s | %-42s | %-12s | %-10s", "${caseDocs.size + 5}.", "Certificate u/s 65B Evidence Act / Sec 63 BSA", "18.04.2024", "$currentPage - ${currentPage + 1}"))
            appendLine(String.format("%-4s | %-42s | %-12s | %-10s", "${caseDocs.size + 6}.", "Duly Stamped Vakalatnama with Welfare Stamp", "18.04.2024", "${currentPage + 2}"))
            appendLine("--------------------------------------------------------------------------------")
            appendLine("NEW DELHI                                              FILED BY: ${selectedCase?.advocateName}")
            appendLine("DATED: 03.11.2026                                      COUNSEL FOR PETITIONER/PLAINTIFF")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Workspace Header
        Card(
            colors = CardDefaults.cardColors(containerColor = DeepNavy),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = MutedGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PDF & Court Document Workspace",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedGold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Automated court compilation tools: Formatted High Court filing index tables, side-by-side pleading diffs, and case-linked OCR text extraction.",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = 15.sp
                )
            }
        }

        // 2. Case Selector Bar
        Card(
            colors = CardDefaults.cardColors(containerColor = IvorySurface),
            shape = RoundedCornerShape(10.dp),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Select Case for PDF Compilation:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                    Surface(shape = RoundedCornerShape(4.dp), color = SubtleBlueContainer) {
                        Text(
                            text = "${caseDocs.size} Exhibits Attached",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SubtleBlue,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(cases) { c ->
                        val isSelected = c.id == selectedCase?.id
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedCase = c
                                onSelectCase(c.id)
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

        // 3. Tool Selector Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            tools.forEachIndexed { idx, name ->
                val isSelected = activeToolIndex == idx
                FilterChip(
                    selected = isSelected,
                    onClick = { activeToolIndex = idx },
                    label = { Text(name, fontSize = 10.sp) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        when (activeToolIndex) {
            0 -> {
                // Tool 1: Court Index & Synopsis Generator
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
                            Text("Standard Filing Index Table (${selectedCase?.caseNumber})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, dynamicCourtIndexTable)
                                        putExtra(Intent.EXTRA_TITLE, "Filing Index - ${selectedCase?.caseNumber}")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share Court Index Table:"))
                                }) {
                                    Icon(Icons.Default.Share, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(16.dp))
                                }
                                IconButton(onClick = {
                                    clipboardManager.setText(AnnotatedString(dynamicCourtIndexTable))
                                    Toast.makeText(context, "Filing Index copied to clipboard", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = SlateGrey, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(shape = RoundedCornerShape(6.dp), color = DeepNavy) {
                            Text(
                                text = dynamicCourtIndexTable,
                                fontSize = 9.sp,
                                color = IvoryBackground,
                                modifier = Modifier.padding(10.dp),
                                lineHeight = 13.sp
                            )
                        }
                    }
                }
            }
            1 -> {
                // Tool 2: Document Comparison Diff
                Card(
                    colors = CardDefaults.cardColors(containerColor = IvorySurface),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Pleadings Diff: Plaint vs Written Statement (${selectedCase?.caseNumber})", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = LegalRedContainer.copy(alpha = 0.4f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Plaintiff's Case Assertion", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = LegalRed)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Para 4: 92.4% physical completion achieved as of 28.02.2024. Invocation of Bank Guarantee without cure notice violates Clause 18 and causes irreparable insolvency.",
                                        fontSize = 10.sp,
                                        color = DeepNavy,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                            Card(
                                colors = CardDefaults.cardColors(containerColor = LegalGreenContainer.copy(alpha = 0.4f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Defendant's Reply Defence", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = LegalGreen)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Para 4: Guarantee is unconditional and independent. Commercial operation date (COD) was missed. Courts cannot restrain bank invocation per U.P. State Sugar (1997).",
                                        fontSize = 10.sp,
                                        color = DeepNavy,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            else -> {
                // Tool 3: OCR Scanned Document Text Extractor
                Card(
                    colors = CardDefaults.cardColors(containerColor = IvorySurface),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("OCR Text Snippets from ${selectedCase?.caseNumber} (${caseDocs.size} indexed):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                        Spacer(modifier = Modifier.height(8.dp))
                        if (caseDocs.isEmpty()) {
                            Text("No documents indexed for this case yet.", fontSize = 11.sp, color = SlateLight)
                        } else {
                            caseDocs.forEach { doc ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = IvorySurfaceVariant),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("${doc.exhibitNumber}: ${doc.title}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                            IconButton(
                                                onClick = {
                                                    clipboardManager.setText(AnnotatedString(doc.ocrSnippet))
                                                    Toast.makeText(context, "OCR text copied", Toast.LENGTH_SHORT).show()
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = SlateGrey, modifier = Modifier.size(14.dp))
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "\"${doc.ocrSnippet}\"",
                                            fontSize = 10.sp,
                                            color = SlateGrey,
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
