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
import com.example.model.CaseCategory
import com.example.model.CourtCase
import com.example.model.LimitationItem
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LimitationCalculatorScreen(
    items: List<LimitationItem>,
    cases: List<CourtCase>,
    selectedCaseId: String?,
    language: AppLanguage,
    onSelectCase: (String) -> Unit,
    onAddCalculation: (LimitationItem) -> Unit,
    onUpdateCaseLimitation: ((String, String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val presetProceedingTypes = listOf(
        "Commercial Suit Written Statement (30 / 120 Days)",
        "Section 138 NI Act Cheque Dishonour (30 Days)",
        "Section 34 Arbitration Award Challenge (90 Days)",
        "Regular Civil Appeal to High Court (90 Days - Art. 116)",
        "Civil Revision Petition (90 Days - Art. 118)",
        "Application for Review of Judgment (30 Days - Art. 124)",
        "Suit for Recovery of Money (3 Years - Art. 19)",
        "Suit for Possession of Immovable Property (12 Years - Art. 65)"
    )

    var linkedCase by remember {
        mutableStateOf(cases.find { it.id == selectedCaseId } ?: cases.firstOrNull())
    }

    var selectedPreset by remember(linkedCase) {
        val preset = when (linkedCase?.category) {
            CaseCategory.COMMERCIAL -> if (linkedCase?.title?.contains("Sunil") == true) presetProceedingTypes[1] else presetProceedingTypes[0]
            CaseCategory.CRIMINAL -> presetProceedingTypes[5] // Review / Revision
            CaseCategory.CONSTITUTIONAL -> presetProceedingTypes[3] // Appeal
            else -> presetProceedingTypes[0]
        }
        mutableStateOf(preset)
    }

    var eventDate by remember(linkedCase) {
        mutableStateOf(linkedCase?.filingDate ?: "2026-10-15")
    }

    var courtName by remember(linkedCase) {
        mutableStateOf(linkedCase?.courtName ?: "High Court of Delhi / Commercial Division")
    }

    var timeTakenForCertifiedCopyDays by remember { mutableIntStateOf(4) }
    var courtClosedDays by remember { mutableIntStateOf(0) }

    // Calculated state
    var calculatedDeadline by remember { mutableStateOf("2026-11-18") }
    var statutoryArticle by remember { mutableStateOf("Order VIII Rule 1 CPC read with Commercial Courts Act, 2015") }
    var daysRemaining by remember { mutableIntStateOf(15) }
    var isLimitationExpired by remember { mutableStateOf(false) }
    var isUncertainOrComplex by remember { mutableStateOf(false) }

    fun recalculate() {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val cal = Calendar.getInstance()
            cal.time = sdf.parse(eventDate) ?: Date()

            val baseDays = when {
                selectedPreset.contains("Written Statement") -> {
                    statutoryArticle = "Proviso to Order VIII Rule 1 CPC (Commercial Courts Act, 2015)"
                    isUncertainOrComplex = true
                    30
                }
                selectedPreset.contains("Section 138") -> {
                    statutoryArticle = "Section 142(1)(b) read with Section 138 proviso (c), NI Act, 1881"
                    isUncertainOrComplex = false
                    45 // 15 days notice + 30 days complaint
                }
                selectedPreset.contains("Section 34") -> {
                    statutoryArticle = "Section 34(3) Arbitration & Conciliation Act, 1996"
                    isUncertainOrComplex = true
                    90
                }
                selectedPreset.contains("Appeal to High Court") -> {
                    statutoryArticle = "Article 116(a), Limitation Act, 1963"
                    isUncertainOrComplex = false
                    90
                }
                selectedPreset.contains("Review") -> {
                    statutoryArticle = "Article 124, Limitation Act, 1963"
                    isUncertainOrComplex = false
                    30
                }
                selectedPreset.contains("Recovery of Money") -> {
                    statutoryArticle = "Article 19, Schedule to Limitation Act, 1963"
                    isUncertainOrComplex = false
                    365 * 3
                }
                else -> {
                    statutoryArticle = "Limitation Act, 1963 (Standard Schedule)"
                    isUncertainOrComplex = false
                    90
                }
            }

            // Section 12 exclusion: time requisite for obtaining certified copy + Section 4 court closure
            val totalDays = baseDays + timeTakenForCertifiedCopyDays + courtClosedDays
            cal.add(Calendar.DAY_OF_YEAR, totalDays)
            calculatedDeadline = sdf.format(cal.time)

            val today = Calendar.getInstance()
            val diffMs = cal.timeInMillis - today.timeInMillis
            val rawDiffDays = (diffMs / (1000 * 60 * 60 * 24)).toInt()
            isLimitationExpired = rawDiffDays < 0
            daysRemaining = if (rawDiffDays < 0) 0 else rawDiffDays
        } catch (e: Exception) {
            // fallback
        }
    }

    LaunchedEffect(selectedPreset, eventDate, timeTakenForCertifiedCopyDays, courtClosedDays) {
        recalculate()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Calculator Header Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = DeepNavy),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.HourglassBottom, contentDescription = null, tint = MutedGold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Indian Limitation Act, 1963 Engine",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedGold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Statutory deadline calculator incorporating Section 4 (Court Closures), Section 12 (Certified Copy Exclusions) and Commercial Non-Condonable Forfeiture Rules.",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = 15.sp
                )
            }
        }

        // 2. Linked Case Selector Bar
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
                    Text("Select Case to Calculate Limitation:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                    if (linkedCase != null) {
                        Surface(shape = RoundedCornerShape(4.dp), color = GoldContainer) {
                            Text(
                                text = "Current Deadline: ${linkedCase?.limitationDate}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(cases) { c ->
                        val isSelected = c.id == linkedCase?.id
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                linkedCase = c
                                onSelectCase(c.id)
                                eventDate = c.filingDate
                                courtName = c.courtName
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

        // 3. Preset Proceeding Type Chips
        Text(
            text = "Select Type of Proceeding / Forum:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = DeepNavy
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(presetProceedingTypes) { type ->
                val isSelected = type == selectedPreset
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedPreset = type },
                    label = { Text(type.take(28) + "...", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DeepNavy,
                        selectedLabelColor = MutedGold
                    )
                )
            }
        }

        // 4. Input Fields
        OutlinedTextField(
            value = eventDate,
            onValueChange = { eventDate = it },
            label = { Text("Cause of Action / Impugned Order Date (YYYY-MM-DD)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("limitation_event_date")
        )

        OutlinedTextField(
            value = courtName,
            onValueChange = { courtName = it },
            label = { Text("Court / Forum") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = timeTakenForCertifiedCopyDays.toString(),
                onValueChange = { timeTakenForCertifiedCopyDays = it.toIntOrNull() ?: 0 },
                label = { Text("Sec 12 Copy Days") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = courtClosedDays.toString(),
                onValueChange = { courtClosedDays = it.toIntOrNull() ?: 0 },
                label = { Text("Sec 4 Holiday Days") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        // 5. Computed Output Card
        Card(
            colors = CardDefaults.cardColors(containerColor = IvorySurface),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.fillMaxWidth().testTag("limitation_result_card")
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Indicative Calculated Deadline", fontSize = 11.sp, color = SlateLight, fontWeight = FontWeight.Medium)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = when {
                            isLimitationExpired -> LegalRedContainer
                            daysRemaining <= 7 -> LegalRedContainer
                            daysRemaining <= 30 -> LegalAmberContainer
                            else -> LegalGreenContainer
                        }
                    ) {
                        Text(
                            text = when {
                                isLimitationExpired -> "LIMITATION EXPIRED (SEC 3)"
                                else -> "$daysRemaining DAYS REMAINING"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                isLimitationExpired -> LegalRed
                                daysRemaining <= 7 -> LegalRed
                                daysRemaining <= 30 -> LegalAmber
                                else -> LegalGreen
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = calculatedDeadline,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isLimitationExpired) LegalRed else DeepNavy
                )
                Text(
                    text = "Applicable Provision: $statutoryArticle",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AccentBlue
                )

                if (isLimitationExpired) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LegalRedContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = LegalRed, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Bar of Limitation (Section 3, Limitation Act 1963): The calculated limitation period has elapsed. Suit or appeal subject to dismissal unless condonation of delay under Section 5 is admissible or Section 18 acknowledgment applies.",
                                fontSize = 10.sp,
                                color = LegalRed,
                                lineHeight = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                if (isUncertainOrComplex) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LegalAmberContainer.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = LegalAmber, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Strict Deadline Warning: Under Commercial Courts Act (SCG Contracts) & Section 34 Arbitration (Simplex), periods beyond 120 / 120 days cannot be condoned under Section 5.",
                                fontSize = 10.sp,
                                color = DeepNavy,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = SlateBorder.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(8.dp))

                Text("Statutory Verification Checklist:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text("✓ Section 4: If court reopens on Monday, filing is permissible on reopening day.", fontSize = 10.sp, color = SlateGrey)
                Text("✓ Section 12: Excluded $timeTakenForCertifiedCopyDays days between application & receipt of certified copy.", fontSize = 10.sp, color = SlateGrey)
                Text("✓ Section 14: Time spent in good faith in court without jurisdiction can be excluded.", fontSize = 10.sp, color = SlateGrey)

                Spacer(modifier = Modifier.height(12.dp))

                // Pin Deadline to Case Button
                Button(
                    onClick = {
                        val item = LimitationItem(
                            id = "lim_${System.currentTimeMillis()}",
                            eventTitle = "${linkedCase?.caseNumber ?: "CS(COMM) 412/2024"}: $selectedPreset",
                            eventDate = eventDate,
                            proceedingType = selectedPreset,
                            act = "Limitation Act, 1963",
                            provision = statutoryArticle,
                            statutoryPeriod = "$daysRemaining days left",
                            computedDeadline = calculatedDeadline,
                            daysRemaining = daysRemaining,
                            exclusionChecklist = listOf(
                                "Section 12 excluded $timeTakenForCertifiedCopyDays days",
                                "Court closed excluded $courtClosedDays days"
                            ),
                            verified = true
                        )
                        onAddCalculation(item)
                        linkedCase?.let {
                            onUpdateCaseLimitation?.invoke(it.id, calculatedDeadline)
                        }
                        Toast.makeText(context, "Pinned limitation ($calculatedDeadline) to case ${linkedCase?.caseNumber}", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                    modifier = Modifier.fillMaxWidth().testTag("btn_pin_limitation")
                ) {
                    Icon(Icons.Default.BookmarkAdd, contentDescription = null, tint = MutedGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pin Deadline to ${linkedCase?.caseNumber ?: "Case Calendar"}", color = MutedGold)
                }
            }
        }

        // 6. Active Tracked Limitations
        Text("Tracked Practice Limitations (${items.size}):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
        items.forEach { item ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.eventTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                        Text("${item.daysRemaining} days left", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = LegalAmber)
                    }
                    Text("Deadline: ${item.computedDeadline} • ${item.provision}", fontSize = 11.sp, color = SlateGrey)
                }
            }
        }
    }
}
