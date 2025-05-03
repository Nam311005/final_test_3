package com.example.foodd

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodd.Adapter.CategoryAdapter
import com.example.foodd.DataClass.CategoryRoot
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val gson = Gson()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = CategoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadCategories()
    }

    private fun loadCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getDataByType("categories")

                // Kiểm tra body hợp lệ
                val json = response.string()
                val categoryRoot = gson.fromJson(json, CategoryRoot::class.java)

                withContext(Dispatchers.Main) {
                    // Kiểm tra null hoặc empty trước khi setData
                    if (categoryRoot.status == "success" && categoryRoot.categories.isNotEmpty()) {
                        adapter.setData(categoryRoot.categories)
                    } else {
                         Toast.makeText(this@MainActivity, "No categories found", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}