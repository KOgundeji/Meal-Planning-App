package com.kunle.aisle9b.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.meals.MealListOptions
import com.kunle.aisle9b.templates.CustomSearchBar9


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    source: GroceryScreens,
    navigate: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = GroceryScreens.headerTitle(source),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                AdditionalScreenOptions { navigate() }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomListTopAppBar(
    searchWord: String,
    onSearchChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    navigate: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.padding(vertical = 5.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomSearchBar9(
                    text = searchWord,
                    onValueChange = { onSearchChange(it) },
                    label = "Search Saved Grocery Lists",
                    trailingIcon = {
                        if (searchWord.isNotEmpty()) {
                            IconButton(onClick = { onCancelClick() }) {
                                Icon(
                                    imageVector = Icons.Filled.Cancel,
                                    contentDescription = "Cancel button",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                )
                AdditionalScreenOptions { navigate() }
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealTopAppBar(
    searchWord: String,
    viewListOption: MealListOptions?,
    onSearchChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    setListOptions: (MealListOptions) -> Unit,
    navigate: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.padding(vertical = 5.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomSearchBar9(
                    modifier = Modifier.fillMaxWidth(.65f),
                    text = searchWord,
                    onValueChange = { string -> onSearchChange(string) },
                    label = "Search in Meals",
                    trailingIcon = {
                        if (searchWord.isNotEmpty()) {
                            IconButton(onClick = {
                                onCancelClick()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Cancel,
                                    contentDescription = "Cancel button",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                )
                IconButton(onClick = { setListOptions(MealListOptions.List) }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.FormatListBulleted,
                        contentDescription = "List Option",
                        tint = if (viewListOption == MealListOptions.Images) {
                            MaterialTheme.colorScheme.onBackground
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }
                IconButton(onClick = { setListOptions(MealListOptions.Images) }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.Image,
                        contentDescription = "Image Option",
                        tint = if (viewListOption == MealListOptions.Images) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        }
                    )
                }
                AdditionalScreenOptions { navigate() }
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopAppBar(
    source: GroceryScreens,
    onBackClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Go back",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .clickable {
                        onBackClick()
                    }
            )
        },
        title = {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = GroceryScreens.headerTitle(source),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}


