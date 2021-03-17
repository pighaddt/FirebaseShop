package com.angus.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //sign up onClick
        btn_signup.setOnClickListener {
            SignUp()
        }
        //login
        btn_login.setOnClickListener {
            val email = edit_email.text.toString()
            val password = edit_password.text.toString()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {  task ->
                    if (task.isSuccessful){
                        setResult(RESULT_OK)
                        finish()
                    }else{
                        AlertDialog.Builder(this@SignInActivity)
                            .setTitle("Login")
                            .setMessage(task.exception?.message)
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
        }
    }

    private fun SignUp() {
        val email = edit_email.text.toString()
        val password = edit_password.text.toString()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AlertDialog.Builder(this@SignInActivity)
                        .setTitle("Sign Up")
                        .setMessage("Account created")
                        .setPositiveButton("OK") { _, _ ->
                            setResult(RESULT_OK)
                            finish()
                        }.show()
                } else {
                    AlertDialog.Builder(this@SignInActivity)
                        .setTitle("Sign Up")
                        .setMessage(task.exception?.message)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
    }
}