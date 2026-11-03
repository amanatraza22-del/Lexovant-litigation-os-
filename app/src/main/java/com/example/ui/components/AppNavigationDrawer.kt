package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen

data class DrawerItem(
    val screen: AppScreen,
    val titleKey: String,
    val icon: ImageVector,
    val badge: String? = null
)

@Composable
fun AppNavigationDrawer(
    currentScreen: AppScreen,
    language: AppLanguage,
    onScreenSelected: (AppScreen) -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    ModalDrawerSheet(
        modifier = modifier.width(310.dp),
        drawerContainerColor = DeepNavy,
        drawerContentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(scrollState)
                .padding(bottom = 24.dp)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavySurface)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MutedGold,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Gavel,
                                    contentDescription = "Lexovant Gavel",
                                    tint = DeepNavy,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "LEXOVANT",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = MutedGold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "LitigationOS",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = AppStrings.get("tagline", language),
                        fontSize = 11.sp,
                        color = SlateMuted,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = NavyLight
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = MutedGold,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "India Jurisdiction • Supreme Court / High Courts",
                                fontSize = 10.sp,
                                color = IvoryBackground,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Section 1: CORE WORKSPACE
            DrawerSectionHeader("CORE PRACTICE")
            DrawerNavRow(
                item = DrawerItem(AppScreen.DASHBOARD, "nav_dashboard", Icons.Default.Dashboard),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
            DrawerNavRow(
                item = DrawerItem(AppScreen.CASES, "nav_cases", Icons.Default.Folder, "4"),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
            DrawerNavRow(
                item = DrawerItem(AppScreen.CASE_INTELLIGENCE, "nav_intelligence", Icons.Default.Psychology, "Flagship"),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
            DrawerNavRow(
                item = DrawerItem(AppScreen.COURT_DIARY, "nav_diary", Icons.Default.CalendarToday, "Today"),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )

            HorizontalDivider(
                color = NavyLight,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Section 2: AI & DRAFTING
            DrawerSectionHeader("LEGAL AI & DRAFTING")
            DrawerNavRow(
                item = DrawerItem(AppScreen.AI_ASSISTANT, "nav_ai_assistant", Icons.Default.AutoAwesome, "11 Workflows"),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
            DrawerNavRow(
                item = DrawerItem(AppScreen.DRAFTING_STUDIO, "nav_drafting", Icons.Default.EditNote),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
            DrawerNavRow(
                item = DrawerItem(AppScreen.LEGAL_RESEARCH, "nav_research", Icons.Default.MenuBook),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )

            HorizontalDivider(
                color = NavyLight,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Section 3: ADVOCATE WORKBENCH
            DrawerSectionHeader("PREPARATION & EVIDENCE")
            DrawerNavRow(
                item = DrawerItem(AppScreen.COURTROOM_PREP, "nav_court_prep", Icons.Default.Mic),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
            DrawerNavRow(
                item = DrawerItem(AppScreen.DOCUMENT_ROOM, "nav_documents", Icons.Default.Description),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
            DrawerNavRow(
                item = DrawerItem(AppScreen.LIMITATION_CALCULATOR, "nav_limitation", Icons.Default.HourglassBottom, "1963 Act"),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
            DrawerNavRow(
                item = DrawerItem(AppScreen.PDF_WORKSPACE, "nav_pdf_tools", Icons.Default.PictureAsPdf),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )

            HorizontalDivider(
                color = NavyLight,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Section 4: CHAMBERS & FIRM
            DrawerSectionHeader("PRACTICE & MANAGEMENT")
            DrawerNavRow(
                item = DrawerItem(AppScreen.CLIENT_MANAGEMENT, "nav_clients", Icons.Default.People),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
            DrawerNavRow(
                item = DrawerItem(AppScreen.FEES_BILLING, "nav_billing", Icons.Default.Payments, "₹ INR"),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
            DrawerNavRow(
                item = DrawerItem(AppScreen.TEAM_WORKSPACE, "nav_team", Icons.Default.Groups),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
            DrawerNavRow(
                item = DrawerItem(AppScreen.REPORTS, "nav_reports", Icons.Default.BarChart),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
            DrawerNavRow(
                item = DrawerItem(AppScreen.SUBSCRIPTION, "nav_subscription", Icons.Default.CardMembership),
                currentScreen = currentScreen,
                language = language,
                onSelected = { onScreenSelected(it); onCloseDrawer() }
            )
        }
    }
}

@Composable
private fun DrawerSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = SlateMuted,
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
    )
}

@Composable
private fun DrawerNavRow(
    item: DrawerItem,
    currentScreen: AppScreen,
    language: AppLanguage,
    onSelected: (AppScreen) -> Unit
) {
    val isSelected = currentScreen == item.screen
    val background = if (isSelected) NavyLight else Color.Transparent
    val contentColor = if (isSelected) MutedGold else IvoryBackground

    Surface(
        color = background,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelected(item.screen) }
            .testTag("nav_${item.screen.id}")
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(19.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = AppStrings.get(item.titleKey, language),
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = contentColor,
                modifier = Modifier.weight(1f)
            )
            if (item.badge != null) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) MutedGold else NavySurface
                ) {
                    Text(
                        text = item.badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) DeepNavy else MutedGold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
