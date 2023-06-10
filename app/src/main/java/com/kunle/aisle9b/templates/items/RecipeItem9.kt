package com.kunle.aisle9b.templates.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kunle.aisle9b.navigation.GroceryScreens

@Composable
fun RecipeItem9(
    modifier: Modifier = Modifier,
    navController: NavController,
    id: Int,
    readyTimeInMinutes: Int,
    imageURL: String,
    name: String,
    source: String
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                navController.navigate(GroceryScreens.RecipeDetailsScreen.name + "/${id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .height(350.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = imageURL, contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    alignment = Alignment.Center
                )
            }
            Text(
                modifier = Modifier.padding(8.dp),
                text = name,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = source.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ready in $readyTimeInMinutes minutes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}