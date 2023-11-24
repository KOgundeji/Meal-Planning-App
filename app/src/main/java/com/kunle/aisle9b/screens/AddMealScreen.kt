package com.kunle.aisle9b.screens

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.meals.IngredientResponse
import com.kunle.aisle9b.screens.meals.MealResponse
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.screens.utilScreens.ErrorScreen
import com.kunle.aisle9b.screens.utilScreens.LoadingScreen
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditIngredientDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditInstructionsDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditSummaryDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.IngredientHeadlineDialog9
import com.kunle.aisle9b.templates.items.mealItems.IngredientItem9
import com.kunle.aisle9b.templates.items.mealItems.InstructionItem
import com.kunle.aisle9b.util.CameraXMode
import com.kunle.aisle9b.util.DropActions
import com.kunle.aisle9b.util.PhotoOptionsDialog9
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddMealScreenGate(
    modifier: Modifier = Modifier,
    mealVM: MealVM = hiltViewModel(),
    generalVM: GeneralVM = hiltViewModel()
) {
    generalVM.setTopBarOption(TopBarOptions.Back)
    generalVM.setClickSource(GroceryScreens.AddNewMealScreen)

    LaunchedEffect(key1 = Unit) {
        mealVM.getBrandNewMeal()
    }

    when (val retrievedMealState = mealVM.createdNewMealState.collectAsState().value) {
        is MealResponse.Error -> ErrorScreen(errorText = retrievedMealState.getMessage())
        is MealResponse.Loading -> LoadingScreen()
        is MealResponse.Success -> {
            AddMealScreen(
                modifier = modifier,
                createdMeal = retrievedMealState.meal,
                mealVM = mealVM,
                generalVM
            )
        }

        MealResponse.Neutral -> {}
    }
}


