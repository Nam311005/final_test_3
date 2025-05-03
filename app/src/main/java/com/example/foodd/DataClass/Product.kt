package com.example.foodd.DataClass

data class ProductRoot(
    val status: String,
    val products: List<Product>
)

data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val category_id: Int,
    val image_url: String?
)
