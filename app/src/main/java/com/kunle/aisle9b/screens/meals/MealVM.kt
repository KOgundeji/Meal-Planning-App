package com.kunle.aisle9b.screens.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.repositories.BasicRepositoryFunctions
import com.kunle.aisle9b.repositories.meals.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealVM @Inject constructor(private val repository: MealRepository) : ViewModel(),
    BasicRepositoryFunctions {

    private val _mealList = MutableStateFlow<List<Meal>>(emptyList())
    private val _mealsWithIngredients = MutableStateFlow<List<MealWithIngredients>>(emptyList())
    private val _instructions = MutableStateFlow<List<Instruction>>(emptyList())
    val mealList = _mealList.asStateFlow()
    val mealsWithIngredients = _mealsWithIngredients.asStateFlow()
    val instructions = _instructions.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMeals().distinctUntilChanged().collect { meals ->
                _mealList.value = meals
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMealsWithIngredients().distinctUntilChanged().collect { mwi ->
                _mealsWithIngredients.value = mwi
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllInstructions().distinctUntilChanged().collect { list ->
                _instructions.value = list
            }
        }
    }

    fun insertMeal(meal: Meal): Long {
        var mealId = -1L
        viewModelScope.launch {
            mealId = async { repository.insertMeal(meal) }.await()
        }
        return mealId
    }

    fun upsertMeal(meal: Meal) = viewModelScope.launch { repository.upsertMeal(meal) }
    fun deleteMeal(meal: Meal) = viewModelScope.launch { repository.deleteMeal(meal) }
    fun updateName(obj: MealNameUpdate) = viewModelScope.launch { repository.updateName(obj) }
    fun updatePic(obj: PicUpdate) = viewModelScope.launch { repository.updatePic(obj) }
    fun updateServingSize(obj: ServingSizeUpdate) =
        viewModelScope.launch { repository.updateServingSize(obj) }

    fun updateNotes(obj: NotesUpdate) = viewModelScope.launch { repository.updateNotes(obj) }

    fun upsertInstruction(instruction: Instruction) =
        viewModelScope.launch { repository.upsertInstruction(instruction) }

    fun deleteInstruction(instruction: Instruction) =
        viewModelScope.launch { repository.deleteInstruction(instruction) }


    fun insertPair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.insertPair(crossRef) }

    fun deletePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.deletePair(crossRef) }

    fun deleteSpecificMealIngredients(mealId: Long) =
        viewModelScope.launch { repository.deleteSpecificMealWithIngredients(mealId) }

    override suspend fun insertFood(food: Food): Long {
        var foodId = -1L
        viewModelScope.launch {
            foodId = async { repository.insertFood(food) }.await()
        }
        return foodId
    }

    override suspend fun insertGrocery(grocery: Grocery) {
        viewModelScope.launch { repository.insertGrocery(grocery) }
    }

    override suspend fun upsertFood(food: Food) {
        viewModelScope.launch { repository.upsertFood(food) }
    }

    override suspend fun deleteFood(food: Food) {
        viewModelScope.launch { repository.deleteFood(food) }
    }

    override suspend fun deleteGrocery(grocery: Grocery) {
        viewModelScope.launch { repository.deleteGrocery(grocery) }
    }

    override suspend fun deleteGroceryByName(name: String) {
        viewModelScope.launch { repository.deleteGroceryByName(name) }
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