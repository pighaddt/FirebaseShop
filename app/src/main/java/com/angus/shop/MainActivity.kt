package com.angus.shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {
    private lateinit var itemViewModel: ItemViewModel

    //    private lateinit var adapter: FirestoreRecyclerAdapter<Item, ItemHolder>
    var categories = mutableListOf<Category>()
    lateinit var adapter : ItemAdapter

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
        //spinner
        FirebaseFirestore.getInstance().collection("categories")
            .get().addOnCompleteListener { task ->
                Log.d(TAG, "task: ${task.exception}")
                if (task.isSuccessful){
                    task.result?.let {
                        categories.add(Category("", "不分類"))
                        for (doc in it){
                            categories.add(Category(doc.id,  doc.data.get("name").toString()))
                            Log.d(TAG, "categories: ${doc.id} ${doc.data.get("name").toString()}")
                        }
                        spinner.adapter = ArrayAdapter<Category>(
                            this@MainActivity,
                            android.R.layout.simple_spinner_item,
                            categories
                        ).apply {
                            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
                        spinner.setSelection(0, false)
                        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
//                                setupAdapter
                                itemViewModel.getCategory(categories[position].id)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                        }
                    }
                }
            }

        //recycler setting
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = ItemAdapter(mutableListOf<Item>())
        recycler.adapter = adapter
        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)
        itemViewModel.getItems().observe(this, Observer {
            Log.d(TAG, "observe: ${it.size()}")
            val list = mutableListOf<Item>()
            for (doc in it.documents){
                val item = doc.toObject(Item::class.java) ?: Item()
                item.id = doc.id
                list.add(item)
            }
            adapter.items = list
            adapter.notifyDataSetChanged()
            //Room insert
            list.forEach {
                ItemDatabase.getInstance(this@MainActivity)?.getItemDao()?.addItem(it)
            }
            //print database content
            ItemDatabase.getInstance(this@MainActivity)?.getItemDao()?.getItems()?.forEach {
                Log.d(TAG, "Room: ${it.title} ${it.id} ${it.price}")
            }
            
        })
//        setupAdapter()
    }

    inner class ItemAdapter(var items : List<Item>) : RecyclerView.Adapter<ItemHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false))
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.bindTo(items[position])
            holder.itemView.setOnClickListener {
                itemClicked(items[position], position)
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

    }

//    private fun setupAdapter() {
//        var selected = spinner.selectedItemPosition
//        var query : Query
//        if (selected > 0){
//            adapter.stopListening()
//            query = FirebaseFirestore.getInstance()
//                .collection("items")
//                .whereEqualTo("category", categories.get(selected).id)
//                .orderBy("viewCount", Query.Direction.DESCENDING)
//                .limit(10)
//        }else{
//            query = FirebaseFirestore.getInstance()
//                .collection("items")
////                .whereEqualTo("category", "gk412yOnwp9gAP1DSyHw")
//                .orderBy("viewCount", Query.Direction.DESCENDING)
//                .limit(10)
//        }
//
//        val options = FirestoreRecyclerOptions.Builder<Item>()
//            .setQuery(query, Item::class.java)
//            .build()
//        adapter = object : FirestoreRecyclerAdapter<Item, ItemHolder>(options) {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
//                val view =
//                    LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
//                return ItemHolder(view)
//            }
//
//            override fun onBindViewHolder(holder: ItemHolder, position: Int, Item: Item) {
//                Item.id = snapshots.getSnapshot(position).id
//                holder.bindTo(Item)
//                holder.itemView.setOnClickListener {
//                    itemClicked(Item, position)
//                }
//            }
//
//        }
//        recycler.adapter = adapter
//        adapter.startListening()
//    }

    private fun itemClicked(Item: Item, position: Int) {
        Log.d(TAG, "itemClicked: ${Item.title} , ${position}")
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("ITEM", Item)
        startActivity(intent)
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
//        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
//        adapter.stopListening()
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