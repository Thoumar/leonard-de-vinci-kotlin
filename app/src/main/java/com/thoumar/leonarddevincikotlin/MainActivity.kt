package com.thoumar.leonarddevincikotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val allSP = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE).all
        Log.d("[MainActivity]", allSP.toString())


        val prenom = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE).getString("USER_FIRST_NAME", null)
        val nom = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE).getString("USER_LAST_NAME", null)

        Toast.makeText(this, "$prenom $nom", Toast.LENGTH_LONG).show()

        findViewById<TextView>(R.id.welcomeText).text = "Bonjour ${nom} ${prenom}"
    }
}