package com.kunle.aisle9b.screens

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.sharp.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import coil.compose.AsyncImage
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.models.tabs.TabItem
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditSummaryDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.IngredientsListDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.InstructionsListDialog9
import com.kunle.aisle9b.util.CameraXMode
import com.kunle.aisle9b.util.PhotoOptionsDialog9
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddMealScreenTest(
    modifier: Modifier = Modifier,
    mealVM: MealVM,
    sharedVM: SharedVM,
    topBar: (TopBarOptions) -> Unit,
    source: (GroceryScreens) -> Unit
) {
    topBar(TopBarOptions.Back)
    source(GroceryScreens.AddMealsScreenTEST)

    var meal by remember { mutableStateOf(Meal.createBlank()) }
    sharedVM.mealToBeSaved = meal

    val foodList = remember { mutableStateListOf<Food>() }
    sharedVM.foodListToBeSaved = foodList

    var instructionsList by remember { mutableStateOf(emptyList<Instruction>()) }
    val mealInstructions = remember(instructionsList) {
        instructionsList.sortedBy { it.position }
    }
    sharedVM.instructionsToBeSaved = mealInstructions

    var editSummary by remember { mutableStateOf(false) }
    var editIngredients by remember { mutableStateOf(false) }
    var editInstructions by remember { mutableStateOf(false) }
    var editPicture by remember { mutableStateOf(false) }
    var shouldShowCamera by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }


    if (editIngredients) {
        IngredientsListDialog9(
            foodList = foodList,
            originalServingSize = meal.servingSize,
            updateFoodList = { oldFood, newFood, command ->
                if (command == "Edit") {
                    foodList.remove(oldFood)
                }
                foodList.add(newFood)
            },
            onSaveServingSizeClick = { servingSize ->
                meal = Meal(
                    mealId = meal.mealId,
                    name = meal.name,
                    servingSize = servingSize,
                    mealPic = meal.mealPic,
                    notes = meal.notes
                )
            },
            sharedVM = sharedVM,
            setShowDialog = { editIngredients = false })
    }

    if (editInstructions) {
        InstructionsListDialog9(
            mealInstructionList = mealInstructions,
            updatedInstruction = { updatedInstruction, newPosition ->
                instructionsList =
                    mealVM.reorganizeTempInstructions(
                        instruction = updatedInstruction,
                        newPosition = newPosition,
                        instructions = mealInstructions
                    )
                editInstructions = false
            },
            mealId = meal.mealId,
            setShowDialog = { editInstructions = false })
    }

    if (editSummary) {
        EditSummaryDialog9(
            meal = meal,
            updateMeal = {
                meal = it
                editSummary = false
            },
            setShowDialog = { editSummary = false })
    }

    if (editPicture) {
        PhotoOptionsDialog9(
            onImageCaptured = { uri ->
                meal = Meal(
                    mealId = meal.mealId,
                    name = meal.name,
                    servingSize = meal.servingSize,
                    mealPic = uri,
                    notes = meal.notes
                )
                editPicture = false
            },
            toggleCamera = {
                editPicture = false
                shouldShowCamera = it
            },
            deletePic = {
                meal = Meal(
                    mealId = meal.mealId,
                    name = meal.name,
                    servingSize = meal.servingSize,
                    mealPic = null,
                    notes = meal.notes
                )
                editPicture = false
            }
        ) {
            editPicture = false
        }
    }

    if (shouldShowCamera) {
        CameraXMode(
            toggleCamera = { shouldShowCamera = it },
            onImageCaptured = { uri ->
                meal = Meal(
                    mealId = meal.mealId,
                    name = meal.name,
                    servingSize = meal.servingSize,
                    mealPic = uri,
                    notes = meal.notes
                )
            })
    }

    Column(modifier = modifier.fillMaxSize()) {
        if (meal.mealPic == null) {
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
                    model = meal.mealPic,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            }
        }
        Tabs(
            meal = meal,
            foodList = foodList,
            mealInstructions = mealInstructions,
            editSummary = { editSummary = true },
            editIngredients = { editIngredients = true },
            editInstructions = { editInstructions = true })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(
    meal: Meal,
    foodList: List<Food>,
    mealInstructions: List<Instruction>,
    editSummary: () -> Unit,
    editIngredients: () -> Unit,
    editInstructions: () -> Unit,
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val tabLabels = listOf(
        TabItem(
            title = "Notes",
            screen = {
                SummaryScreen(mealName = meal.name, notes = meal.notes) {
                    editSummary()
                }
            }),
        TabItem(
            title = "Ingredients",
            screen = {
                IngredientsTab(meal = meal, foodList = foodList) {
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
    HorizontalPager(pageCount = tabLabels.size, state = pagerState) {
        tabLabels[pagerState.currentPage].screen()
    }
}

@Composable
fun SummaryScreen(
    mealName: String,
    notes: String,
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
    meal: Meal,
    foodList: List<Food>,
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
                        append(" for ${meal.servingSize} servings")
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
            items(items = foodList) {
                Text(text = "${it.quantity} ${it.name}", fontSize = 14.sp)
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