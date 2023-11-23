package com.kunle.aisle9b.screens.meals

import android.util.Log
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
    private var _mealId = MutableStateFlow<Long>(-1)
    private var _createdNewMealState = MutableStateFlow<MealResponse>(MealResponse.Neutral)
    val mealList = _mealList.asStateFlow()
    val mealsWithIngredients = _mealsWithIngredients.asStateFlow()
    val instructions = _instructions.asStateFlow()
    val mealId = _mealId.asStateFlow()
    val createdNewMealState = _createdNewMealState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMeals().distinctUntilChanged().collect { meals ->
                _mealList.value = meals.filter { it.visible }
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

    suspend fun getBrandNewMeal() {
        viewModelScope.launch {
            _createdNewMealState.value = MealResponse.Loading
            try {
                val mealId = repository.insertMeal(Meal.createBlank())
                Log.d("Test", "mealId from VM: ${mealId}")
                _createdNewMealState.value = MealResponse.Success(meal = Meal.createBlank(mealId))
            } catch (e: Exception) {
                _createdNewMealState.value = MealResponse.Error(exception = e)
            }
        }
    }

    fun findMWI(mealId: Long): MealWithIngredients? {
        return _mealsWithIngredients.value.firstOrNull {
            it.meal.mealId == mealId
        }
    }

    fun insertMeal(meal: Meal) { //not used anymore. Only tests call this method
        viewModelScope.launch {
            _mealId.value = repository.insertMeal(meal)
            Log.d("Test", "mealId1 from VM: ${_mealId.value}")
        }
        Log.d("Test", "mealId2 from VM: ${_mealId.value}")
    }

    fun upsertMeal(meal: Meal) = viewModelScope.launch { repository.upsertMeal(meal) }
    fun deleteMeal(meal: Meal) = viewModelScope.launch { repository.deleteMeal(meal) }
    fun updateName(obj: MealNameUpdate) = viewModelScope.launch { repository.updateName(obj) }
    fun updatePic(obj: MealPicUpdate) = viewModelScope.launch { repository.updatePic(obj) }
    fun updateServingSize(obj: MealServingSizeUpdate) =
        viewModelScope.launch { repository.updateServingSize(obj) }

    fun updateNotes(obj: MealNotesUpdate) = viewModelScope.launch { repository.updateNotes(obj) }
    fun updateVisibility(obj: MealVisibilityUpdate) =
        viewModelScope.launch { repository.updateVisibility(obj) }

    fun upsertInstruction(instruction: Instruction) =
        viewModelScope.launch { repository.upsertInstruction(instruction) }

    fun deleteInstruction(instruction: Instruction) =
        viewModelScope.launch { repository.deleteInstruction(instruction) }


    fun insertPair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.insertPair(crossRef) }

    fun deletePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.deletePair(crossRef) }

    fun deleteSpecificMealWithIngredients(mealId: Long) =
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

    fun reorderRestOfInstructionList(oldPosition: Int) {

        val instructionsList = _instructions.value.filter { it.position > oldPosition }
        instructionsList.forEach {
            Log.d("Test", "instructionlist: $it ")
        }

        instructionsList.forEach {
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

    val oldInstruction =
        Instruction(instructionId = 12, step = "second thing", mealId = 1, position = 4)
    val updatedInstruction =
        Instruction(instructionId = 12, step = "first thing", mealId = 1, position = 2)

    val oldInstructionList = listOf(
        Instruction(instructionId = 11, step = "first thing", mealId = 1, position = 1),
        Instruction(instructionId = 22, step = "second thing", mealId = 1, position = 2),
        Instruction(instructionId = 33, step = "third thing", mealId = 1, position = 3),
        Instruction(instructionId = 44, step = "fourth thing", mealId = 1, position = 4),
        Instruction(instructionId = 55, step = "fifth thing", mealId = 1, position = 5)
    )

    fun reorganizeDBInstructions(
        updatedInstruction: Instruction,
        oldInstructionList: List<Instruction>
    ) {
        val oldPosition =
            oldInstructionList.find { it.instructionId == updatedInstruction.instructionId }!!.position
        val newPosition = updatedInstruction.position

        if (newPosition > 0) {
            upsertInstruction(updatedInstruction)

            when {
                newPosition < oldPosition -> {
                    oldInstructionList.forEach {
                        if (it.position in newPosition until oldPosition) {
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
                    oldInstructionList.forEach {
                        if (it.position in (oldPosition + 1)..newPosition) {
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
}