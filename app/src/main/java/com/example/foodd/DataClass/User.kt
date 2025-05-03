package com.example.foodd.DataClass

data class User(
    val id: Int,
    val full_name: String,
    val email: String,
    val phone: String?,
    val address: String?,
    val username: String,
    val password: String,
    val created_at: String,

)

data class UserRoot(
    val status: String,
    val message: String?, // ← thêm dòng này
    val user: User?        // hoặc bất kỳ tên class nào bạn dùng cho thông tin người dùng
)

