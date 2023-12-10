package com.kunle.aisle9b.screens.meals

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AddAPhoto
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kunle.aisle9b.models.Instruction
import com.kunle.aisle9b.models.MealNameUpdate
import com.kunle.aisle9b.models.MealNotesUpdate
import com.kunle.aisle9b.models.MealPicUpdate
import com.kunle.aisle9b.models.MealServingSizeUpdate
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditInstructionsDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditSummaryDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.HeadlineDialog9
import com.kunle.aisle9b.util.CameraXMode
import com.kunle.aisle9b.util.PhotoOptionsDialog9
import kotlinx.coroutines.launch

@Composable
fun EditMealDetailsScreen(
    mealId: Long?,
    modifier: Modifier = Modifier,
    mealVM: MealVM = hiltViewModel(),
) {
    if (mealId != null) {
        val scope = rememberCoroutineScope()
        val ingredientState by mealVM.editedIngredientState.collectAsState()

        mealVM.setMealId(mealId)
        val fullMealSet = mealVM.fullMeal.collectAsState().value
        val newInstructionPosition = mealVM.newInstructionNumber.intValue

        var editSummary by remember { mutableStateOf(false) }
        var modifyServingSize by remember { mutableStateOf(false) }
        var addNewInstruction by remember { mutableStateOf(false) }
        var editPicture by remember { mutableStateOf(false) }
        var shouldShowCamera by remember { mutableStateOf(false) }

        if (addNewInstruction) {
            val brandNewInstruction =
                Instruction.createBlank(fullMealSet.meal.mealId, newInstructionPosition)
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
                original = fullMealSet.meal.servingSize,
                onSave = {
                    mealVM.updateServingSize(
                        MealServingSizeUpdate(
                            mealId = fullMealSet.meal.mealId,
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
                    mealVM.updateName(MealNameUpdate(mealId = fullMealSet.meal.mealId, name = name))
                    mealVM.updateNotes(
                        MealNotesUpdate(
                            mealId = fullMealSet.meal.mealId,
                            notes = notes
                        )
                    )
                    editSummary = false
                },
                setShowDialog = { editSummary = false })
        }

        if (editPicture) {
            PhotoOptionsDialog9(
                onImageCaptured = { Uri ->
                    mealVM.updatePic(
                        MealPicUpdate(
                            mealId = mealId,
                            mealPic = Uri
                        )
                    )
                    editPicture = false
                },
                toggleCamera = { shouldShowCamera = it },
                deletePic = {
                    mealVM.updatePic(
                        MealPicUpdate(
                            mealId = mealId,
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
                    mealVM.updatePic(MealPicUpdate(mealId = mealId, mealPic = uri))
                    editPicture = false
                },
                toggleCamera = { shouldShowCamera = it })
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
                        model = fullMealSet.meal.mealPic,
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
                    scope.launch { mealVM.getIngredientState(ingredient, mealId, MealScreens.Edit) }
                },
                deleteIngredient = {
                    scope.launch { mealVM.deleteFood(it) }
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





