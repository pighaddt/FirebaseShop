package com.angus.shop

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {
    companion object{
        private val RC_SIGNIN  = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        verify_email.setOnClickListener {
            FirebaseAuth.getInstance().currentUser.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Snackbar.make(it, "Send email", Snackbar.LENGTH_LONG).show()
                    }
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onAuthStateChanged(auth: FirebaseAuth) {
        val user = auth.currentUser
            if (user != null){
                userInfo.text = "Email : ${user.email} / ${user.isEmailVerified}"
                verify_email.visibility = if (user.isEmailVerified) View.GONE else  View.VISIBLE
            }else{
                userInfo.text = "NOT LOGIN"
                verify_email.visibility = View.GONE
            }
    }
    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_signin ->{
                startActivityForResult(
                        Intent(this@MainActivity, SignInActivity::class.java),
                RC_SIGNIN)
               return true
            }
            R.id.action_signout -> {
                FirebaseAuth.getInstance().signOut()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}