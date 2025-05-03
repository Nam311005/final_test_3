package com.example.foodd.DataClass

data class CategoryRoot(
    val status: String,
    val categories: List<Category>
)

data class Category(
    val id: Int,
    val name: String,
    val description: String?
)
