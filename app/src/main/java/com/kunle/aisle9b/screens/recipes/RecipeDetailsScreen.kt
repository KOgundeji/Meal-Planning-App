package com.kunle.aisle9b.screens.recipes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.api.apiModels.ApiResponseInstructions
import com.kunle.aisle9b.api.apiModels.ApiResponseRecipe
import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.TabItem
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.screens.utilScreens.ErrorScreen
import com.kunle.aisle9b.screens.utilScreens.LoadingScreen
import com.kunle.aisle9b.templates.headers.IngredientHeader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RecipeDetailsScreen(
    recipeId: Int?,
    modifier: Modifier = Modifier,
    recipesVM: RecipesVM = hiltViewModel(),
    generalVM: GeneralVM = hiltViewModel()
) {
    generalVM.setTopBarOption(TopBarOptions.Back)
    generalVM.setClickSource(GroceryScreens.RecipeDetailsScreen)

    if (recipeId != null) {
        LaunchedEffect(key1 = recipeId) {
            recipesVM.getRecipe(id = recipeId)
            recipesVM.getInstructions(id = recipeId)
        }
    }

    val retrievedRecipeState = recipesVM.retrievedRecipeState.collectAsState().value
    val retrievedInstructionState = recipesVM.instructions.collectAsState().value

    when {
        retrievedRecipeState is ApiResponseRecipe.Error -> ErrorScreen(errorText = retrievedRecipeState.getMessage())
        retrievedRecipeState is ApiResponseRecipe.Loading -> LoadingScreen()
        retrievedRecipeState is ApiResponseRecipe.Success && retrievedInstructionState is ApiResponseInstructions.Success ->
        DetailsScreen(
            modifier = modifier,
            generalVM = generalVM,
            recipe = retrievedRecipeState.recipe,
            instructions = retrievedInstructionState.instructions
        )
        else -> {}
    }
}

@Composable
private fun DetailsScreen(
    modifier: Modifier,
    generalVM: GeneralVM,
    recipe: Recipe,
    instructions: Instructions
) {
    generalVM.apiMealToBeSaved.value =
        Meal(name = recipe.title, apiID = recipe.id, servingSize = recipe.servings.toString())

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.42f),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = recipe.image, contentDescription = null,
                contentScale = ContentScale.FillHeight,
                alignment = Alignment.Center
            )
        }
        Tabs(recipe = recipe, instructions = instructions)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(recipe: Recipe, instructions: Instructions) {
    val pagerState = rememberPagerState{ 4 }
    val coroutineScope = rememberCoroutineScope()

    val tabLabels = listOf(
        TabItem(
            title = "Summary",
            screen = { SummaryScreen(recipe = recipe) }),
        TabItem(
            title = "Ingredients",
            screen = { IngredientsTab(recipe = recipe) }),
        TabItem(
            title = "Instructions",
            screen = { InstructionsScreen(instructions = instructions) })
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
    HorizontalPager( state = pagerState) {
        tabLabels[pagerState.currentPage].screen()
    }
}

@Composable
fun SummaryScreen(recipe: Recipe) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = recipe.title,
            modifier = Modifier.padding(5.dp),
            fontSize = 24.sp,
            maxLines = 2,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "Source: ${recipe.sourceName}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

    }
}

@Composable
fun IngredientsTab(recipe: Recipe) {
    val ingredients = recipe.extendedIngredients.map {
        it.original
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                        fontSize = 20.sp
                    )
                ) {
                    append(" for ${recipe.servings} servings")
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = ingredients) {
                Text(text = it, fontSize = 14.sp)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InstructionsScreen(instructions: Instructions) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        instructions.forEach {
            stickyHeader {
                IngredientHeader(string = it.name.ifBlank { "Preparation" })
            }
            items(items = it.steps) { stepItem ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stepItem.number.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = stepItem.step, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}





