package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.model.CourtCase
import com.example.model.Hearing
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.DashboardMetrics

@Composable
fun DashboardScreen(
    metrics: DashboardMetrics,
    cases: List<CourtCase>,
    hearings: List<Hearing>,
    language: AppLanguage,
    onNavigate: (AppScreen) -> Unit,
    onSelectCase: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val todayHearings = hearings.filter { it.date == "2026-11-03" }
    val upcomingHearings = hearings.filter { it.date > "2026-11-03" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Legal Practice Welcome Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavy),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Adv. Rajesh Malhotra's Chambers",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MutedGold
                            )
                            Text(
                                text = "High Court of Delhi & Supreme Court Practice",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = NavyLight
                        ) {
                            Text(
                                text = "TODAY: 03 NOV 2026",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = IvoryBackground,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Statutory notice
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NavySurface
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MutedGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.get("ai_safeguard_notice", language),
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // Quick Action Bar (5 Core Workflow Actions)
        item {
            Column {
                Text(
                    text = if (language == AppLanguage.HI) "त्वरित क्रियाएं (Quick Actions)" else "Quick Actions",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionButton(
                        icon = Icons.Default.AddBox,
                        label = AppStrings.get("quick_add_case", language),
                        onClick = { onNavigate(AppScreen.CASES) },
                        modifier = Modifier.weight(1f).testTag("quick_action_add_case")
                    )
                    QuickActionButton(
                        icon = Icons.Default.EditNote,
                        label = AppStrings.get("quick_create_draft", language),
                        onClick = { onNavigate(AppScreen.DRAFTING_STUDIO) },
                        modifier = Modifier.weight(1f).testTag("quick_action_create_draft")
                    )
                    QuickActionButton(
                        icon = Icons.Default.EventAvailable,
                        label = AppStrings.get("quick_add_hearing", language),
                        onClick = { onNavigate(AppScreen.COURT_DIARY) },
                        modifier = Modifier.weight(1f).testTag("quick_action_add_hearing")
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionButton(
                        icon = Icons.Default.UploadFile,
                        label = AppStrings.get("quick_upload_doc", language),
                        onClick = { onNavigate(AppScreen.DOCUMENT_ROOM) },
                        modifier = Modifier.weight(1f).testTag("quick_action_upload_doc")
                    )
                    QuickActionButton(
                        icon = Icons.Default.AutoAwesome,
                        label = AppStrings.get("quick_open_ai", language),
                        onClick = { onNavigate(AppScreen.AI_ASSISTANT) },
                        modifier = Modifier.weight(1f).testTag("quick_action_open_ai"),
                        isGold = true
                    )
                }
            }
        }

        // Live Calculated Metrics Grid (Row 1 & Row 2)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricCard(
                        title = AppStrings.get("stat_active_cases", language),
                        value = metrics.activeCases.toString(),
                        subtitle = "Litigation portfolio",
                        icon = Icons.Default.FolderSpecial,
                        color = SubtleBlue,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.CASES) }
                    )
                    MetricCard(
                        title = AppStrings.get("stat_today_hearings", language),
                        value = metrics.todayHearingsCount.toString(),
                        subtitle = "Court appearance",
                        icon = Icons.Default.Gavel,
                        color = LegalRed,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.COURT_DIARY) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricCard(
                        title = AppStrings.get("stat_upcoming_hearings", language),
                        value = metrics.upcomingHearingsCount.toString(),
                        subtitle = "Next 7 days",
                        icon = Icons.Default.CalendarMonth,
                        color = SlateGrey,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.COURT_DIARY) }
                    )
                    MetricCard(
                        title = AppStrings.get("stat_filing_deadlines", language),
                        value = metrics.filingDeadlinesCount.toString(),
                        subtitle = "Limitation Act 1963",
                        icon = Icons.Default.HourglassBottom,
                        color = LegalAmber,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.LIMITATION_CALCULATOR) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricCard(
                        title = AppStrings.get("stat_pending_drafts", language),
                        value = metrics.pendingDraftsCount.toString(),
                        subtitle = "Under review",
                        icon = Icons.Default.Article,
                        color = DeepNavy,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.DRAFTING_STUDIO) }
                    )
                    MetricCard(
                        title = AppStrings.get("stat_outstanding_fees", language),
                        value = "₹${metrics.outstandingFeesInr.toInt()}",
                        subtitle = "Unpaid client invoices",
                        icon = Icons.Default.AccountBalanceWallet,
                        color = LegalGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.FEES_BILLING) }
                    )
                }
            }
        }

        // Today's Hearings Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(LegalRed)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.get("stat_today_hearings", language),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        }
                        TextButton(onClick = { onNavigate(AppScreen.COURT_DIARY) }) {
                            Text(
                                text = "View Cause List",
                                fontSize = 12.sp,
                                color = AccentBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (todayHearings.isEmpty()) {
                        Text(
                            text = "No court hearings listed for today.",
                            fontSize = 13.sp,
                            color = SlateGrey,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        todayHearings.forEach { hearing ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = LegalRedContainer.copy(alpha = 0.4f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        onSelectCase(hearing.caseId)
                                        onNavigate(AppScreen.COURTROOM_PREP)
                                    }
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = hearing.time,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LegalRed
                                        )
                                        Text(
                                            text = hearing.courtroom,
                                            fontSize = 11.sp,
                                            color = SlateGrey,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = hearing.caseTitle,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy
                                    )
                                    Text(
                                        text = "${hearing.caseNumber} • ${hearing.purpose}",
                                        fontSize = 12.sp,
                                        color = SlateGrey
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = {
                                                onSelectCase(hearing.caseId)
                                                onNavigate(AppScreen.COURTROOM_PREP)
                                            },
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Description,
                                                contentDescription = null,
                                                modifier = Modifier.size(13.dp),
                                                tint = MutedGold
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Open Court Brief",
                                                fontSize = 11.sp,
                                                color = Color.White
                                            )
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                onSelectCase(hearing.caseId)
                                                onNavigate(AppScreen.CASE_INTELLIGENCE)
                                            },
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "Case Intelligence",
                                                fontSize = 11.sp,
                                                color = DeepNavy
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

        // Active Litigation Portfolio
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.HI) "हाल के सक्रिय मुकदमे" else "Active Cases Portfolio",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
                TextButton(onClick = { onNavigate(AppScreen.CASES) }) {
                    Text(
                        text = "View All (${cases.size})",
                        fontSize = 12.sp,
                        color = AccentBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        items(cases.take(3)) { courtCase ->
            Card(
                colors = CardDefaults.cardColors(containerColor = IvorySurface),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSelectCase(courtCase.id)
                        onNavigate(AppScreen.CASE_INTELLIGENCE)
                    }
                    .testTag("dashboard_case_${courtCase.id}")
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
                                text = courtCase.category.displayName.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "CNR: ${courtCase.cnrNumber}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = SlateLight
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = courtCase.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Text(
                        text = "${courtCase.caseNumber} • ${courtCase.courtName}",
                        fontSize = 12.sp,
                        color = SlateGrey
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = null,
                                tint = LegalAmber,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Next: ${courtCase.nextHearingDate}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DeepNavy
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Open Case",
                            tint = SlateMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isGold: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isGold) GoldContainer else IvorySurface,
        border = CardDefaults.outlinedCardBorder(),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isGold) GoldDark else DeepNavy,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (isGold) GoldDark else DeepNavy,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = IvorySurface),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = SlateGrey,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = DeepNavy
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = SlateLight
            )
        }
    }
}
