package com.kunle.aisle9b.screens.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kunle.aisle9b.api.apiModels.ApiResponseSearch
import com.kunle.aisle9b.models.apiModels.queryModels.Result
import com.kunle.aisle9b.screens.utilScreens.ErrorScreen
import com.kunle.aisle9b.screens.utilScreens.LoadingScreen
import com.kunle.aisle9b.templates.CustomSearchBar9
import com.kunle.aisle9b.templates.items.RecipeItem9
import kotlinx.coroutines.launch

@Composable
fun RecipeScreenGate(
    modifier: Modifier = Modifier,
    recipesVM: RecipesVM = hiltViewModel(),
    navToRecipeDetails: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SearchBar(onSearchClick = { search ->
                scope.launch {
                    recipesVM.getSearchResults(query = search)
                }
            })
            when (val searchState = recipesVM.searchState.collectAsState().value) {
                is ApiResponseSearch.Error -> ErrorScreen(errorText = searchState.getMessage())
                is ApiResponseSearch.Loading -> LoadingScreen()
                is ApiResponseSearch.Success ->
                    RecipeScreen(
                        recipeList = searchState.recipes,
                        navToRecipeDetails = { navToRecipeDetails(it) }
                    )
                ApiResponseSearch.Neutral -> {}
            }
        }
    }
}

@Composable
private fun RecipeScreen(
    recipeList: List<Result>,
    navToRecipeDetails: (Int) -> Unit
) {
    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        state = listState,
    ) {
        items(items = recipeList) {recipe ->
            RecipeItem9(
                modifier = Modifier.padding(5.dp),
                navToRecipeDetails = { navToRecipeDetails(recipe.id) },
                imageURL = recipe.image,
                name = recipe.title,
                source = recipe.sourceName,
                readyTimeInMinutes = recipe.readyInMinutes
            )
        }
    }
}

@Composable
fun SearchBar(onSearchClick: (String) -> Unit) {
    var searchWord by remember { mutableStateOf("") }

    CustomSearchBar9(
        text = searchWord,
        onValueChange = { searchWord = it },
        label = "Find a Recipe",
        trailingIcon = {
            Button(
                shape = RectangleShape,
                contentPadding = PaddingValues(7.dp),
                modifier = Modifier.padding(5.dp),
                onClick = {
                    if (searchWord.isNotEmpty()) {
                        onSearchClick(searchWord)
                    }
                }) {
                Text(
                    text = "Search",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

