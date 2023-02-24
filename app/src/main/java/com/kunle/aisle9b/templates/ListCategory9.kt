package com.kunle.aisle9b.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kunle.aisle9b.data.sampleFoodCategories
import com.kunle.aisle9b.data.sampleFoodData
import com.kunle.aisle9b.models.FoodCategory
import com.kunle.aisle9b.ui.theme.BaseOrange

@Composable
fun ListCategory9(categoryList: List<FoodCategory>) {

    LazyColumn {
        items(items = categoryList) { category ->
            Column() {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BaseOrange,
                    elevation = 4.dp
                ) {
                    Text(text = category.categoryName)
                }
//                LazyColumn() {
//                    items(items = category.itemList) { item ->
//                        ListItem9(food = item)
//                    }
//                }
            }
        }
    }
}

@Preview
@Composable
fun CategoryPreview(categoryList: List<FoodCategory> = sampleFoodCategories) {
    ListCategory9(categoryList = categoryList)
}
