package com.thoumar.leonarddevincikotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.thoumar.entities.Credentials
import com.thoumar.entities.User
import com.thoumar.network.API
import com.thoumar.network.LoginService
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        if(isLoggedIn()) {
            // Is logged in
            Toast.makeText(this, "Vous vous êtes déjà connecté", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            finish()
        } else {
            // Never logged in
            Toast.makeText(this, "Vous n'êtes pas connecté", Toast.LENGTH_LONG).show()
        }

        Log.d("[SIGN_IN_ACTIVITY]",
            getPreferences(MODE_PRIVATE).getBoolean("IS_LOGGED_IN", false).toString()
        )

        findViewById<Button>(R.id.loginBtn).setOnClickListener {
            logIn()
        }
    }

    private fun logIn() {
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
        val password = findViewById<EditText>(R.id.editTextTextPassword).text.toString()

        if(isEmailValid(email) && isPasswordValid(password)) {
            Toast.makeText(this, "Trying to log you in", Toast.LENGTH_LONG).show()

            // Call api
            val service = LoginService.buildService(API::class.java)
            val call = service.login()
            call.enqueue(object: Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    Toast.makeText(this@SignInActivity, "Request succesful", Toast.LENGTH_LONG).show()
                    if(response.isSuccessful) {
                        setSuccesfullyLoggedIn()
                        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                        finish()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@SignInActivity, "Failed request", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun isPasswordValid(password: String?): Boolean {
        return true
    }

    private fun isEmailValid(email: String?): Boolean {
        return true
    }

    private fun isLoggedIn(): Boolean {
        return getPreferences(MODE_PRIVATE).getBoolean("IS_LOGGED_IN", false)
    }

    fun setSuccesfullyLoggedIn() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean("IS_LOGGED_IN", true)
            apply()
        }
    }
}