package com.kunle.aisle9b.screens.meals

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import com.kunle.aisle9b.models.Instruction
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.models.MealNameUpdate
import com.kunle.aisle9b.models.MealNotesUpdate
import com.kunle.aisle9b.models.MealPicUpdate
import com.kunle.aisle9b.models.MealServingSizeUpdate
import com.kunle.aisle9b.models.MealVisibilityUpdate
import com.kunle.aisle9b.models.MealWithIngredients
import com.kunle.aisle9b.repositories.BasicRepositoryFunctions
import com.kunle.aisle9b.repositories.meals.MealRepository
import com.kunle.aisle9b.util.IngredientResponse
import com.kunle.aisle9b.util.MealResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealVM @Inject constructor(private val repository: MealRepository) : ViewModel(),
    BasicRepositoryFunctions {

    private val _visibleMealList = MutableStateFlow<List<Meal>>(emptyList())
    val visibleMealList = _visibleMealList.asStateFlow()

    private val _allMeals = MutableStateFlow<List<Meal>>(emptyList())
    val allMeals = _allMeals.asStateFlow()

    private val _filteredMealList = MutableStateFlow<List<Meal>>(emptyList())
    val filteredMealList = _filteredMealList.asStateFlow()

    private val _searchWord = MutableStateFlow("")
    val searchWord = _searchWord.asStateFlow()

    private val _mealsWithIngredients = MutableStateFlow<List<MealWithIngredients>>(emptyList())
    val mealsWithIngredients = _mealsWithIngredients.asStateFlow()

    private val _instructions = MutableStateFlow<List<Instruction>>(emptyList())
    val instructions = _instructions.asStateFlow()

    private var _mealId = MutableStateFlow<Long>(-1)
    val mealId = _mealId.asStateFlow()

    private var _createdNewMealState = MutableStateFlow<MealResponse>(MealResponse.Loading)
    private var _editIngredientScreenListState =
        MutableStateFlow<IngredientResponse>(IngredientResponse.Neutral)
    private var _addIngredientScreenListState =
        MutableStateFlow<IngredientResponse>(IngredientResponse.Neutral)
    val createdNewMealState = _createdNewMealState.asStateFlow()
    val editedIngredientState = _editIngredientScreenListState.asStateFlow()
    val addedIngredientState = _addIngredientScreenListState.asStateFlow()


    private val _viewMealId = MutableStateFlow(0L)
    var newInstructionNumber = mutableIntStateOf(0)

    val fullMeal =
        combine(_viewMealId, _mealsWithIngredients, instructions) { mealId, mwi, instructions ->
            val yum = mwi.firstOrNull { it.meal.mealId == mealId }
            val directions = instructions
                .filter { it.mealId == mealId }
                .sortedBy { it.position }

            if (directions.isNotEmpty()) {
                newInstructionNumber.intValue = directions.last().position + 1
            } else {
                newInstructionNumber.intValue = 1
            }
            if (yum != null) {
                FullMealSet(
                    meal = yum.meal,
                    ingredients = yum.ingredients,
                    instructions = directions
                )
            } else {
                FullMealSet.emptyMealSet()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), FullMealSet.emptyMealSet())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMeals().distinctUntilChanged().collect { meals ->
                _allMeals.value = meals
                _visibleMealList.value = meals.filter { it.visible }
                _filteredMealList.value = _visibleMealList.value
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

    fun setMealId(mealId: Long) {
        _viewMealId.value = mealId
    }

    fun setSearchWord(searchWord: String) {
        _searchWord.value = searchWord
        searchForMeal(searchWord)
    }

    private fun searchForMeal(searchWord: String) {
        _filteredMealList.update {
            if (searchWord != "") {
                _visibleMealList.value.filter {
                    it.name.contains(
                        searchWord,
                        ignoreCase = true
                    )
                }
            } else {
                _visibleMealList.value
            }
        }
    }

    suspend fun getBrandNewMeal() {
        _createdNewMealState.value = MealResponse.Loading
        viewModelScope.launch {
            try {
                val mealId = repository.insertMeal(Meal.createBlank())
                _createdNewMealState.value =
                    MealResponse.Success(meal = Meal.createBlank(mealId))
            } catch (e: Exception) {
                _createdNewMealState.value = MealResponse.Error(exception = e)
            }
        }
    }

    suspend fun getIngredientState(food: Food?, mealId: Long, origin: MealScreens) {
        if (food != null) {
            when (origin) {
                MealScreens.Add -> {
                    _addIngredientScreenListState.value = IngredientResponse.Loading
                    viewModelScope.launch {
                        try {
                            updateCategories(food.name, food.category)
                            val foodId = repository.insertFood(food)
                            repository.insertPair(MealFoodMap(mealId, foodId))
                            _addIngredientScreenListState.value =
                                IngredientResponse.Success(foodId = foodId)
                        } catch (e: Exception) {
                            _addIngredientScreenListState.value =
                                IngredientResponse.Error(exception = e)
                        }
                    }
                }

                MealScreens.Edit -> {
                    _editIngredientScreenListState.value = IngredientResponse.Loading
                    viewModelScope.launch {
                        try {
                            val foodId = repository.insertFood(food)
                            repository.insertPair(MealFoodMap(mealId, foodId))
                            _editIngredientScreenListState.value =
                                IngredientResponse.Success(foodId = foodId)
                        } catch (e: Exception) {
                            _editIngredientScreenListState.value =
                                IngredientResponse.Error(exception = e)
                        }
                    }
                }
            }
        }
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

    fun findMWI(mealId: Long): MealWithIngredients? {
        return _mealsWithIngredients.value.firstOrNull { it.meal.mealId == mealId }
    }

    fun updateFood(food: Food) {
        viewModelScope.launch {
            updateCategories(food.name, food.category)
            upsertFood(food)
        }
    }

    private fun updateCategories(foodGroceryName: String, newCategory: String) {
        viewModelScope.launch {
            repository.updateGlobalFoodCategories(
                foodName = foodGroceryName,
                newCategory = newCategory
            )
            repository.updateGlobalGroceryCategories(
                groceryName = foodGroceryName,
                newCategory = newCategory
            )
        }
    }

    fun upsertMeal(meal: Meal) = viewModelScope.launch { repository.upsertMeal(meal) }
    fun deleteMeal(meal: Meal) = viewModelScope.launch { repository.deleteMeal(meal) }
    fun updateName(obj: MealNameUpdate) = viewModelScope.launch { repository.updateName(obj) }
    fun updatePic(obj: MealPicUpdate) = viewModelScope.launch { repository.updatePic(obj) }
    fun updateServingSize(obj: MealServingSizeUpdate) =
        viewModelScope.launch { repository.updateServingSize(obj) }

    fun updateNotes(obj: MealNotesUpdate) =
        viewModelScope.launch { repository.updateNotes(obj) }

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

    override suspend fun upsertGrocery(grocery: Grocery) {
        viewModelScope.launch { repository.upsertGrocery(grocery) }
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

    data class FullMealSet(
        val meal: Meal,
        val ingredients: List<Food>,
        val instructions: List<Instruction>
    ) {
        companion object {
            fun emptyMealSet(): FullMealSet {
                return FullMealSet(
                    meal = Meal.createBlank(),
                    ingredients = emptyList(),
                    instructions = emptyList()
                )
            }
        }
    }
}