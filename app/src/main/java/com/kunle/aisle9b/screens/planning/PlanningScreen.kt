package com.kunle.aisle9b.screens.planning

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AddAPhoto
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.screens.meals.MealVM

@Composable
fun PlanningScreen(
    viewModel: MealVM
) {
    val listState = rememberLazyListState()
    val mealList = viewModel.visibleMealList.collectAsState().value
    var daySelected: Int? by remember { mutableStateOf(null) }
    val itemWidth = LocalConfiguration.current.screenWidthDp * .95

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        DaySelect(
            onDaySelect = { daySelected = it })

        LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(mealList) {
                PlanningItem(it, itemWidth.dp)
            }

        }
    }


}

@Preview
@Composable
fun PlanningItem(meal: Meal = Meal.createBlank(), screenWidth: Dp = 325.dp) {
    val bottomFade = Brush.verticalGradient(
        0F to Color.Transparent,
        .5F to Color.Black.copy(alpha = 0.5F),
        1F to Color.Black.copy(alpha = 0.8F)
    )

    Column(modifier = Modifier.size(screenWidth)) {
        Card {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(text = "Breakfast")
                    Text(text = "Set Schedule")
                }
                Icon(
                    imageVector = Icons.Sharp.AddAPhoto,
                    contentDescription = "Take picture button"
                )
            }

        }
        Card(
            modifier = Modifier.size(screenWidth),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(5),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = meal.mealPic,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
                Text(
                    text = meal.name,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(bottomFade)
                        .padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    lineHeight = 30.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun DaySelect(
    foodList: List<String> = emptyList(),
    onDaySelect: (Int?) -> Unit = {},
    onWeekChange: () -> Int? = { null }
) {
    Card() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "This Week", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "November 27, 2023")
            }
            Row(
                modifier = Modifier.fillMaxWidth(.9f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(times = 7) { currentNum ->
                    Surface(
                        shape = RoundedCornerShape(40),
                        border = BorderStroke(width = Dp.Hairline, color = Color.Black),
                        modifier = Modifier
                            .padding(horizontal = 2.dp, vertical = 4.dp)
                            .size(30.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = (currentNum + 1).toString(),
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }


}