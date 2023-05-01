package com.kunle.aisle9b.screens.recipes

import androidx.lifecycle.ViewModel
import com.kunle.aisle9b.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RecipesVM @Inject constructor(private val repository: ShoppingRepository): ViewModel()  {
}