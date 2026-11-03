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
import com.example.model.Client
import com.example.model.CourtCase
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientManagementScreen(
    clients: List<Client>,
    cases: List<CourtCase>,
    language: AppLanguage,
    onSelectCase: (String) -> Unit,
    onAddClient: (Client) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedClientForReport by remember { mutableStateOf<Client?>(null) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = DeepNavy,
                contentColor = MutedGold,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("add_client_fab")
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Client Profile", fontSize = 13.sp, fontWeight = FontWeight.Bold)
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

            // Client Practice Metrics Summary
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavy),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("CHAMBERS CLIENT DIRECTORY & RETENTION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MutedGold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Active Clients", fontSize = 10.sp, color = SlateLight)
                            Text("${clients.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text("Total Outstanding", fontSize = 10.sp, color = SlateLight)
                            Text("₹${clients.sumOf { it.outstandingFees }.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = LegalAmber)
                        }
                        Column {
                            Text("Active Matters", fontSize = 10.sp, color = SlateLight)
                            Text("${cases.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = LegalGreen)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Client Profiles & Linked Case Records (${clients.size}):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(clients, key = { it.id }) { client ->
                    val linkedCases = cases.filter {
                        it.clientId == client.id || it.clientName.contains(client.name, ignoreCase = true) || client.name.contains(it.petitioner, ignoreCase = true)
                    }

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
                                Text(client.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (client.outstandingFees > 0) LegalAmberContainer else LegalGreenContainer
                                ) {
                                    Text(
                                        text = if (client.outstandingFees > 0) "Fee Due: ₹${client.outstandingFees.toInt()}" else "Fees Settled",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (client.outstandingFees > 0) LegalAmber else LegalGreen,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text("${client.organization} • ${client.clientType}", fontSize = 11.sp, color = SlateLight)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Email: ${client.email} | Phone: ${client.phone}", fontSize = 11.sp, color = SlateGrey)
                            Text("Address: ${client.address}", fontSize = 10.sp, color = SlateLight)

                            Spacer(modifier = Modifier.height(6.dp))

                            // Linked Case Badges
                            if (linkedCases.isNotEmpty()) {
                                Text("Linked Case Matters:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    linkedCases.forEach { lc ->
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = SubtleBlueContainer,
                                            modifier = Modifier.clickable {
                                                onSelectCase(lc.id)
                                                Toast.makeText(context, "Opened case: ${lc.caseNumber}", Toast.LENGTH_SHORT).show()
                                            }
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                            ) {
                                                Icon(Icons.Default.Gavel, contentDescription = null, tint = SubtleBlue, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "${lc.caseNumber} (${lc.currentStage.label})",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = SubtleBlue
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(shape = RoundedCornerShape(6.dp), color = IvorySurfaceVariant) {
                                Text(
                                    text = "Consultation Notes: ${client.consultationNotes}",
                                    fontSize = 10.sp,
                                    color = SlateGrey,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Client Update Report Button
                            Button(
                                onClick = { selectedClientForReport = client },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.fillMaxWidth().testTag("btn_report_${client.id}")
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, tint = MutedGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Generate Client Update Report", fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    // Dynamic Client Update Report Dialog
    selectedClientForReport?.let { client ->
        val linkedCase = cases.find {
            it.clientId == client.id || it.clientName.contains(client.name, ignoreCase = true) || client.name.contains(it.petitioner, ignoreCase = true)
        } ?: cases.firstOrNull()

        val report = buildString {
            appendLine("==================================================")
            appendLine("CHAMBERS OF ADV. RAJESH MALHOTRA")
            appendLine("High Court of Delhi | Supreme Court of India")
            appendLine("OFFICIAL LITIGATION CASE PROGRESS UPDATE")
            appendLine("DATE: 03 November 2026")
            appendLine("==================================================")
            appendLine("CLIENT: ${client.name} (${client.organization})")
            appendLine("CASE TITLE: ${linkedCase?.title ?: "M/s Apex Infra v. Northern Energy"}")
            appendLine("CASE NUMBER: ${linkedCase?.caseNumber ?: "CS(COMM) 412/2024"}")
            appendLine("COURT / BENCH: ${linkedCase?.courtName} (${linkedCase?.judgeCourtroom})")
            appendLine("CURRENT STAGE: ${linkedCase?.currentStage?.label}")
            appendLine("NEXT HEARING DATE: ${linkedCase?.nextHearingDate}")
            appendLine("PURPOSE OF LISTING: ${linkedCase?.nextHearingPurpose}")
            appendLine("\n[RECENT ORDERS & ADVOCATE NOTES]")
            appendLine(linkedCase?.notes ?: "Rejoinder submissions filed. Matter listed for arguments.")
            appendLine("\n[CHAMBERS FINANCIAL SUMMARY]")
            appendLine("Outstanding Balance: ₹${client.outstandingFees.toInt()}/-")
            appendLine("Please deposit pending professional fee prior to next hearing.")
            appendLine("==================================================")
            appendLine("For further queries, please reach out to Chamber Clerk at +91 98110 44210.")
        }

        AlertDialog(
            onDismissRequest = { selectedClientForReport = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MarkEmailRead, contentDescription = null, tint = DeepNavy)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Client Update Report", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            },
            text = {
                val scroll = rememberScrollState()
                Column(modifier = Modifier.fillMaxWidth().heightIn(max = 380.dp).verticalScroll(scroll)) {
                    Text(
                        text = "Real-time client summary extracted directly from linked case records:",
                        fontSize = 11.sp,
                        color = SlateGrey
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(shape = RoundedCornerShape(8.dp), color = Color.White, border = CardDefaults.outlinedCardBorder()) {
                        Text(report, fontSize = 10.sp, lineHeight = 15.sp, color = DeepNavy, modifier = Modifier.padding(10.dp))
                    }
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilledTonalButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, report)
                                putExtra(Intent.EXTRA_TITLE, "Case Update - ${client.name}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Client Update via WhatsApp/Email:"))
                        }
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Share", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(report))
                            Toast.makeText(context, "Client report copied for WhatsApp/Email sharing", Toast.LENGTH_SHORT).show()
                            selectedClientForReport = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                    ) {
                        Text("Copy Report", color = MutedGold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedClientForReport = null }) { Text("Close") }
            }
        )
    }

    if (showAddDialog) {
        var name by remember { mutableStateOf("") }
        var org by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Client Profile") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Client Name / Representative") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = org, onValueChange = { org = it }, label = { Text("Organization / Company") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email Address") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Consultation Notes / Retainer Terms") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            val newC = Client(
                                id = "client_${System.currentTimeMillis()}",
                                name = name,
                                organization = if (org.isBlank()) "Individual" else org,
                                email = email,
                                phone = phone,
                                address = "New Delhi, India",
                                clientType = "Private Litigant",
                                activeCases = 1,
                                outstandingFees = 0.0,
                                consultationNotes = notes
                            )
                            onAddClient(newC)
                            showAddDialog = false
                            Toast.makeText(context, "Client profile registered", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                ) {
                    Text("Add Client", color = MutedGold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}
