package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.CourtCase
import com.example.model.TeamMember
import com.example.ui.theme.*

@Composable
fun TeamScreen(
    team: List<TeamMember>,
    cases: List<CourtCase>,
    language: AppLanguage,
    onSelectCase: (String) -> Unit,
    onAssignCase: ((String, String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAssignModal by remember { mutableStateOf(false) }
    var selectedMemberForAssign by remember { mutableStateOf<TeamMember?>(null) }

    val auditLog = listOf(
        "03 Nov, 10:15 AM: Adv. Sneha Sen drafted Regular Bail Application in BAIL APPLN. 1824/2024.",
        "02 Nov, 04:30 PM: Adv. Rajesh Malhotra reviewed Rejoinder submissions in CS(COMM) 412/2024.",
        "02 Nov, 11:20 AM: Rohan Verma verified Section 12 certified copy limitation in CS(COMM) 412/2024.",
        "01 Nov, 02:45 PM: Adv. Ananya Roy scheduled PIL compliance hearing for 18 Nov 2026."
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. Chambers Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavy),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("CHAMBERS & LAW FIRM TEAM WORKSPACE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MutedGold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Adv. Rajesh Malhotra & Associates", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Chamber 412, Lawyers Chambers Block, High Court of Delhi", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(shape = RoundedCornerShape(4.dp), color = NavyLight) {
                        Text(
                            text = "Organisation Data Isolation • Role-Based Access Control (RBAC)",
                            fontSize = 10.sp,
                            color = IvoryBackground,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // 2. Team Members List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Chambers Advocates & Allocated Matters (${team.size}):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
            }
        }

        items(team, key = { it.id }) { member ->
            val assignedCaseList = when (member.name) {
                "Adv. Rajesh Malhotra" -> cases.take(3)
                "Adv. Sneha Sen" -> cases.filter { it.category.name == "CONSTITUTIONAL" || it.category.name == "CRIMINAL" }
                "Rohan Verma" -> cases.filter { it.category.name == "COMMERCIAL" }
                else -> cases.take(1)
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = GoldContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = member.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldDark
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(member.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                            Text(member.role, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = AccentBlue)
                            Text(member.assignedSpeciality, fontSize = 10.sp, color = SlateLight)
                        }
                        OutlinedButton(
                            onClick = {
                                selectedMemberForAssign = member
                                showAssignModal = true
                            },
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp).testTag("btn_assign_${member.id}")
                        ) {
                            Icon(Icons.Default.AddLink, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Assign Case", fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Assigned Case Files (${assignedCaseList.size}):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DeepNavy)
                    Spacer(modifier = Modifier.height(4.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(assignedCaseList) { c ->
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = SubtleBlueContainer,
                                modifier = Modifier.clickable {
                                    onSelectCase(c.id)
                                    Toast.makeText(context, "Switched to assigned case: ${c.caseNumber}", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Icon(Icons.Default.Folder, contentDescription = null, tint = SubtleBlue, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${c.caseNumber} (${c.courtName.take(12)}...)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SubtleBlue
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. RBAC Permissions Matrix
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(10.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Role-Based Access Control (RBAC) Permissions:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("• Senior Advocate: Full Read/Write/Delete on all matters, Billing settlement, Vault export.", fontSize = 10.sp, color = SlateGrey)
                    Text("• Associate Advocate: Draft generation, Case Intelligence, Courtroom Prep, Hearing notes.", fontSize = 10.sp, color = SlateGrey)
                    Text("• Junior Advocate: Document indexing, Limitation computation, Cause list calendar updates.", fontSize = 10.sp, color = SlateGrey)
                    Text("• Legal Researcher / Paralegal: OCR text extraction, Precedent research, Read-only pleadings.", fontSize = 10.sp, color = SlateGrey)
                }
            }
        }

        // 4. Activity Audit Log
        item {
            Text("Chambers Security & Activity Audit Log:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
        }

        items(auditLog) { log ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(8.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.History, contentDescription = null, tint = SlateLight, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(log, fontSize = 11.sp, color = SlateGrey, lineHeight = 15.sp)
                }
            }
        }
    }

    // Assign Case Dialog
    if (showAssignModal && selectedMemberForAssign != null) {
        val member = selectedMemberForAssign!!
        var caseToAssign by remember { mutableStateOf(cases.firstOrNull()) }

        AlertDialog(
            onDismissRequest = { showAssignModal = false },
            title = { Text("Assign Matter to ${member.name}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select case from chambers docket to allocate:", fontSize = 11.sp, color = SlateGrey)
                    cases.forEach { c ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { caseToAssign = c }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = caseToAssign?.id == c.id,
                                onClick = { caseToAssign = c }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(c.caseNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                Text(c.title.take(35) + "...", fontSize = 10.sp, color = SlateLight)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        caseToAssign?.let {
                            onAssignCase?.invoke(member.id, it.id)
                            Toast.makeText(context, "Case ${it.caseNumber} allocated to ${member.name}", Toast.LENGTH_SHORT).show()
                        }
                        showAssignModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                ) {
                    Text("Confirm Allocation", color = MutedGold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAssignModal = false }) { Text("Cancel") }
            }
        )
    }
}
