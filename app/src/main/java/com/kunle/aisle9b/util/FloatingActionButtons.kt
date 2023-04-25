package com.kunle.aisle9b.util

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kunle.aisle9b.MultiFloatingState
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.CustomListButtonBar
import com.kunle.aisle9b.screens.MealButtonBar
import com.kunle.aisle9b.screens.ShoppingVM

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FAB(
    navController: NavController,
    shoppingVM: ShoppingVM,
    multiFloatingState: MultiFloatingState,
    onMultiFabStateChange: (MultiFloatingState) -> Unit
) {
    val transition = updateTransition(targetState = multiFloatingState, label = "transition")
    val rotate by transition.animateFloat(label = "rotate") {
        if (it == MultiFloatingState.Expanded) 360f else 0f
    }
    var expanded by remember { mutableStateOf(false) }
    val mealScreen = shoppingVM.fabSource.value == GroceryScreens.MealScreen.name

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = expanded,
        ) {
            Column() {
                SmallFloatingActionButton(
                    modifier = Modifier.animateEnterExit(
                        enter = scaleIn(animationSpec = tween(durationMillis = 500)),
                        exit = scaleOut(animationSpec = tween(durationMillis = 500))
                    ),
                    onClick = {
                        navController.navigate(GroceryScreens.MealScreen.name)
                        shoppingVM.mealPrimaryButtonBar.value = MealButtonBar.Delete
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                SmallFloatingActionButton(
                    modifier = Modifier.animateEnterExit(
                        enter = scaleIn(animationSpec = tween(durationMillis = 500)),
                        exit = scaleOut(animationSpec = tween(durationMillis = 500))
                    ),
                    onClick = {
                        if (mealScreen) {
                            navController.navigate(GroceryScreens.MealScreen.name)
                            shoppingVM.mealPrimaryButtonBar.value = MealButtonBar.Transfer
                        } else {
                            navController.navigate(GroceryScreens.PremadeListScreen.name)
                            shoppingVM.listPrimaryButtonBar.value = CustomListButtonBar.Transfer
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.DriveFileMoveRtl,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                SmallFloatingActionButton(
                    modifier = Modifier.animateEnterExit(
                        enter = scaleIn(animationSpec = tween(durationMillis = 500)),
                        exit = scaleOut(animationSpec = tween(durationMillis = 500))
                    ),
                    onClick = {
                        if (mealScreen) {
                            navController.navigate(GroceryScreens.AddMealsScreen.name)
                        } else {
                            navController.navigate(GroceryScreens.AddCustomListScreen.name)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Add button",
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        FloatingActionButton(
            onClick = {
                onMultiFabStateChange(
                    if (transition.currentState == MultiFloatingState.Expanded) {
                        expanded = false
                        MultiFloatingState.Collapsed
                    } else {
                        expanded = true
                        MultiFloatingState.Expanded
                    }
                )
            },
            modifier = Modifier.rotate(rotate),
            containerColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Filled.TouchApp,
                contentDescription = null
            )
        }
    }
}

