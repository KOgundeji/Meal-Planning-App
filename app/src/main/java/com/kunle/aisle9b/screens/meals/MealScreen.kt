package com.kunle.aisle9b.screens.meals

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.twotone.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.templates.items.MealItem9
import com.kunle.aisle9b.templates.items.VisualMealItem
import com.kunle.aisle9b.ui.theme.DM_LightGray
import com.kunle.aisle9b.ui.theme.DM_MediumGray
import com.kunle.aisle9b.ui.theme.DM_MediumToLightGray
import com.kunle.aisle9b.util.CustomBottomSheet9
import com.kunle.aisle9b.util.ReconciliationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealScreen(
    modifier: Modifier = Modifier,
    generalVM: GeneralVM = hiltViewModel(),
    mealVM: MealVM = hiltViewModel(),
    navToEditDetails: (Long) -> Unit,
    navToViewDetails: (Long) -> Unit,
    navToRecipeDetails: (Int) -> Unit,
) {
    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    var listsToAddToGroceryList by remember { mutableStateOf(emptyList<Food>()) }

    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetMealId by remember { mutableLongStateOf(0L) }
    val sheetState = rememberModalBottomSheetState()

    val context = LocalContext.current

    val filteredMealLists = mealVM.filteredMealList.collectAsState().value
    val mealViewSetting = generalVM.mealViewSetting.collectAsState().value
    val listSpacing = if (mealViewSetting == MealListOptions.Images) {
        30.dp
    } else {
        5.dp
    }

    if (showBottomSheet) {
        val mealWithIngredients = mealVM.findMWI(bottomSheetMealId)
        CustomBottomSheet9(
            sheetState = sheetState,
            textLabels = arrayOf(
                "View Meal",
                "Transfer to Grocery List",
                "Edit Meal",
                "Delete Meal"
            ),
            apiSourced = mealWithIngredients != null && mealWithIngredients.meal.apiID > 0,
            closeBottomSheet = { showBottomSheet = false },
            viewList = {
                if (mealWithIngredients != null) {
                    if (mealWithIngredients.meal.apiID > 0) {
                        navToRecipeDetails(mealWithIngredients.meal.apiID)
                    } else {
                        navToViewDetails(bottomSheetMealId)
                    }
                }
            },
            transferFood = {
                if (mealWithIngredients != null && mealWithIngredients.meal.apiID > 0) {
                    listsToAddToGroceryList = mealWithIngredients.ingredients
                    transferFoodsToGroceryList = true
                }
                showBottomSheet = false
            },
            edit = {
                if (mealWithIngredients != null && mealWithIngredients.meal.apiID > 0) {
                    navToEditDetails(bottomSheetMealId)
                }
                showBottomSheet = false
            },
            delete = {
                if (mealWithIngredients != null) {
                    mealVM.deleteMeal(mealWithIngredients.meal)
                    mealVM.deleteSpecificMealWithIngredients(bottomSheetMealId)
                    Toast.makeText(
                        context,
                        "${mealWithIngredients.meal.name} deleted from Meals",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                showBottomSheet = false
            },
            headerContent = {
                if (mealWithIngredients != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(.85f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            if (mealWithIngredients.meal.mealPic != Uri.EMPTY || mealWithIngredients.meal.apiImageURL != null) {
                                AsyncImage(
                                    modifier = Modifier.size(60.dp),
                                    model =
                                    if (mealWithIngredients.meal.apiID <= 0) {
                                        mealWithIngredients.meal.mealPic
                                    } else {
                                        mealWithIngredients.meal.apiImageURL
                                    },
                                    contentDescription = null,
                                    contentScale = ContentScale.FillHeight,
                                    alignment = Alignment.Center
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp),
                                text = mealWithIngredients.meal.name,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Start,
                                lineHeight = 22.sp
                            )
                        }
                        Icon(
                            modifier = Modifier
                                .size(45.dp)
                                .clickable { showBottomSheet = false },
                            tint = DM_MediumToLightGray,
                            imageVector = Icons.Filled.Cancel, contentDescription = null
                        )
                    }
                }
            }
        )
    }

    if (transferFoodsToGroceryList) {
        val foodsForReconciliation =
            generalVM.filterForReconciliation(
                listToAdd = listsToAddToGroceryList
            )

        if (foodsForReconciliation.isNotEmpty()) {
            ReconciliationDialog(
                items = foodsForReconciliation,
                viewModel = mealVM
            ) {
                transferFoodsToGroceryList = false
            }
        } else {
            transferFoodsToGroceryList = false
        }

        Toast.makeText(context, "Groceries added to Grocery List", Toast.LENGTH_SHORT).show()
    }
    LazyColumn(
        modifier = modifier.padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(listSpacing)
    ) {
        items(items = filteredMealLists) { meal ->
            when (mealViewSetting) {
                MealListOptions.List, null ->
                    MealItem9(
                        mealWithIngredients = mealVM.findMWI(meal.mealId),
                        showBottomSheet = {
                            bottomSheetMealId = meal.mealId
                            showBottomSheet = true
                        })

                MealListOptions.Images ->
                    VisualMealItem(
                        meal = meal,
                        showBottomSheet = {
                            bottomSheetMealId = meal.mealId
                            showBottomSheet = true
                        }
                    )
            }
        }
    }
}

enum class MealListOptions {
    List,
    Images
}



