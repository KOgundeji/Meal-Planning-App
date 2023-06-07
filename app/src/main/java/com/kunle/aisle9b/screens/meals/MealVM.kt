package com.kunle.aisle9b.screens.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MealVM @Inject constructor(private val repository: ShoppingRepository) : ViewModel() {

    private val _mealsList = MutableStateFlow<List<Meal>>(emptyList())
    private val _mealsWithIngredients = MutableStateFlow<List<MealWithIngredients>>(emptyList())
    private val _instructions = MutableStateFlow<List<Instruction>>(emptyList())
    val mealsList = _mealsList.asStateFlow()
    val mealsWithIngredients = _mealsWithIngredients.asStateFlow()
    val instructions = _instructions.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMeals().distinctUntilChanged().collect {
                _mealsList.value = it
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMealsWithIngredients().distinctUntilChanged().collect {
                _mealsWithIngredients.value = it
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllInstructions().distinctUntilChanged().collect {
                _instructions.value = it
            }
        }
    }

    fun upsertMeal(meal: Meal) = viewModelScope.launch { repository.upsertMeal(meal) }
    fun deleteMeal(meal: Meal) = viewModelScope.launch { repository.deleteMeal(meal) }
    fun updateName(obj: MealNameUpdate) = viewModelScope.launch { repository.updateName(obj) }
    fun updatePic(obj: PicUpdate) = viewModelScope.launch { repository.updatePic(obj) }
    fun updateServingSize(obj: ServingSizeUpdate) =
        viewModelScope.launch { repository.updateServingSize(obj) }

    fun updateNotes(obj: NotesUpdate) = viewModelScope.launch { repository.updateNotes(obj) }
    fun deleteAllMeals() = viewModelScope.launch { repository.deleteAllMeals() }
    suspend fun getMeal(name: String): Meal {
        return viewModelScope.async {
            repository.getMeal(name)
        }.await()
    }

    fun upsertInstruction(instruction: Instruction) =
        viewModelScope.launch { repository.upsertInstruction(instruction) }

    fun deleteInstruction(instruction: Instruction) =
        viewModelScope.launch { repository.deleteInstruction(instruction) }


    suspend fun getAllInstructionsForMeal(mealId: UUID): Flow<List<Instruction>> =
        viewModelScope.async {
            repository.getAllInstructionsForMeal(mealId)
        }.await()


    fun insertPair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.insertPair(crossRef) }

    fun deletePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.deletePair(crossRef) }

    fun updatePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.updatePair(crossRef) }

    fun deleteSpecificMealIngredients(mealId: UUID) =
        viewModelScope.launch { repository.deleteSpecificMealIngredients(mealId) }

    fun deleteAllMealWithIngredients() =
        viewModelScope.launch { repository.deleteAllMealWithIngredients() }

    suspend fun getSpecificMealWithIngredients(mealId: Long): MealWithIngredients {
        return viewModelScope.async {
            repository.getSpecificMealWithIngredients(mealId)
        }.await()
    }

    fun reorganizeTempInstructions(
        instruction: Instruction,
        newPosition: Int,
        instructions: List<Instruction>
    ): List<Instruction> {
        val oldPosition = instruction.position
        val newInstructionList = mutableListOf<Instruction>()

        newInstructionList.add(
            Instruction(
                instructionId = instruction.instructionId,
                step = instruction.step,
                mealId = instruction.mealId,
                position = newPosition
            )
        )

        when {
            (oldPosition == 0 || oldPosition == newPosition) -> {
                instructions.forEach {
                    if (it.instructionId != instruction.instructionId) {
                        newInstructionList.add(it)
                    }
                }
            }
            newPosition < oldPosition -> {
                instructions.forEach {
                    if (it.position >= newPosition && it.position < instruction.position) {
                        newInstructionList.add(
                            Instruction(
                                instructionId = it.instructionId,
                                step = it.step,
                                mealId = it.mealId,
                                position = it.position + 1
                            )
                        )
                    } else {
                        newInstructionList.add(it)
                    }
                }
            }
            newPosition > oldPosition -> {
                instructions.forEach {
                    if (it.position <= newPosition && it.position > instruction.position) {
                        newInstructionList.add(
                            Instruction(
                                instructionId = it.instructionId,
                                step = it.step,
                                mealId = it.mealId,
                                position = it.position - 1
                            )
                        )
                    }
                }
            }
        }
        return newInstructionList.toList()
    }

    fun reorganizeDBInstructions(
        instruction: Instruction,
        newPosition: Int,
        instructions: List<Instruction>
    ) {
        val oldPosition = instruction.position

        upsertInstruction(
            Instruction(
                instructionId = instruction.instructionId,
                step = instruction.step,
                mealId = instruction.mealId,
                position = newPosition
            )
        )
        when {
            newPosition < oldPosition -> {
                instructions.forEach {
                    if (it.position >= newPosition && it.position < instruction.position) {
                        upsertInstruction(
                            Instruction(
                                instructionId = it.instructionId,
                                step = it.step,
                                mealId = it.mealId,
                                position = it.position + 1
                            )
                        )
                    }
                }
            }
            newPosition > oldPosition -> {
                instructions.forEach {
                    if (it.position <= newPosition && it.position > instruction.position) {
                        upsertInstruction(
                            Instruction(
                                instructionId = it.instructionId,
                                step = it.step,
                                mealId = it.mealId,
                                position = it.position - 1
                            )
                        )
                    }
                }
            }
        }

    }


}