package com.kunle.aisle9b.screens.meals

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.sharp.AddAPhoto
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.Instruction
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.models.MealNameUpdate
import com.kunle.aisle9b.models.MealNotesUpdate
import com.kunle.aisle9b.models.MealPicUpdate
import com.kunle.aisle9b.models.MealServingSizeUpdate
import com.kunle.aisle9b.models.MealWithIngredients
import com.kunle.aisle9b.models.TabItem
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditSummaryDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.IngredientHeadlineDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.IngredientsListDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.InstructionsListDialog9
import com.kunle.aisle9b.util.CameraXMode
import com.kunle.aisle9b.util.PhotoOptionsDialog9
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MealDetailsScreen(
    mealIndex: Int?,
    modifier: Modifier = Modifier,
    generalVM: GeneralVM = hiltViewModel(),
    mealVM: MealVM = hiltViewModel(),
) {
    generalVM.setTopBarOption(TopBarOptions.Back)
    generalVM.setClickSource(GroceryScreens.MealDetailsScreen)

    val scope = rememberCoroutineScope()

    if (mealIndex != null) {
        val mwi = mealVM.mealsWithIngredients.collectAsState().value[mealIndex]
        val mealInstructions = mealVM.instructions.collectAsState().value
            .filter {
                it.mealId == mwi.meal.mealId
            }
            .sortedBy {
                it.position
            }

        var editSummary by remember { mutableStateOf(false) }
        var editIngredients by remember { mutableStateOf(false) }
        var editInstructions by remember { mutableStateOf(false) }
        var editPicture by remember { mutableStateOf(false) }
        var shouldShowCamera by remember { mutableStateOf(false) }

        if (editIngredients) {
            IngredientsListDialog9(
                foodList = mwi.ingredients,
                mealVM = mealVM,
                updateFoodList = { newFood ->
                    scope.launch {
                        mealVM.upsertFood(newFood)
                        mealVM.insertPair(
                            MealFoodMap(
                                mealId = mwi.meal.mealId,
                                foodId = newFood.foodId
                            )
                        )
                    }
                },
                onSaveServingSizeClick = { servingSize ->
                    mealVM.updateServingSize(
                        MealServingSizeUpdate(
                            mealId = mwi.meal.mealId,
                            servingSize = servingSize
                        )
                    )
                },
                originalServingSize = mwi.meal.servingSize,
                setShowDialog = { editIngredients = false }
            )
        }

        if (editInstructions) {
            InstructionsListDialog9(
                mealInstructionList = mealInstructions,
                updatedInstruction = {
                    mealVM.reorganizeDBInstructions(
                        updatedInstruction = it,
                        oldInstructionList = mealInstructions
                    )
                    editInstructions = false
                },
                mealId = mwi.meal.mealId
            ) { editInstructions = false }
        }

        if (editSummary) {
            EditSummaryDialog9(
                meal = mwi.meal,
                updateMeal = { name, notes ->
                    mealVM.updateName(MealNameUpdate(mealId = mwi.meal.mealId, name = name))
                    mealVM.updateNotes(MealNotesUpdate(mealId = mwi.meal.mealId, notes = notes))
                    editSummary = false
                },
                setShowDialog = { editSummary = false })
        }

        if (editPicture) {
            PhotoOptionsDialog9(
                onImageCaptured = { Uri ->
                    mealVM.updatePic(
                        MealPicUpdate(
                            mealId = mwi.meal.mealId,
                            mealPic = Uri
                        )
                    )
                    editPicture = false
                },
                toggleCamera = { shouldShowCamera = it },
                deletePic = {
                    mealVM.updatePic(
                        MealPicUpdate(
                            mealId = mwi.meal.mealId,
                            mealPic = Uri.EMPTY
                        )
                    )
                    editPicture = false
                }
            ) {
                editPicture = false
            }
        }

        if (shouldShowCamera) {
            CameraXMode(
                onImageCaptured = { uri ->
                    mealVM.updatePic(MealPicUpdate(mealId = mwi.meal.mealId, mealPic = uri))
                    editPicture = false
                },
                toggleCamera = { shouldShowCamera = it })
        }

        Column(modifier = modifier.fillMaxSize()) {
            if (mwi.meal.mealPic == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.42f)
                        .padding(5.dp)
                        .clickable {
                            editPicture = true
                        }
                        .drawBehind {
                            drawRoundRect(
                                color = Color.Gray, style = Stroke(
                                    width = 5f, pathEffect = PathEffect.dashPathEffect(
                                        intervals =
                                        floatArrayOf(10f, 10f)
                                    )
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(65.dp),
                        imageVector = Icons.Sharp.AddAPhoto,
                        contentDescription = "Take picture button"
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.42f)
                        .clickable {
                            editPicture = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = mwi.meal.mealPic,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                }
            }
            Tabs(mwi = mwi,
                mealInstructions = mealInstructions,
                editSummary = { editSummary = true },
                editIngredients = { editIngredients = true },
                editInstructions = { editInstructions = true })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(
    mwi: MealWithIngredients,
    mealInstructions: List<Instruction>,
    editSummary: () -> Unit,
    editIngredients: () -> Unit,
    editInstructions: () -> Unit,
) {
    val pagerState = rememberPagerState { 4 }
    val coroutineScope = rememberCoroutineScope()

    val tabLabels = listOf(
        TabItem(
            title = "Notes",
            screen = {
                SummaryScreen(
                    mealName = mwi.meal.name,
                    notes = mwi.meal.notes
                ) {
                    editSummary()
                }
            }),
        TabItem(
            title = "Ingredients",
            screen = {
                IngredientsTab(mealAndIngredients = mwi) {
                    editIngredients()
                }
            }),
        TabItem(
            title = "Instructions",
            screen = {
                InstructionsScreen(mealInstructions = mealInstructions) {
                    editInstructions()
                }
            }),
    )

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        divider = { Divider(thickness = 4.dp) }
    ) {
        tabLabels.forEachIndexed { index, tab ->
            Tab(selected = index == pagerState.currentPage,
                text = { Text(text = tab.title) },
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        pagerState.animateScrollToPage(index)
                    }
                })
        }
    }
    HorizontalPager(state = pagerState) {
        tabLabels[pagerState.currentPage].screen()
    }
}

@Composable
fun SummaryScreen(
    mealName: String, notes: String,
    editSummary: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = mealName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { editSummary() },
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit Icon",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Text(
            text = notes,
            modifier = Modifier.verticalScroll(rememberScrollState()),
            fontSize = 14.sp,
        )
    }
}

@Composable
fun IngredientsTab(
    mealAndIngredients: MealWithIngredients,
    editIngredients: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Ingredients")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 20.sp
                        )
                    ) {
                        append(" for ${mealAndIngredients.meal.servingSize} servings")
                    }
                }
            )
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { editIngredients() },
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit Icon",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = mealAndIngredients.ingredients) {
                Text(text = "${it.name}: ${it.quantity}", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun InstructionsScreen(
    mealInstructions: List<Instruction>,
    editInstructions: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Preparation",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { editInstructions() },
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit Icon",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = mealInstructions) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = it.position.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = it.step, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}





