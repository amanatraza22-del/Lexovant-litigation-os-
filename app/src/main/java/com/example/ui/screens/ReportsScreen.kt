package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.model.CaseCategory
import com.example.model.Client
import com.example.model.CourtCase
import com.example.model.Hearing
import com.example.model.Invoice
import com.example.ui.theme.*

@Composable
fun ReportsScreen(
    cases: List<CourtCase>,
    hearings: List<Hearing>,
    invoices: List<Invoice>,
    clients: List<Client>,
    language: AppLanguage,
    onSelectCase: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val categoryDistribution = cases.groupBy { it.category }
    val courtDistribution = cases.groupBy { it.courtName }
    val totalRevenue = invoices.sumOf { it.paidAmount }
    val totalOutstanding = invoices.sumOf { it.totalAmount - it.paidAmount }
    val pendingHearings = hearings.count { !it.isCompleted }
    val completedHearings = hearings.count { it.isCompleted }
    val totalHearings = hearings.size.coerceAtLeast(1)
    val clearanceRate = (completedHearings * 100) / totalHearings

    val casesWithLimitation = cases.filter { it.limitationDate != "N/A" && it.limitationDate.isNotEmpty() }

    val fullPracticeReport = buildString {
        appendLine("==================================================")
        appendLine("LEXOVANT LITIGATIONOS — PRACTICE PERFORMANCE REPORT")
        appendLine("CHAMBERS OF ADV. RAJESH MALHOTRA (DELHI HIGH COURT)")
        appendLine("DATE: 03 November 2026")
        appendLine("==================================================")
        appendLine("Total Active Cases: ${cases.size}")
        appendLine("Pending Cause List Hearings: $pendingHearings")
        appendLine("Completed Hearings: $completedHearings (Clearance Rate: $clearanceRate%)")
        appendLine("Total Revenue Collected: ₹${totalRevenue.toInt()}/-")
        appendLine("Outstanding Client Fees: ₹${totalOutstanding.toInt()}/-")
        appendLine("\n[CASE DISTRIBUTION BY JURISDICTION]")
        categoryDistribution.forEach { (cat, list) ->
            appendLine("• ${cat.displayName}: ${list.size} cases")
        }
        appendLine("\n[CASES PENDING STATUTORY LIMITATION]")
        casesWithLimitation.forEach { c ->
            appendLine("• ${c.caseNumber} (${c.title}): Deadline ${c.limitationDate}")
        }
        appendLine("==================================================")
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. Executive Performance Banner
        item {
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
                        Text("PRACTICE PERFORMANCE & ANALYTICS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MutedGold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, fullPracticeReport)
                                        putExtra(Intent.EXTRA_TITLE, "Practice Performance Report")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share Practice Report via:"))
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = MutedGold, modifier = Modifier.size(16.dp))
                            }
                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(fullPracticeReport))
                                    Toast.makeText(context, "Practice report copied to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MutedGold),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Copy Dossier", fontSize = 10.sp, color = DeepNavy, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Executive Chambers Analytics", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Real-time workload, court cause list clearance & fee performance metrics.", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }

        // 2. High-Level KPI Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = IvorySurface), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Active Cases", fontSize = 11.sp, color = SlateLight)
                        Text("${cases.size}", fontSize = 20.sp, fontWeight = FontWeight.Black, color = DeepNavy)
                        Text("Across 4 Courts", fontSize = 10.sp, color = SlateLight)
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = IvorySurface), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Hearing Clearance", fontSize = 11.sp, color = SlateLight)
                        Text("$clearanceRate%", fontSize = 20.sp, fontWeight = FontWeight.Black, color = LegalGreen)
                        Text("$completedHearings done / $totalHearings listed", fontSize = 10.sp, color = SlateLight)
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = IvorySurface), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Revenue Collected", fontSize = 11.sp, color = SlateLight)
                        Text("₹${totalRevenue.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = LegalGreen)
                        Text("FY 2026-27 YTD", fontSize = 10.sp, color = SlateLight)
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = IvorySurface), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Outstanding Fees", fontSize = 11.sp, color = SlateLight)
                        Text("₹${totalOutstanding.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = LegalAmber)
                        Text("${invoices.count { it.paidAmount < it.totalAmount }} Pending Bills", fontSize = 10.sp, color = SlateLight)
                    }
                }
            }
        }

        // 3. Subject Matter Distribution
        item {
            Text("Case Distribution by Subject Matter:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    categoryDistribution.forEach { (cat, list) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(cat.displayName, fontSize = 12.sp, color = DeepNavy, fontWeight = FontWeight.Medium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LinearProgressIndicator(
                                    progress = { list.size.toFloat() / cases.size.coerceAtLeast(1) },
                                    modifier = Modifier.width(100.dp).height(6.dp),
                                    color = AccentBlue,
                                    trackColor = SlateBorder
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("${list.size} (${(list.size * 100 / cases.size.coerceAtLeast(1))}%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                            }
                        }
                    }
                }
            }
        }

        // 4. Limitation Deadlines & Risk Register
        item {
            Text("Statutory Limitation Risk Register (${casesWithLimitation.size} Tracked Matters):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
        }

        items(casesWithLimitation, key = { it.id }) { c ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(8.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.clickable {
                    onSelectCase(c.id)
                    Toast.makeText(context, "Opened case: ${c.caseNumber}", Toast.LENGTH_SHORT).show()
                }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(c.caseNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                        Text(c.title, fontSize = 11.sp, color = SlateGrey)
                        Text("Court: ${c.courtName}", fontSize = 10.sp, color = SlateLight)
                    }
                    Surface(shape = RoundedCornerShape(4.dp), color = LegalAmberContainer) {
                        Text(
                            text = "Due: ${c.limitationDate}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = LegalAmber,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
