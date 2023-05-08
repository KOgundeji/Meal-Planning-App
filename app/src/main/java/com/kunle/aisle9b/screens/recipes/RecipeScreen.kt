package com.kunle.aisle9b.screens.recipes

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
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
import com.kunle.aisle9b.models.apiModels.DataOrException
import com.kunle.aisle9b.models.apiModels.recipeModels.RecipeRawAPIData
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.RecipeItem9

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

    val listState = rememberLazyGridState()

    val randomRecipeData = produceState<DataOrException<RecipeRawAPIData, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = recipesVM.getRandomRecipes(tags = "")
    }.value


//    if (randomRecipeData.loading == true) {
//        CircularProgressIndicator()
//    } else if (randomRecipeData.data != null) {
//        val recipeList = buildList {
//            randomRecipeData.data!!.recipes.forEach {
//                add(it)
//            }
//        }

    //fake data
//    val randomRecipeData = DataOrException<List<TestingRecipeClass>, Boolean, Exception>(
//        data = listOf(
//            TestingRecipeClass(
//                name = "Test Meal Yum",
//                imageURL = "https://spoonacular.com/productImages/469199-312x231.jpg",
//                description = "Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs might be a good recipe to expand your main course repertoire. One portion of this dish contains approximately <b>19g of protein </b>,  <b>20g of fat </b>, and a total of  <b>584 calories </b>. For  <b>\$1.63 per serving </b>, this recipe  <b>covers 23% </b> of your daily requirements of vitamins and minerals. This recipe serves 2. It is brought to you by fullbellysisters.blogspot.com. 209 people were glad they tried this recipe. A mixture of scallions, salt and pepper, white wine, and a handful of other ingredients are all it takes to make this recipe so scrumptious. From preparation to the plate, this recipe takes approximately  <b>45 minutes </b>. All things considered, we decided this recipe  <b>deserves a spoonacular score of 83% </b>. This score is awesome. If you like this recipe, take a look at these similar recipes: <a href=\\\"https://spoonacular.com/recipes/cauliflower-gratin-with-garlic-breadcrumbs-318375\\\">Cauliflower Gratin with Garlic Breadcrumbs</a>, < href=\\\"https://spoonacular.com/recipes/pasta-with-cauliflower-sausage-breadcrumbs-30437\\\">Pasta With Cauliflower, Sausage, & Breadcrumbs</a>, and <a href=\\\"https://spoonacular.com/recipes/pasta-with-roasted-cauliflower-parsley-and-breadcrumbs-30738\\\">Pasta With Roasted Cauliflower, Parsley, And Breadcrumbs</a>."
//            ),
//            TestingRecipeClass(
//                name = "Pumpkin",
//                imageURL = "https://spoonacular.com/recipeImages/716429-556x370.jpg",
//                description = "We barrel ferment our Chardonnay and age it in a mix of Oak and Stainless. Giving this light bodied wine modest oak character, a delicate floral aroma, and a warming finish."
//            ),
//            TestingRecipeClass(
//                name = "Test2",
//                imageURL = "https://static.toiimg.com/photo/76942221.cms",
//                description = "We barrel ferment our Chardonnay and age it in a mix of Oak and Stainless. Giving this light bodied wine modest oak character, a delicate floral aroma, and a warming finish."
//            ),
//            TestingRecipeClass(
//                name = "Test2",
//                imageURL = "https://media1.popsugar-assets.com/files/thumbor/q_eu4G_Yfvd1qUU7rkJYpC9Qalk/0x532:1560x2092/fit-in/2048xorig/filters:format_auto-!!-:strip_icc-!!-/2019/11/18/102/n/1922729/2010a3325dd3450317e273.27544324_/i/healthy-meal-prep-dinner-recipes.jpg",
//                description = "We barrel ferment our Chardonnay and age it in a mix of Oak and Stainless. Giving this light bodied wine modest oak character, a delicate floral aroma, and a warming finish."
//            ),
//            TestingRecipeClass(
//                name = "Salmon",
//                imageURL = "https://media.self.com/photos/63a31904dcba23cb155ff501/4:3/w_2560%2Cc_limit/greenchef.jpeg",
//                description = "We barrel ferment our Chardonnay and age it in a mix of Oak and Stainless. Giving this light bodied wine modest oak character, a delicate floral aroma, and a warming finish."
//            )
//        )
//    )
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SearchBar(recipesVM = recipesVM)
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 2),
                state = listState,
            ) {
                items(items = recipesVM.recipeList.value) {
                    RecipeItem9(
                        modifier = Modifier.padding(6.dp),
                        navController = navController,
                        id = 2,
                        imageURL = it.image,
                        name = it.title,
                        numOfLikes = it.aggregateLikes,
                        source = it.sourceName,
                        readyTimeInMinutes = it.readyInMinutes
                    )
                }
            }
        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    recipesVM: RecipesVM
) {
    var searchWord by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        modifier = Modifier
            .height(45.dp)
            .fillMaxWidth(0.95f),
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

