package com.kunle.aisle9b.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.meals.MealScreens
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.screens.meals.Tabs
import com.kunle.aisle9b.screens.utilScreens.ErrorScreen
import com.kunle.aisle9b.screens.utilScreens.LoadingScreen
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditInstructionsDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditSummaryDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.HeadlineDialog9
import com.kunle.aisle9b.util.CameraXMode
import com.kunle.aisle9b.util.MealResponse
import com.kunle.aisle9b.util.PhotoOptionsDialog9
import kotlinx.coroutines.launch

@Composable
fun AddMealScreenGate(
    modifier: Modifier = Modifier,
    mealVM: MealVM = hiltViewModel(),
    generalVM: GeneralVM = hiltViewModel()
) {
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
                mealVM = mealVM
            )
        }
    }
}


@Composable
fun AddMealScreen(
    modifier: Modifier = Modifier,
    createdMeal: Meal,
    mealVM: MealVM,
) {
    if (createdMeal.mealId != 0L) {
        mealVM.setMealId(createdMeal.mealId)
        val fullMealSet = mealVM.fullMeal.collectAsState().value
        val mealId = fullMealSet.meal.mealId

        mealVM.upsertMeal(fullMealSet.meal.copy(visible = true))
        val newInstructionPosition = mealVM.newInstructionNumber.intValue

        val scope = rememberCoroutineScope()

        //states
        val ingredientState by mealVM.addedIngredientState.collectAsState()

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
            HeadlineDialog9(
                original = createdMeal.servingSize,
                onSave = {
                    mealVM.updateServingSize(
                        MealServingSizeUpdate(
                            mealId = mealId,
                            servingSize = it
                        )
                    )
                    modifyServingSize = false
                },
                labelText = "# of servings recipe makes",
                closeDialog = { modifyServingSize = false })
        }

        if (editSummary) {
            EditSummaryDialog9(
                meal = fullMealSet.meal,
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
            if (fullMealSet.meal.mealPic == null) {
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
                fullMealSet = fullMealSet,
                ingredientState = ingredientState,
                editSummary = { editSummary = true },
                addNewIngredient = { ingredient, mealId ->
                    scope.launch { mealVM.getIngredientState(ingredient, mealId, MealScreens.Add) }
                },
                deleteIngredient = {
                    scope.launch {
                        mealVM.deleteFood(it)
                    }
                },
                updateIngredient = { mealVM.updateFood(it) },
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
