package com.thoumar.leonarddevincikotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.thoumar.database.models.User
import com.thoumar.network.API
import com.thoumar.network.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private var editTextFirstName: EditText? = null
    private var editTextLastName: EditText? = null
    private var editTextEmailAddress: EditText? = null
    private var editTextPassword: EditText? = null
    private var signInBtn: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress)
        editTextPassword = findViewById(R.id.editTextPassword)
        signInBtn = findViewById(R.id.signUpBtn)

        setSignInButtonListener()
    }

    private fun setSignInButtonListener() {
        signInBtn?.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val nom = editTextFirstName?.text.toString()
        val prenom = editTextLastName?.text.toString()
        val email = editTextEmailAddress?.text.toString()
        val password = editTextPassword?.text.toString()

        if(isNomValid(nom) && isPrenomValid(prenom) && isEmailValid(email) && isPasswordValid(password)) {
            val service = LoginService.buildService(API::class.java)
            val call = service.login()
            call.enqueue(object: Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful) {
                        val user  = response.body()
                        if(user !== null) {
                            setSuccesfullySignedIn(user.prenom, user.nom)
                            goToMainActivity()
                        }
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {}
            })
        } else {
            Snackbar.make(this.findViewById(R.id.root), "Tous les champs ne sont pas remplit", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
        finish()
    }

    private fun isNomValid(nom: String?): Boolean {
        return nom.isNullOrEmpty()
    }

    private fun isPrenomValid(prenom: String?): Boolean {
        return prenom.isNullOrEmpty()
    }

    private fun isPasswordValid(password: String?): Boolean {
        return password?.length!! >= 4
    }

    private fun isEmailValid(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email as CharSequence).matches()
    }

    fun setSuccesfullySignedIn(prenom: String, nom: String) {
        val spe: SharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        spe.edit().putString("USER_FIRST_NAME", prenom).apply()
        spe.edit().putString("USER_LAST_NAME", nom).apply()
    }
}