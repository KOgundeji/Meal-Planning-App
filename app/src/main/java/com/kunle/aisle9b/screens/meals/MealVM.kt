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

    private val _visibleMealList = MutableStateFlow<List<Meal>>(emptyList())
    private val _allMeals = MutableStateFlow<List<Meal>>(emptyList())
    private val _mealsWithIngredients = MutableStateFlow<List<MealWithIngredients>>(emptyList())
    private val _instructions = MutableStateFlow<List<Instruction>>(emptyList())
    private var _mealId = MutableStateFlow<Long>(-1)
    private var _createdNewMealState = MutableStateFlow<MealResponse>(MealResponse.Neutral)
    private var _ingredientListState =
        MutableStateFlow<IngredientResponse>(IngredientResponse.Neutral)
    val visibleMealList = _visibleMealList.asStateFlow()
    val allMeals = _allMeals.asStateFlow()
    val mealsWithIngredients = _mealsWithIngredients.asStateFlow()
    val instructions = _instructions.asStateFlow()
    val mealId = _mealId.asStateFlow()
    val createdNewMealState = _createdNewMealState.asStateFlow()
    val ingredientResponse = _ingredientListState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMeals().distinctUntilChanged().collect { meals ->
                _allMeals.value = meals
                _visibleMealList.value = meals.filter { it.visible }
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
        _createdNewMealState.value = MealResponse.Loading
        viewModelScope.launch {
            try {
                val mealId = repository.insertMeal(Meal.createBlank())
                _createdNewMealState.value = MealResponse.Success(meal = Meal.createBlank(mealId))
            } catch (e: Exception) {
                _createdNewMealState.value = MealResponse.Error(exception = e)
            }
        }
    }

    suspend fun getIngredientState(food: Food?, mealId: Long) {
        if (food != null) {
            _ingredientListState.value = IngredientResponse.Loading
            viewModelScope.launch {
                try {
                    val foodId = repository.insertFood(food)
                    repository.insertPair(MealFoodMap(mealId, foodId))
                    _ingredientListState.value = IngredientResponse.Success(foodId = foodId)
                } catch (e: Exception) {
                    _ingredientListState.value = IngredientResponse.Error(exception = e)
                }
            }
        }
    }

    fun insertMeal(meal: Meal) { //not used anymore. Only tests call this method
        viewModelScope.launch {
            _mealId.value = repository.insertMeal(meal)
        }
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