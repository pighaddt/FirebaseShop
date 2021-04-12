package com.angus.shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {
    private lateinit var adapter: FirestoreRecyclerAdapter<item, ItemHolder>

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val RC_SIGNIN_GOOGLE = 1
        private val RC_SIGNIN_FIREBASEUI: Int = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        verify_email.setOnClickListener {
            FirebaseAuth.getInstance().currentUser.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Snackbar.make(it, "Send email", Snackbar.LENGTH_LONG).show()
                    }
                }
        }

        //recycler setting
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        val query = FirebaseFirestore.getInstance()
            .collection("items")
            .limit(10)
        val options = FirestoreRecyclerOptions.Builder<item>()
            .setQuery(query, item::class.java)
            .build()
         adapter = object : FirestoreRecyclerAdapter<item, ItemHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
                return ItemHolder(view)
            }

            override fun onBindViewHolder(holder: ItemHolder, position: Int, item: item) {
                holder.bindTo(item)
                holder.itemView.setOnClickListener {
                    itemClicked(item, position)
                }
            }

        }
        recycler.adapter = adapter
    }

    private fun itemClicked(item: item, position: Int) {
        Log.d(TAG, "itemClicked: ${item.title} , ${position}")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        val user = auth.currentUser
        if (user != null) {
//            userInfo.text = "Email : ${user.email} / ${user.isEmailVerified}"
            verify_email.visibility = if (user.isEmailVerified) View.GONE else View.VISIBLE
        } else {
//            userInfo.text = "NOT LOGIN"
            verify_email.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
        adapter.stopListening()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_signin -> {
                val whiteList = listOf<String>("tw", "hk", "cn")
                val idpConfig = arrayOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.FacebookBuilder().build(),
                    AuthUI.IdpConfig.PhoneBuilder()
                        .setWhitelistedCountries(whiteList)
                        .build(),
                    AuthUI.IdpConfig.AnonymousBuilder().build()
                )
                val firebaseUIIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(idpConfig.toMutableList())
                    .setLogo(R.drawable.payment__card)
                    .setIsSmartLockEnabled(false)
                    .setTheme(R.style.SignUp)
                    .build()
                startActivityForResult(firebaseUIIntent, RC_SIGNIN_FIREBASEUI)
                /*startActivityForResult(
                        Intent(this@MainActivity, SignInActivity::class.java),
                RC_SIGNIN)*/
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