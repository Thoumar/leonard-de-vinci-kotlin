package com.thoumar.leonarddevincikotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.thoumar.database.models.User
import com.thoumar.network.API
import com.thoumar.network.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInActivity : AppCompatActivity() {

    private var signInBtn : Button? = null
    private var signUpBtn : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signInBtn = findViewById(R.id.signInBtn)
        signUpBtn = findViewById(R.id.signUpBtn)

        if(isLoggedIn()) goToMainActivity()

        setSignInBtnListener()
        setSignUpBtnListener()
    }

    private fun setSignUpBtnListener() {
        signUpBtn?.setOnClickListener {
            goToSignUpActivity()
        }
    }

    private fun setSignInBtnListener() {
        signInBtn?.setOnClickListener {
            logIn()
        }
    }

    private fun logIn() {
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
        val password = findViewById<EditText>(R.id.editTextTextPassword).text.toString()

        if(isEmailValid(email) && isPasswordValid(password)) {
            val service = LoginService.buildService(API::class.java)
            val call = service.login()
            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        setSuccesfullyLoggedIn(user!!.prenom, user.nom)
                        goToMainActivity()
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {}
            })
        }
    }

    private fun isPasswordValid(password: String?): Boolean {
        return password?.length!! >= 4
    }

    private fun isEmailValid(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email as CharSequence).matches()
    }

    private fun isLoggedIn(): Boolean {
        return !getSharedPreferences("PREFERENCES", MODE_PRIVATE).getString("USER_FIRST_NAME", "").isNullOrEmpty()
                && !getSharedPreferences("PREFERENCES", MODE_PRIVATE).getString("USER_LAST_NAME", "").isNullOrEmpty()
    }

    private fun setSuccesfullyLoggedIn(prenom: String, nom: String) {
        val spe: SharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        spe.edit().putString("USER_FIRST_NAME", prenom).apply()
        spe.edit().putString("USER_LAST_NAME", nom).apply()
    }

    private fun goToSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    private fun goToMainActivity() {
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }

}