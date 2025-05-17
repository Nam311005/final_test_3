package com.example.foodd

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import android.content.Intent
import android.widget.TextView
import com.example.foodd.DataClass.LoginData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001  // Mã yêu cầu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val edtUsername = findViewById<EditText>(R.id.usernameEditText)
        val edtPassword = findViewById<EditText>(R.id.passwordEditText)
        val btnLogin = findViewById<Button>(R.id.loginButton)

        btnLogin.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password) // Gọi hàm login với username
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }


        // Cấu hình Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("3162430156-sebvc57coavn9tgdnfhigaseb1t3rngt.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Khởi tạo nút đăng nhập
        val signInButton: SignInButton = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
            signIn()
        }
    }

    // Xử lý đăng nhập Google
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Xử lý kết quả trả về từ Google Sign-In
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    // Xử lý kết quả đăng nhập
    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            // Đăng nhập thành công, có thể truy cập thông tin người dùng
            updateUI(account)
        } catch (e: ApiException) {
            // Đăng nhập thất bại
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            val username = account.displayName
            Toast.makeText(this, "Đăng nhập thành công: $username", Toast.LENGTH_SHORT).show()

            // Chuyển sang HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("username", username) // Nếu muốn truyền thêm thông tin
            startActivity(intent)
            finish() // Đóng MainActivity
        } else {
            Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
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

                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
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
