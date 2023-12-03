package com.kunle.aisle9b.screens.meals

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Instruction
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.TabItem
import com.kunle.aisle9b.screens.utilScreens.ErrorScreen
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditIngredientDialog9
import com.kunle.aisle9b.templates.items.mealItems.IngredientItem9
import com.kunle.aisle9b.templates.items.mealItems.InstructionItem
import com.kunle.aisle9b.util.DropActions
import com.kunle.aisle9b.util.IngredientResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(
    fullMealSet: MealVM.FullMealSet,
    ingredientState: IngredientResponse,
    editSummary: () -> Unit,
    addNewIngredient: (Food?, Long) -> Unit,
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
                    meal = fullMealSet.meal,
                    editSummary = { editSummary() }
                )
            }),
        TabItem(
            title = "Ingredients",
            screen = {
                IngredientsTabGate(
                    fullMealSet = fullMealSet,
                    ingredientState = ingredientState,
                    addNewIngredient = { ingredient, mealId ->
                        addNewIngredient(
                            ingredient,
                            mealId
                        )
                    },
                    deleteIngredients = { deleteIngredient(it) },
                    updateIngredient = { updateIngredient(it) },
                    updateServingSize = { updateServingSize() })
            }),
        TabItem(
            title = "Instructions",
            screen = {
                InstructionsScreen(
                    mealInstructions = fullMealSet.instructions,
                    addInstruction = { addInstruction() },
                    deleteInstruction = { deleteInstruction(it) },
                    updateInstruction = { updateInstruction(it) },
                    swapInstructions = { original, moved ->
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
    meal: Meal,
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
                text = meal.name,
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
            text = meal.notes,
            modifier = Modifier.verticalScroll(rememberScrollState()),
            fontSize = 14.sp,
        )
    }
}

@Composable
fun IngredientsTabGate(
    fullMealSet: MealVM.FullMealSet,
    ingredientState: IngredientResponse,
    addNewIngredient: (Food?, Long) -> Unit,
    deleteIngredients: (Food) -> Unit,
    updateIngredient: (Food) -> Unit,
    updateServingSize: () -> Unit
) {

    var triggerFood by remember { mutableStateOf<Food?>(null) }

    if (triggerFood != null) {
        LaunchedEffect(key1 = triggerFood) {
            addNewIngredient(triggerFood, fullMealSet.meal.mealId)
            triggerFood = null
        }
    }

    when (ingredientState) {
        is IngredientResponse.Error -> ErrorScreen(errorText = ingredientState.getMessage())
        is IngredientResponse.Success, is IngredientResponse.Neutral -> {
            IngredientsTab(
                fullMealSet = fullMealSet,
                addIngredient = { triggerFood = it },
                deleteIngredient = deleteIngredients,
                updateIngredient = updateIngredient,
                updateServingSize = { updateServingSize() }
            )
        }

        else -> {}
    }
}

@Composable
private fun IngredientsTab(
    fullMealSet: MealVM.FullMealSet,
    addIngredient: (Food) -> Unit,
    deleteIngredient: (Food) -> Unit,
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
                        append(" for ${fullMealSet.meal.servingSize} servings")
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
            items(items = fullMealSet.ingredients) {
                IngredientItem9(
                    ingredient = it,
                    deleteIngredient = { deleteIngredient(it) },
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
    addInstruction: () -> Unit,
    deleteInstruction: (Instruction) -> Unit,
    updateInstruction: (Instruction) -> Unit,
    swapInstructions: (Instruction, Instruction) -> Unit
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
                    .clickable { addInstruction() },
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
                                    swapInstructions(
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
                                    swapInstructions(
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
                    updateInstruction(instruction)
                }
                Spacer(modifier = Modifier.height(5.dp))
                Divider()
            }
        }
    }
}

enum class MealScreens {
    Add,
    Edit
}