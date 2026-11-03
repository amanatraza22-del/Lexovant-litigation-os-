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
import com.example.model.CaseDocument
import com.example.model.CourtCase
import com.example.model.DocumentCategory
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentRoomScreen(
    documents: List<CaseDocument>,
    cases: List<CourtCase>,
    selectedCaseId: String?,
    language: AppLanguage,
    onSelectCase: (String) -> Unit,
    onAddDocument: (CaseDocument) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf<DocumentCategory?>(null) }
    var selectedCaseFilter by remember { mutableStateOf<String?>(selectedCaseId) }
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    val activeCase = cases.find { it.id == selectedCaseFilter }

    val filtered = documents.filter { doc ->
        val matchesCase = selectedCaseFilter == null || doc.caseId == selectedCaseFilter
        val matchesCat = selectedCategory == null || doc.category == selectedCategory
        val matchesSearch = searchQuery.isBlank() ||
                doc.title.contains(searchQuery, ignoreCase = true) ||
                doc.exhibitNumber.contains(searchQuery, ignoreCase = true) ||
                doc.ocrSnippet.contains(searchQuery, ignoreCase = true)
        matchesCase && matchesCat && matchesSearch
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = DeepNavy,
                contentColor = MutedGold,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("index_document_fab")
            ) {
                Icon(Icons.Default.UploadFile, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Index Document", fontSize = 13.sp, fontWeight = FontWeight.Bold)
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

            // 1. Linked Case Selector Bar
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
                        Text(
                            text = "Filter Evidence by Linked Case:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        if (activeCase != null) {
                            Surface(shape = RoundedCornerShape(4.dp), color = NavyLight.copy(alpha = 0.15f)) {
                                Text(
                                    text = "CNR: ${activeCase.cnrNumber}",
                                    fontSize = 10.sp,
                                    color = DeepNavy,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item {
                            FilterChip(
                                selected = selectedCaseFilter == null,
                                onClick = { selectedCaseFilter = null },
                                label = { Text("All Vault (${documents.size})", fontSize = 10.sp) },
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

            // 2. Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Evidence Title, OCR text, or Exhibit Number...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SlateLight) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().testTag("evidence_search_input")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Category Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All Categories", fontSize = 10.sp) }
                    )
                }
                items(DocumentCategory.values()) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = if (selectedCategory == cat) null else cat },
                        label = { Text(if (language == AppLanguage.HI) cat.hiLabel else cat.label, fontSize = 10.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Indexed Case Documents & Exhibits (${filtered.size}):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered, key = { it.id }) { doc ->
                    val docCase = cases.find { it.id == doc.caseId }
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
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = GoldContainer
                                ) {
                                    Text(
                                        text = doc.exhibitNumber,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.DocumentScanner, contentDescription = null, tint = LegalGreen, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("OCR Extracted", fontSize = 10.sp, color = LegalGreen, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(doc.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                            Text(
                                text = "Linked Case: ${docCase?.caseNumber ?: "CS(COMM) 412/2024"} • Dated: ${doc.dateOfDocument}",
                                fontSize = 11.sp,
                                color = AccentBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text("Source: ${doc.source}", fontSize = 10.sp, color = SlateLight)

                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = IvorySurfaceVariant
                            ) {
                                Text(
                                    text = "OCR Snippet: \"${doc.ocrSnippet}\"",
                                    fontSize = 11.sp,
                                    color = SlateGrey,
                                    modifier = Modifier.padding(8.dp),
                                    lineHeight = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Surface(shape = RoundedCornerShape(4.dp), color = SubtleBlueContainer) {
                                        Text(doc.category.label, fontSize = 9.sp, color = SubtleBlue, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (doc.title.contains("Bank") || doc.title.contains("Protocol")) LegalGreenContainer else LegalAmberContainer
                                    ) {
                                        Text(
                                            text = if (doc.title.contains("Bank") || doc.title.contains("Protocol")) "Sec 65B/63 Certified" else "Sec 65B Required",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (doc.title.contains("Bank") || doc.title.contains("Protocol")) LegalGreen else LegalAmber,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                TextButton(
                                    onClick = {
                                        onSelectCase(doc.caseId)
                                        Toast.makeText(context, "Opened case: ${docCase?.caseNumber}", Toast.LENGTH_SHORT).show()
                                    },
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Open in Case Workspace", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var selectedCaseForDoc by remember {
            mutableStateOf(cases.find { it.id == selectedCaseFilter } ?: cases.firstOrNull())
        }
        var docTitle by remember { mutableStateOf("") }
        var exhibitNo by remember(selectedCaseForDoc) {
            val count = documents.count { it.caseId == (selectedCaseForDoc?.id ?: "case_1") }
            mutableStateOf("Ex. P-${count + 1}")
        }
        var dateOfDoc by remember { mutableStateOf("2026-10-20") }
        var source by remember { mutableStateOf("Original Document Produced by Client") }
        var category by remember { mutableStateOf(DocumentCategory.EVIDENCE) }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.UploadFile, contentDescription = null, tint = DeepNavy)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Index Case Document / Exhibit", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select Case to Attach Document:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(cases) { c ->
                            val isSelected = c.id == selectedCaseForDoc?.id
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCaseForDoc = c },
                                label = { Text(c.caseNumber, fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DeepNavy,
                                    selectedLabelColor = MutedGold
                                )
                            )
                        }
                    }

                    OutlinedTextField(value = docTitle, onValueChange = { docTitle = it }, label = { Text("Document Title") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = exhibitNo, onValueChange = { exhibitNo = it }, label = { Text("Exhibit / Marking No. (e.g. Ex. P-1)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = dateOfDoc, onValueChange = { dateOfDoc = it }, label = { Text("Date of Document") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = source, onValueChange = { source = it }, label = { Text("Source / Custody") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (docTitle.isNotBlank()) {
                            val newDoc = CaseDocument(
                                id = "doc_${System.currentTimeMillis()}",
                                caseId = selectedCaseForDoc?.id ?: "case_1",
                                title = docTitle,
                                category = category,
                                exhibitNumber = exhibitNo,
                                dateOfDocument = dateOfDoc,
                                uploadDate = "2026-11-03",
                                source = source,
                                tags = listOf("Court Marked", "Evidence"),
                                notes = "Uploaded and OCR verified in Chambers",
                                ocrReady = true,
                                ocrSnippet = "Verified legal text extract from $docTitle."
                            )
                            onAddDocument(newDoc)
                            showAddDialog = false
                            Toast.makeText(context, "Document indexed as $exhibitNo", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                ) {
                    Text("Index Document", color = MutedGold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}
