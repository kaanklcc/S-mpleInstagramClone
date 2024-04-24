package com.kaankilic.kotlininstagram.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kaankilic.kotlininstagram.R
import com.kaankilic.kotlininstagram.adapter.FeedRecyclerAdapter
import com.kaankilic.kotlininstagram.databinding.ActivityFeedBinding
import com.kaankilic.kotlininstagram.model.Post

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var  auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private  lateinit var  postArrayList : ArrayList<Post>
    private lateinit var feedAdapter : FeedRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        db=Firebase.firestore
        postArrayList = ArrayList<Post>()
        getData()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = feedAdapter


    }

    private fun getData(){

        db.collection("Posts").addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }else{
                if (value != null){
                    if (!value.isEmpty){

                       val documents =  value.documents
                        for (document in documents){
                            //casting
                            val comment  = document.get("comment") as String
                            val useremail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String

                            val post = Post(useremail,comment,downloadUrl)
                            postArrayList.add(post)

                        }
                        feedAdapter.notifyDataSetChanged()

                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.insta_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.add_post){
            val intent = Intent(this, UploadAcitivity::class.java)
            startActivity(intent)
        }else if (item.itemId == R.id.Signout){
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()


        }

        return super.onOptionsItemSelected(item)
    }



    }