package com.example.weathermaps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermaps.databinding.FragmentDetailPostBinding
import com.example.weathermaps.model.PostModel

private lateinit var fragmentDetailPostBinding: FragmentDetailPostBinding

class PostsAdapter(var mPostModel: List<PostModel>? = null) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: FragmentDetailPostBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.ViewHolder {
        val binding =
            FragmentDetailPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(mPostModel!![position]) {
                binding.tvUsernameDetailpost.text = this.usernamePost
                binding.tvTitle.text = this.titlePost
                binding.tvLocation.text= this.locationPost
                binding.tvDate.text= this.datePost

            }
        }
    }

    override fun getItemCount(): Int {
        return mPostModel!!.size
    }
}