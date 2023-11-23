package com.kunle.aisle9b.screens

import android.graphics.Bitmap
import android.net.Uri
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
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditInstructionsDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditSummaryDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.IngredientsListDialog9
import com.kunle.aisle9b.templates.items.mealItems.InstructionItem
import com.kunle.aisle9b.util.CameraXMode
import com.kunle.aisle9b.util.DropActions
import com.kunle.aisle9b.util.PhotoOptionsDialog9
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddMealScreenTest(
    modifier: Modifier = Modifier,
    mealVM: MealVM = hiltViewModel(),
    generalVM: GeneralVM = hiltViewModel()
) {
    generalVM.setTopBarOption(TopBarOptions.Back)
    generalVM.setClickSource(GroceryScreens.AddNewMealScreen)

    val scope = rememberCoroutineScope()

    val meal = Meal.createBlank()
    val mealId = remember { mealVM.insertMeal(meal) }
    val mwi = remember(key1 = mealVM.mealsWithIngredients.collectAsState().value) {
        mealVM.findMWI(mealId) ?: MealWithIngredients(meal = meal, ingredients = emptyList())
    }
    val instructionsList = mealVM.instructions.collectAsState().value

    val mealInstructions = remember(instructionsList) {
        instructionsList.sortedBy { it.position }
    }

    val newInstructionPosition =
        if (mealInstructions.isNotEmpty()) {
            mealInstructions.last().position + 1
        } else {
            1
        }
//    generalVM.instructionsToBeSaved = mealInstructions

    var editSummary by remember { mutableStateOf(false) }
    var editIngredients by remember { mutableStateOf(false) }
    var addNewInstruction by remember { mutableStateOf(false) }
    var editPicture by remember { mutableStateOf(false) }
    var shouldShowCamera by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    if (editIngredients) {
        IngredientsListDialog9(
            foodList = mwi.ingredients,
            originalServingSize = mwi.meal.servingSize,
            mealVM = mealVM,
            updateFoodList = { newFood ->
                scope.launch { mealVM.upsertFood(food = newFood) }
            },
            onSaveServingSizeClick = { servingSize ->
                mealVM.updateServingSize(
                    MealServingSizeUpdate(
                        mealId = mealId,
                        servingSize = servingSize
                    )
                )
            },
            setShowDialog = { editIngredients = false })
    }

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

    if (editSummary) {
        EditSummaryDialog9(
            meal = mwi.meal,
            updateMeal = {
                mealVM.upsertMeal(it)
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
        Tabs(
            meal = mwi.meal,
            ingredientList = mwi.ingredients,
            mealInstructions = mealInstructions,
            editSummary = { editSummary = true },
            editIngredients = { editIngredients = true },
            addInstructions = { addNewInstruction = true },
            deleteInstruction = {
                mealVM.deleteInstruction(it)
                mealVM.reorderRestOfInstructionList(oldPosition = it.position)
            },
            updatedInstruction = { mealVM.upsertInstruction(it) },
            swappedInstructions = { original, moved ->
                mealVM.upsertInstruction(original)
                mealVM.upsertInstruction(moved)
            })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(
    meal: Meal,
    ingredientList: List<Food>,
    mealInstructions: List<Instruction>,
    editSummary: () -> Unit,
    editIngredients: () -> Unit,
    addInstructions: () -> Unit,
    deleteInstruction: (Instruction) -> Unit,
    updatedInstruction: (Instruction) -> Unit,
    swappedInstructions: (Instruction, Instruction) -> Unit
) {
    val pagerState = rememberPagerState { 4 }
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
                IngredientsTab(meal = meal, ingredientList = ingredientList) {
                    editIngredients()
                }
            }),
        TabItem(
            title = "Instructions",
            screen = {
                InstructionsScreen(
                    mealInstructions = mealInstructions,
                    addInstructions = { addInstructions() },
                    deleteInstruction = { deleteInstruction(it) },
                    updatedInstruction = { updatedInstruction(it) }) { original, moved ->
                    swappedInstructions(original, moved)
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
fun IngredientsTab(
    meal: Meal,
    ingredientList: List<Food>,
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
            items(items = ingredientList) {
                Text(text = "${it.name}: ${it.quantity}", fontSize = 14.sp)
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
                                    swappedInstructions(originalInstruction, movedInstruction)
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
                                    swappedInstructions(originalInstruction, movedInstruction)
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