package com.thoumar.leonarddevincikotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.thoumar.entities.User
import com.thoumar.network.API
import com.thoumar.network.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        findViewById<EditText>(R.id.editTextFirstName)
        findViewById<EditText>(R.id.editTextLastName)
        findViewById<EditText>(R.id.editTextEmailAddress)
        findViewById<EditText>(R.id.editTextPassword)
        findViewById<Button>(R.id.signUpBtn).setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val nom = findViewById<EditText>(R.id.editTextFirstName).text.toString()
        val prenom = findViewById<EditText>(R.id.editTextLastName).text.toString()
        val email = findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
        val password = findViewById<EditText>(R.id.editTextPassword).text.toString()

        if(isNomValid(nom) && isPrenomValid(prenom) && isEmailValid(email) && isPasswordValid(password)) {
            Toast.makeText(this, "Trying to sign you in", Toast.LENGTH_LONG).show()

            val service = LoginService.buildService(API::class.java)
            val call = service.login()
            call.enqueue(object: Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful) {
                        val user  = response.body()
                        setSuccesfullySignedIn(user!!.prenom, user.nom)
                        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                        finish()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@SignUpActivity, "Failed request", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun isNomValid(nom: String?): Boolean {
        return true
    }

    private fun isPrenomValid(prenom: String?): Boolean {
        return true
    }

    private fun isPasswordValid(password: String?): Boolean {
        return true
    }

    private fun isEmailValid(email: String?): Boolean {
        return true
    }

    fun setSuccesfullySignedIn(prenom: String, nom: String) {
        val spe: SharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        spe.edit().putString("USER_FIRST_NAME", prenom).apply()
        spe.edit().putString("USER_LAST_NAME", nom).apply()
    }
}