@Composable
fun AddMealScreen(
    modifier: Modifier = Modifier,
    createdMeal: Meal,
    mealVM: MealVM,
    generalVM: GeneralVM
) {
    val meal = mealVM.allMeals.collectAsState().value.find { it.mealId == createdMeal.mealId }

    if (meal != null) {
        generalVM.setNewMealToBeVisible(meal)
        val scope = rememberCoroutineScope()
        val mealId = meal.mealId

        val instructionsList = mealVM.instructions.collectAsState().value
        val mwiList = mealVM.mealsWithIngredients.collectAsState().value

        val mealInstructions = remember(instructionsList) {
            instructionsList
                .filter { it.mealId == mealId }
                .sortedBy { it.position }
        }

        val ingredientList = remember(mwiList) {
            mwiList.first { it.meal.mealId == mealId }
                .ingredients
        }

        val newInstructionPosition =
            if (mealInstructions.isNotEmpty()) {
                mealInstructions.last().position + 1
            } else {
                1
            }

        var editSummary by remember { mutableStateOf(false) }
        var modifyServingSize by remember { mutableStateOf(false) }
        var addNewInstruction by remember { mutableStateOf(false) }
        var editPicture by remember { mutableStateOf(false) }
        var shouldShowCamera by remember { mutableStateOf(false) }
        var bitmap by remember { mutableStateOf<Bitmap?>(null) }


        if (addNewInstruction) {
            val brandNewInstruction = Instruction.createBlank(mealId, newInstructionPosition)
            EditInstructionsDialog9(
                instruction = brandNewInstruction,
                exitDialog = { addNewInstruction = false },
                deleteInstruction = {}, //instruction doesn't exist yet. No need to delete
                updatedInstruction = { instruction ->
                    mealVM.upsertInstruction(instruction)
                    addNewInstruction = false
                }
            )
        }

        if (modifyServingSize) {
            IngredientHeadlineDialog9(
                originalServingSize = createdMeal.servingSize,
                onSaveServingSize = {
                    mealVM.updateServingSize(
                        MealServingSizeUpdate(
                            mealId = mealId,
                            servingSize = it
                        )
                    )
                    modifyServingSize = false
                },
                closeDialog = { modifyServingSize = false })
        }

        if (editSummary) {
            EditSummaryDialog9(
                meal = meal,
                updateMeal = { name, notes ->
                    mealVM.updateName(MealNameUpdate(mealId = mealId, name = name))
                    mealVM.updateNotes(MealNotesUpdate(mealId = mealId, notes = notes))
                    editSummary = false
                },
                setShowDialog = { editSummary = false })
        }

        if (editPicture) {
            PhotoOptionsDialog9(
                onImageCaptured = { uri ->
                    mealVM.updatePic(MealPicUpdate(mealId = mealId, mealPic = uri))
                    editPicture = false
                },
                toggleCamera = {
                    editPicture = false
                    shouldShowCamera = it
                },
                deletePic = {
                    mealVM.updatePic(MealPicUpdate(mealId = mealId, mealPic = Uri.EMPTY))
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
                    mealVM.updatePic(MealPicUpdate(mealId = mealId, mealPic = uri))
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
                        model = createdMeal.mealPic,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                }
            }
            Tabs(
                meal = meal,
                mealVM = mealVM,
                ingredientList = ingredientList,
                mealInstructions = mealInstructions,
                editSummary = { editSummary = true },
                deleteIngredient = {
                    scope.launch {
                        mealVM.deleteFood(it)
                    }
                },
                updateIngredient = {
                    scope.launch {
                        mealVM.upsertFood(it)
                    }
                },
                updateServingSize = { modifyServingSize = true },
                addInstruction = { addNewInstruction = true },
                deleteInstruction = {
                    mealVM.deleteInstruction(it)
                    mealVM.reorderRestOfInstructionList(oldPosition = it.position)
                },
                updateInstruction = { mealVM.upsertInstruction(it) },
                swappedInstructions = { original, moved ->
                    mealVM.upsertInstruction(original)
                    mealVM.upsertInstruction(moved)
                })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(
    meal: Meal,
    mealVM: MealVM,
    ingredientList: List<Food>,
    mealInstructions: List<Instruction>,
    editSummary: () -> Unit,
    deleteIngredient: (Food) -> Unit,
    updateIngredient: (Food) -> Unit,
    updateServingSize: () -> Unit,
    addInstruction: () -> Unit,
    deleteInstruction: (Instruction) -> Unit,
    updateInstruction: (Instruction) -> Unit,
    swappedInstructions: (Instruction, Instruction) -> Unit
) {
    val pagerState = rememberPagerState { 4 }
    val coroutineScope = rememberCoroutineScope()

    val tabLabels = listOf(
        TabItem(
            title = "Notes",
            screen = {
                SummaryScreen(
                    mealName = meal.name,
                    notes = meal.notes,
                    editSummary = { editSummary() }
                )
            }),
        TabItem(
            title = "Ingredients",
            screen = {
                IngredientsTabGate(
                    meal = meal,
                    mealVM = mealVM,
                    ingredientList = ingredientList,
                    deleteIngredients = { deleteIngredient(it) },
                    updateIngredient = { updateIngredient(it) },
                    updateServingSize = { updateServingSize() })
            }),
        TabItem(
            title = "Instructions",
            screen = {
                InstructionsScreen(
                    mealInstructions = mealInstructions,
                    addInstructions = { addInstruction() },
                    deleteInstruction = { deleteInstruction(it) },
                    updatedInstruction = { updateInstruction(it) },
                    swappedInstructions = { original, moved ->
                        swappedInstructions(original, moved)
                    })
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
                fontSize = 24.sp,
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
fun IngredientsTabGate(
    meal: Meal,
    mealVM: MealVM,
    ingredientList: List<Food>,
    deleteIngredients: (Food) -> Unit,
    updateIngredient: (Food) -> Unit,
    updateServingSize: () -> Unit
) {

    var triggerFood by remember { mutableStateOf<Food?>(null) }

    if (triggerFood != null) {
        LaunchedEffect(key1 = triggerFood) {
            mealVM.getIngredientState(triggerFood, meal.mealId)
            triggerFood = null
        }
    }

    when (val ingredientListState = mealVM.ingredientResponse.collectAsState().value) {
        is IngredientResponse.Error -> ErrorScreen(errorText = ingredientListState.getMessage())
        is IngredientResponse.Success, is IngredientResponse.Neutral -> {
            IngredientsTab(
                meal = meal,
                ingredientList = ingredientList,
                addIngredient = { triggerFood = it },
                deleteIngredients = deleteIngredients,
                updateIngredient = updateIngredient,
                updateServingSize = { updateServingSize() }
            )
        }

        else -> {}
    }
}

@Composable
private fun IngredientsTab(
    meal: Meal,
    ingredientList: List<Food>,
    addIngredient: (Food) -> Unit,
    deleteIngredients: (Food) -> Unit,
    updateIngredient: (Food) -> Unit,
    updateServingSize: () -> Unit
) {
    var addNewIngredient by remember { mutableStateOf(false) }

    if (addNewIngredient) {
        val newIngredient = Food.createBlank()
        EditIngredientDialog9(
            oldFood = newIngredient,
            closeDialog = { addNewIngredient = false },
            deleteIngredient = {
                addNewIngredient = false
            }, //ingredient doesn't exist yet. No need to delete
            updateIngredient = { food ->
                Log.d("Test", "ingredient added from dialog")
                addIngredient(food)
                addNewIngredient = false
            })
    }



    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable {
                        updateServingSize()
                    },
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Ingredients")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 24.sp
                        )
                    ) {
                        append(" for ${meal.servingSize} servings")
                    }
                }
            )
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { addNewIngredient = true },
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add Icon",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = ingredientList) {
                IngredientItem9(
                    ingredient = it,
                    deleteIngredient = { deleteIngredients(it) },
                    updatedIngredient = { updatedIngredient ->
                        updateIngredient(updatedIngredient)
                    })
            }
        }
    }
}

