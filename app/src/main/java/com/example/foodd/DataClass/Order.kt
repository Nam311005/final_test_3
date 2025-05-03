package com.example.foodd.DataClass

data class OrderRoot(
    val status: String,
    val orders: List<Order>
)

data class Order(
    val id: Int,
    val user_id: Int,
    val total_price: Double,
    val created_at: String
)
