package com.example.weathermaps

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import com.example.weathermaps.adapter.PostsAdapter
import com.example.weathermaps.databinding.FragmentPostBinding
import com.example.weathermaps.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class FragmentPost : Fragment() {
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseAuth: FirebaseAuth
    private var postsAdapter :PostsAdapter ?= null
    private var listDataPost :ArrayList<PostModel> ? =null

    private var binding: FragmentPostBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() {
        binding?.apply {
            listDataPost= arrayListOf()
            postsAdapter = PostsAdapter(listDataPost!!)
            getDataPost()
            recyclerView.adapter = postsAdapter


        }
    }


    private fun getDataPost(){
        val  db  =  FirebaseDatabase.getInstance().getReference("Posts")
        db.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (item in snapshot.children) {
                    val post = item.getValue<PostModel>()
                    listDataPost?.add(post!!)
                    Log.d("ABCD", "onDataChange:post ${post.toString()}   ")
                }

               postsAdapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
    private fun searchPost(){
        binding?.apply {

        }
    }
}