@Composable
fun InstructionsScreen(
    mealInstructions: List<Instruction>,
    addInstructions: () -> Unit,
    deleteInstruction: (Instruction) -> Unit,
    updatedInstruction: (Instruction) -> Unit,
    swappedInstructions: (Instruction, Instruction) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Preparation",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { addInstructions() },
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add Icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = mealInstructions) { oldInstruction ->
                InstructionItem(
                    instruction = oldInstruction,
                    deleteInstruction = { deleteInstruction(oldInstruction) },
                    positionChange = { dropAction ->

                        when (dropAction) {
                            DropActions.MoveUp -> {
                                if (oldInstruction.position > 1) {
                                    val movedInstruction1 =
                                        mealInstructions.find { it.position == (oldInstruction.position - 1) }

                                    val originalInstruction = Instruction(
                                        instructionId = oldInstruction.instructionId,
                                        step = oldInstruction.step,
                                        mealId = oldInstruction.mealId,
                                        position = oldInstruction.position - 1
                                    )

                                    val movedInstruction = Instruction(
                                        instructionId = movedInstruction1!!.instructionId,
                                        step = movedInstruction1.step,
                                        mealId = movedInstruction1.mealId,
                                        position = movedInstruction1.position + 1
                                    )
                                    swappedInstructions(
                                        originalInstruction,
                                        movedInstruction
                                    )
                                }
                            }

                            DropActions.MoveDown -> {
                                if (oldInstruction.position < mealInstructions.last().position) {
                                    val movedInstruction1 =
                                        mealInstructions.find { it.position == (oldInstruction.position + 1) }

                                    val originalInstruction = Instruction(
                                        instructionId = oldInstruction.instructionId,
                                        step = oldInstruction.step,
                                        mealId = oldInstruction.mealId,
                                        position = oldInstruction.position + 1
                                    )

                                    val movedInstruction = Instruction(
                                        instructionId = movedInstruction1!!.instructionId,
                                        step = movedInstruction1.step,
                                        mealId = movedInstruction1.mealId,
                                        position = movedInstruction1.position - 1
                                    )
                                    swappedInstructions(
                                        originalInstruction,
                                        movedInstruction
                                    )
                                }
                            }

                            DropActions.Delete -> {
                                deleteInstruction(oldInstruction)
                            }

                            else -> {}
                        }
                    }) { instruction ->
                    updatedInstruction(instruction)
                }
                Spacer(modifier = Modifier.height(5.dp))
                Divider()
            }
        }
    }
}