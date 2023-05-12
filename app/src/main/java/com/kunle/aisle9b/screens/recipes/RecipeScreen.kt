package com.kunle.aisle9b.screens.recipes

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.api.apiModels.ApiResponseList
import com.kunle.aisle9b.models.apiModels.queryModels.Result
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.utilScreens.ErrorScreen
import com.kunle.aisle9b.screens.utilScreens.LoadingScreen
import com.kunle.aisle9b.templates.RecipeItem9
import kotlinx.coroutines.launch

@Composable
fun RecipeScreen(
    modifier: Modifier = Modifier,
    recipesVM: RecipesVM,
    navController: NavController,
    topBar: (TopBarOptions) -> Unit,
    source: (GroceryScreens) -> Unit
) {
    topBar(TopBarOptions.Default)
    source(GroceryScreens.RecipeScreen)

    val scope = rememberCoroutineScope()

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SearchBar(onSearchClick = {search ->
                scope.launch {
                    recipesVM.getSearchResults(query = search)
                }
            })
            when (val searchState = recipesVM.searchState.collectAsState().value) {
                is ApiResponseList.Error -> ErrorScreen(errorText = searchState.getMessage())
                is ApiResponseList.Loading -> LoadingScreen()
                is ApiResponseList.Success ->
                    Screen(
                        recipeList = searchState.recipes,
                        navController = navController,
                    )
                ApiResponseList.Neutral -> {}
            }
        }
    }
}

@Composable
private fun Screen(
    recipeList: List<Result>,
    navController: NavController
) {
    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        state = listState,
    ) {
        items(items = recipeList) {
            RecipeItem9(
                modifier = Modifier.padding(6.dp),
                navController = navController,
                id = it.id,
                imageURL = it.image,
                name = it.title,
                numOfLikes = it.aggregateLikes,
                source = it.sourceName,
                readyTimeInMinutes = it.readyInMinutes
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onSearchClick: (String) -> Unit) {
    var searchWord by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        modifier = Modifier
            .height(45.dp)
            .fillMaxWidth(.95f),
        value = searchWord,
        singleLine = true,
        onValueChange = { searchWord = it },
        interactionSource = interactionSource
    ) { onValueChange ->
        TextFieldDefaults.TextFieldDecorationBox(
            value = searchWord,
            innerTextField = onValueChange,
            enabled = true,
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(24.dp)
                )
            },
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
            },
            shape = RectangleShape,
            label = { Text(text = "Find a Recipe") },
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(horizontal = 15.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

