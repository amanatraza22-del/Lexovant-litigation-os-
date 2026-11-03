package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.model.SubscriptionPlan
import com.example.ui.theme.*

@Composable
fun SubscriptionScreen(
    currentPlan: SubscriptionPlan,
    language: AppLanguage,
    onSelectPlan: (SubscriptionPlan) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val plans = SubscriptionPlan.values()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavy),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("LEXOVANT LITIGATIONOS PLANS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MutedGold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Commercial Subscription Workspace", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Configurable legal practice plans designed for independent advocates, seniors and law firms in India.", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(shape = RoundedCornerShape(4.dp), color = NavyLight) {
                        Text(
                            text = "Current Active Tier: ${currentPlan.title.uppercase()} (Active Practice)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedGold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        item {
            Text("Available Practice Plans & Capacity Limits:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
        }

        items(plans) { plan ->
            val isCurrent = plan == currentPlan
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrent) IvorySurface else IvorySurface
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrent) 3.dp else 1.dp),
                border = if (isCurrent) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MutedGold), width = 2.dp) else null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(plan.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                            Text(plan.maxCases, fontSize = 11.sp, color = SlateLight)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (plan.priceMonthlyInr == 0) "Free" else "₹${plan.priceMonthlyInr}/mo",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isCurrent) MutedGold else DeepNavy
                            )
                            if (isCurrent) {
                                Surface(shape = RoundedCornerShape(4.dp), color = LegalGreenContainer) {
                                    Text("CURRENT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = LegalGreen, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(plan.description, fontSize = 11.sp, color = SlateGrey)

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = SlateBorder.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Included Features:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                    plan.features.forEach { feat ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = LegalGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(feat, fontSize = 11.sp, color = DeepNavy)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    if (!isCurrent) {
                        Button(
                            onClick = {
                                onSelectPlan(plan)
                                Toast.makeText(context, "Upgraded to ${plan.title} plan (Configurable Tier)", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Switch to ${plan.title}", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
