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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.model.Client
import com.example.model.CourtCase
import com.example.model.Invoice
import com.example.model.InvoiceStatus
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingScreen(
    invoices: List<Invoice>,
    cases: List<CourtCase>,
    clients: List<Client>,
    selectedCaseId: String?,
    language: AppLanguage,
    onSelectCase: (String) -> Unit,
    onAddInvoice: (Invoice) -> Unit,
    onUpdateInvoice: ((Invoice) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedStatusFilter by remember { mutableStateOf<InvoiceStatus?>(null) }
    var selectedCaseFilter by remember { mutableStateOf<String?>(selectedCaseId) }

    val filteredByCase = if (selectedCaseFilter == null) invoices else invoices.filter { it.caseId == selectedCaseFilter }
    val filtered = filteredByCase.filter {
        selectedStatusFilter == null || it.status == selectedStatusFilter
    }

    val totalBilled = filteredByCase.sumOf { it.totalAmount }
    val totalCollected = filteredByCase.sumOf { it.paidAmount }
    val totalOutstanding = totalBilled - totalCollected

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = DeepNavy,
                contentColor = MutedGold,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("generate_invoice_fab")
            ) {
                Icon(Icons.Default.AddCard, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Generate Invoice", fontSize = 13.sp, fontWeight = FontWeight.Bold)
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

            // 1. Revenue Summary Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavy),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("PRACTICE FINANCIAL OVERVIEW (INR ₹)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MutedGold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Total Billed", fontSize = 10.sp, color = SlateLight)
                            Text("₹${(totalBilled).toInt()}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text("Collected", fontSize = 10.sp, color = SlateLight)
                            Text("₹${(totalCollected).toInt()}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = LegalGreen)
                        }
                        Column {
                            Text("Outstanding", fontSize = 10.sp, color = SlateLight)
                            Text("₹${(totalOutstanding).toInt()}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = LegalAmber)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 2. Case Filter Bar
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(8.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("Filter Invoices by Linked Case:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item {
                            FilterChip(
                                selected = selectedCaseFilter == null,
                                onClick = { selectedCaseFilter = null },
                                label = { Text("All Matters (${invoices.size})", fontSize = 10.sp) },
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

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Status Filter Chips
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(
                    selected = selectedStatusFilter == null,
                    onClick = { selectedStatusFilter = null },
                    label = { Text("All Statuses", fontSize = 10.sp) }
                )
                InvoiceStatus.values().forEach { status ->
                    FilterChip(
                        selected = selectedStatusFilter == status,
                        onClick = { selectedStatusFilter = if (selectedStatusFilter == status) null else status },
                        label = { Text(status.label, fontSize = 10.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Fee Invoices & Case Billing Records (${filtered.size}):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered, key = { it.id }) { inv ->
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
                                Text(inv.invoiceNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentBlue)
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = when (inv.status) {
                                        InvoiceStatus.PAID -> LegalGreenContainer
                                        InvoiceStatus.PARTIAL -> LegalAmberContainer
                                        InvoiceStatus.OUTSTANDING -> LegalRedContainer
                                    }
                                ) {
                                    Text(
                                        text = inv.status.label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (inv.status) {
                                            InvoiceStatus.PAID -> LegalGreen
                                            InvoiceStatus.PARTIAL -> LegalAmber
                                            InvoiceStatus.OUTSTANDING -> LegalRed
                                        },
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(inv.clientName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                            Text("Linked Case: ${inv.caseNumber} • ${inv.description}", fontSize = 11.sp, color = SlateGrey)

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Total Amount (incl 18% GST)", fontSize = 10.sp, color = SlateLight)
                                    Text("₹${inv.totalAmount.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.Black, color = DeepNavy)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Paid: ₹${inv.paidAmount.toInt()}", fontSize = 11.sp, color = LegalGreen, fontWeight = FontWeight.SemiBold)
                                    Text("Due Date: ${inv.dueDate}", fontSize = 10.sp, color = SlateLight)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Payment Action & Case Workspace Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (inv.status != InvoiceStatus.PAID) {
                                    FilledTonalButton(
                                        onClick = {
                                            val updated = inv.copy(paidAmount = inv.totalAmount, status = InvoiceStatus.PAID)
                                            onUpdateInvoice?.invoke(updated)
                                            Toast.makeText(context, "Invoice ${inv.invoiceNumber} marked as fully PAID", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LegalGreen, modifier = Modifier.size(13.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Mark as Paid", fontSize = 10.sp)
                                    }
                                } else {
                                    Text("Settled in Full", fontSize = 10.sp, color = LegalGreen, fontWeight = FontWeight.Bold)
                                }

                                TextButton(
                                    onClick = {
                                        onSelectCase(inv.caseId)
                                        Toast.makeText(context, "Switched to ${inv.caseNumber}", Toast.LENGTH_SHORT).show()
                                    },
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Open Case Workspace", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var selectedCaseForInvoice by remember {
            mutableStateOf(cases.find { it.id == selectedCaseFilter } ?: cases.firstOrNull())
        }
        var clientName by remember(selectedCaseForInvoice) {
            mutableStateOf(selectedCaseForInvoice?.clientName ?: "Apex Infra Projects Pvt. Ltd.")
        }
        var caseNumber by remember(selectedCaseForInvoice) {
            mutableStateOf(selectedCaseForInvoice?.caseNumber ?: "CS(COMM) 412/2024")
        }
        var description by remember(selectedCaseForInvoice) {
            mutableStateOf("Appearance for ${selectedCaseForInvoice?.currentStage?.label ?: "Arguments"} before ${selectedCaseForInvoice?.courtName ?: "Delhi High Court"}")
        }
        var amountStr by remember { mutableStateOf("75000") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Receipt, contentDescription = null, tint = DeepNavy)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate Case Invoice", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select Case for Invoicing:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(cases) { c ->
                            val isSelected = c.id == selectedCaseForInvoice?.id
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedCaseForInvoice = c
                                    clientName = c.clientName
                                    caseNumber = c.caseNumber
                                    description = "Appearance for ${c.currentStage.label} before ${c.courtName}"
                                },
                                label = { Text(c.caseNumber, fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DeepNavy,
                                    selectedLabelColor = MutedGold
                                )
                            )
                        }
                    }

                    OutlinedTextField(value = clientName, onValueChange = { clientName = it }, label = { Text("Client Name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = caseNumber, onValueChange = { caseNumber = it }, label = { Text("Case Number") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Professional Services Description") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = amountStr, onValueChange = { amountStr = it }, label = { Text("Base Professional Fee (INR ₹)") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                    val base = amountStr.toDoubleOrNull() ?: 0.0
                    val gst = base * 0.18
                    val total = base + gst
                    Surface(shape = RoundedCornerShape(6.dp), color = IvorySurfaceVariant, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("GST (18%): ₹${gst.toInt()} (CGST 9% + SGST 9%)", fontSize = 10.sp, color = SlateGrey)
                            Text("Total Payable: ₹${total.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val base = amountStr.toDoubleOrNull() ?: 50000.0
                        val gst = base * 0.18
                        val total = base + gst
                        val newInv = Invoice(
                            id = "inv_${System.currentTimeMillis()}",
                            invoiceNumber = "INV/LEX/2026/${(100..999).random()}",
                            clientId = selectedCaseForInvoice?.clientId ?: "client_1",
                            clientName = clientName,
                            caseId = selectedCaseForInvoice?.id ?: "case_1",
                            caseNumber = caseNumber,
                            description = description,
                            baseAmount = base,
                            gstRatePercent = 18.0,
                            totalAmount = total,
                            paidAmount = 0.0,
                            date = "2026-11-03",
                            dueDate = "2026-11-17",
                            status = InvoiceStatus.OUTSTANDING
                        )
                        onAddInvoice(newInv)
                        showAddDialog = false
                        Toast.makeText(context, "Invoice ${newInv.invoiceNumber} generated for ₹${total.toInt()}", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                ) {
                    Text("Generate Invoice", color = MutedGold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}
