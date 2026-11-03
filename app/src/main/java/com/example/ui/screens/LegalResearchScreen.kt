package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.model.CourtCase
import com.example.model.LegalAuthority
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalResearchScreen(
    authorities: List<LegalAuthority>,
    cases: List<CourtCase> = emptyList(),
    selectedCaseId: String? = null,
    language: AppLanguage,
    onSelectCase: (String) -> Unit = {},
    onAddAuthority: (LegalAuthority) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedAct by remember { mutableStateOf("All Acts") }
    var selectedCaseFilter by remember { mutableStateOf(selectedCaseId ?: "ALL") }
    var showAddDialog by remember { mutableStateOf(false) }

    val acts = listOf(
        "All Acts",
        "Commercial Courts Act 2015",
        "Limitation Act 1963",
        "CPC 1908",
        "CrPC / BNSS 2023",
        "NI Act 1881",
        "Arbitration Act 1996"
    )

    val filtered = authorities.filter { auth ->
        val matchesCase = selectedCaseFilter == "ALL" || auth.caseId == selectedCaseFilter
        val matchesAct = selectedAct == "All Acts" ||
                auth.relevantParagraphs.contains(selectedAct, ignoreCase = true) ||
                auth.ratioDecidendi.contains(selectedAct, ignoreCase = true) ||
                auth.title.contains(selectedAct, ignoreCase = true)
        val matchesSearch = searchQuery.isEmpty() ||
                auth.title.contains(searchQuery, ignoreCase = true) ||
                auth.citation.contains(searchQuery, ignoreCase = true) ||
                auth.ratioDecidendi.contains(searchQuery, ignoreCase = true) ||
                auth.bench.contains(searchQuery, ignoreCase = true)

        matchesCase && matchesAct && matchesSearch
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = DeepNavy,
                contentColor = MutedGold,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("btn_add_authority")
            ) {
                Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Precedent Authority", fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
                placeholder = { Text("Search SCC, AIR citations, judges, ratio...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = IvorySurface,
                    focusedContainerColor = IvorySurface
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("research_search_field")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Linked Case Filter Row
            if (cases.isNotEmpty()) {
                Text(
                    text = if (language == AppLanguage.HI) "केस संदर्भ के अनुसार फ़िल्टर:" else "Filter by Case Context:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedCaseFilter == "ALL",
                            onClick = { selectedCaseFilter = "ALL" },
                            label = { Text("All Cases (${authorities.size})", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DeepNavy,
                                selectedLabelColor = MutedGold
                            )
                        )
                    }
                    items(cases) { c ->
                        val count = authorities.count { it.caseId == c.id }
                        FilterChip(
                            selected = selectedCaseFilter == c.id,
                            onClick = {
                                selectedCaseFilter = c.id
                                onSelectCase(c.id)
                            },
                            label = { Text("${c.caseNumber} ($count)", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DeepNavy,
                                selectedLabelColor = MutedGold
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Statutory Act Filter Chips (CPC, Limitation Act, Commercial Courts, etc.)
            Text(
                text = if (language == AppLanguage.HI) "सांविधिक अधिनियम (Statutory Act):" else "Statutory Act Enactments:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(acts) { act ->
                    FilterChip(
                        selected = selectedAct == act,
                        onClick = { selectedAct = act },
                        label = { Text(act, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AccentBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Official sources notice
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = NavyLight
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = MutedGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.get("official_sources_notice", language),
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Precedent Authorities & Ratio Decidendi (${filtered.size}):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
                if (selectedAct != "All Acts" || selectedCaseFilter != "ALL") {
                    TextButton(onClick = {
                        selectedAct = "All Acts"
                        selectedCaseFilter = "ALL"
                    }) {
                        Text("Reset Filters", fontSize = 11.sp, color = AccentBlue)
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered, key = { it.id }) { auth ->
                    val linkedCase = cases.find { it.id == auth.caseId }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = IvorySurface),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth().testTag("authority_card_${auth.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = auth.citation,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentBlue
                                )
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (auth.isOverruled) LegalRedContainer else LegalGreenContainer
                                ) {
                                    Text(
                                        text = if (auth.isOverruled) "Overruled" else "Good Law",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (auth.isOverruled) LegalRed else LegalGreen,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = auth.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                            Text(
                                text = "${auth.court} (${auth.year}) • Bench: ${auth.bench}",
                                fontSize = 11.sp,
                                color = SlateLight
                            )

                            if (linkedCase != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = GoldContainer
                                ) {
                                    Text(
                                        text = "Linked Case: ${linkedCase.caseNumber}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = GoldDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ratio Decidendi:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                            Text(
                                text = auth.ratioDecidendi,
                                fontSize = 12.sp,
                                color = SlateGrey,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Statutory Reference / Paragraphs: ${auth.relevantParagraphs}",
                                fontSize = 10.sp,
                                color = SlateLight
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            // Quick Action Buttons on Authority Card
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString("${auth.title}, ${auth.citation} (${auth.court})"))
                                        Toast.makeText(context, "Citation copied to clipboard", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Copy Citation", fontSize = 11.sp)
                                }

                                OutlinedButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString("Ratio (${auth.citation}): ${auth.ratioDecidendi}"))
                                        Toast.makeText(context, "Ratio decidendi copied", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.CopyAll, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Copy Ratio", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var title by remember { mutableStateOf("") }
        var citation by remember { mutableStateOf("") }
        var court by remember { mutableStateOf("Supreme Court of India") }
        var bench by remember { mutableStateOf("") }
        var ratio by remember { mutableStateOf("") }
        var targetCaseId by remember { mutableStateOf(selectedCaseFilter.takeIf { it != "ALL" } ?: cases.firstOrNull()?.id ?: "case_1") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Save Legal Authority") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (cases.isNotEmpty()) {
                        Text("Associate with Case:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            items(cases) { c ->
                                FilterChip(
                                    selected = targetCaseId == c.id,
                                    onClick = { targetCaseId = c.id },
                                    label = { Text(c.caseNumber, fontSize = 10.sp) }
                                )
                            }
                        }
                    }
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Case Title") }, singleLine = true)
                    OutlinedTextField(value = citation, onValueChange = { citation = it }, label = { Text("Official Citation (SCC/AIR)") }, singleLine = true)
                    OutlinedTextField(value = court, onValueChange = { court = it }, label = { Text("Court / High Court") }, singleLine = true)
                    OutlinedTextField(value = bench, onValueChange = { bench = it }, label = { Text("Coram / Bench") }, singleLine = true)
                    OutlinedTextField(value = ratio, onValueChange = { ratio = it }, label = { Text("Ratio Decidendi / Proposition") }, minLines = 2)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            val newAuth = LegalAuthority(
                                id = "auth_${System.currentTimeMillis()}",
                                caseId = targetCaseId,
                                title = title,
                                citation = if (citation.isBlank()) "(2026) INSC" else citation,
                                court = court,
                                year = 2026,
                                bench = bench,
                                ratioDecidendi = ratio,
                                relevantParagraphs = "Counsel added research note",
                                isOverruled = false
                            )
                            onAddAuthority(newAuth)
                            showAddDialog = false
                            Toast.makeText(context, "Authority saved and linked", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}
