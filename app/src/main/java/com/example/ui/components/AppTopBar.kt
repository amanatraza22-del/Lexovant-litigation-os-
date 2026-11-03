package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentTitle: String,
    language: AppLanguage,
    demoModeActive: Boolean,
    onMenuClick: () -> Unit,
    onLanguageToggle: () -> Unit,
    onDemoToggle: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "LEXOVANT",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = MutedGold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LitigationOS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                Text(
                    text = currentTitle,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Normal
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.testTag("nav_menu_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Navigation Menu",
                    tint = Color.White
                )
            }
        },
        actions = {
            // Demo Mode Badge / Toggle
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (demoModeActive) MutedGold.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.1f),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onDemoToggle() }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                    .testTag("demo_mode_toggle")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (demoModeActive) MutedGold else SlateLight)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (demoModeActive) "DEMO" else "LIVE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (demoModeActive) MutedGold else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Persistent Language Selector (EN / हिन्दी)
            FilledTonalButton(
                onClick = onLanguageToggle,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = NavyLight,
                    contentColor = MutedGold
                ),
                modifier = Modifier
                    .height(34.dp)
                    .testTag("language_toggle_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = "Switch Language",
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (language == AppLanguage.EN) "हिन्दी" else "EN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.testTag("top_search_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Global Search",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DeepNavy,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        modifier = modifier
    )
}
