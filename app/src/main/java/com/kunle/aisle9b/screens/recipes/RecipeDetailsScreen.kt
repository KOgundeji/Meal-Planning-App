package com.kunle.aisle9b.screens.recipes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kunle.aisle9b.models.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.models.tabs.TabItem
import com.kunle.aisle9b.util.parseHtml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RecipeDetailsScreen(
    modifier: Modifier = Modifier,
    recipeId: Int?,
    recipesVM: RecipesVM
) {
    val targetRecipe = recipesVM.recipeList.collectAsState().value.firstOrNull {
        it.id == recipeId
    }

    if (targetRecipe != null) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.42f),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = targetRecipe.image, contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    alignment = Alignment.Center
                )
            }
            Tabs(recipe = targetRecipe)
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Recipe not found",
                fontSize = 30.sp
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(recipe: Recipe) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val tabLabels = listOf(
        TabItem(
            title = "Summary",
            icon = Icons.Default.Description,
            screen = { SummaryScreen(recipe = recipe) }),
        TabItem(
            title = "Ingredients",
            icon = Icons.Default.ShoppingCart,
            screen = { IngredientsTab(recipe = recipe) }),
        TabItem(
            title = "Instructions",
            icon = Icons.Default.FormatListNumbered,
            screen = { InstructionsScreen(recipe = recipe)})
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
    HorizontalPager(pageCount = tabLabels.size, state = pagerState) {
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
            fontSize = 20.sp,
            maxLines = 2,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "Source: ${recipe.sourceName}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(text = recipe.summary.parseHtml())
    }
}

@Composable
fun IngredientsTab(recipe: Recipe) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items = recipe.extendedIngredients) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp),
            ) {
                Text(text = it.original)
            }
            Divider(thickness = 1.dp)
        }
    }
}

@Composable
fun InstructionsScreen(recipe: Recipe) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items = recipe.extendedIngredients) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp),
            ) {
                Text(text = it.original)
            }
            Divider(thickness = 1.dp)
        }
    }
}





