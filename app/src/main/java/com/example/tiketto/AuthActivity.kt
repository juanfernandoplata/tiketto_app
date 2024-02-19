package com.example.tiketto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.auth_activity)

        val vm = ViewModelProvider(this).get(AuthActivityVM::class.java)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val username = findViewById<EditText>(R.id.username)
                .text.toString()

            val password = findViewById<EditText>(R.id.password)
                .text.toString()

            vm.authenticate(username, password)
        }

        vm.authToken.observe(this) {
            val token = it
            if(token != "null"){
                val preferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE)
                val editor = preferences.edit()

                editor.putString("authToken", token)
                editor.apply()

                val intent = Intent(
                    this@AuthActivity,
                    ScanActivity::class.java
                )

                startActivity(intent)
                finish()
            }
            else{
                findViewById<EditText>(R.id.username)
                    .setText("")

                findViewById<EditText>(R.id.password)
                    .setText("")

                Toast.makeText(
                    this,
                    "Credenciales incorrectas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}