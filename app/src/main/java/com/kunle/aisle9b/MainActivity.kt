package com.kunle.aisle9b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.kunle.aisle9b.navigation.Aisle9Navigation
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.screens.ShoppingViewModel
import com.kunle.aisle9b.ui.theme.Aisle9bTheme
import com.kunle.aisle9b.ui.theme.OrangeTintDark
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val shoppingViewModel:ShoppingViewModel by viewModels()
            Aisle9bTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            backgroundColor = OrangeTintDark,
                            elevation = 5.dp,
                        ) {
                            Text(
                                text = "",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                    }, bottomBar = {
                        BottomNavigationBar9(
                            items = shoppingViewModel.screenList,
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.route)
                            })
                    }) {
                    Aisle9Navigation(navController, shoppingViewModel, Modifier.padding(it))
                }
            }
        }
    }
}
