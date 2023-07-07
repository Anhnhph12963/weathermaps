package com.example.weathermaps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermaps.databinding.FragmentAddPostBinding
import com.example.weathermaps.databinding.ItemCommentPostBinding
import com.example.weathermaps.model.PostModel

data class PostsAddAdapter(var mPostModel: List<PostModel>? = null) :
    RecyclerView.Adapter<PostsAddAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: FragmentAddPostBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAddAdapter.ViewHolder {
        val binding =
            FragmentAddPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsAddAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(mPostModel!![position]) {


            }
        }
    }

    override fun getItemCount(): Int {
        return mPostModel!!.size
    }
}