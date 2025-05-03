package com.example.foodd.DataClass

data class OrderItemRoot(
    val status: String,
    val order_items: List<OrderItem>
)

data class OrderItem(
    val id: Int,
    val order_id: Int,
    val product_id: Int,
    val quantity: Int,
    val price: Double
)