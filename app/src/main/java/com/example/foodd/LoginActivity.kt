package com.example.foodd

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodd.DataClass.LoginData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login) // Sử dụng layout login.xml

        val edtUsername = findViewById<EditText>(R.id.usernameEditText) // Đổi thành username
        val edtPassword = findViewById<EditText>(R.id.passwordEditText)
        val btnLogin = findViewById<Button>(R.id.loginButton)

        btnLogin.setOnClickListener {
            val username = edtUsername.text.toString().trim() // Sử dụng username
            val password = edtPassword.text.toString().trim()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password) // Gọi hàm login với username
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val loginData = LoginData(username, password) // Gửi username thay vì email
                val response = RetrofitClient.api.loginUser(loginData)
                if (response.isSuccessful) {
                    val userRoot = response.body()
                    withContext(Dispatchers.Main) {
                        if (userRoot?.status == "success") {
                            Toast.makeText(applicationContext, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                        } else {
                            // Hiển thị thông báo từ server nếu đăng nhập thất bại
                            val message = userRoot?.message ?: "Lỗi không xác định"
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Lỗi server", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Lỗi kết nối", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
