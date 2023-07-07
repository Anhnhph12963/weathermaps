package com.example.weathermaps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermaps.databinding.ItemCommentPostBinding
import com.example.weathermaps.databinding.ItemRycPostBinding
import com.example.weathermaps.model.PostModel

private lateinit var itemRycPostBinding: ItemRycPostBinding

class PostsDetailAdapter (var mPostModel: List<PostModel>? = null) :
    RecyclerView.Adapter<PostsDetailAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCommentPostBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsDetailAdapter.ViewHolder {
        val binding =
            ItemCommentPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsDetailAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(mPostModel!![position]) {


            }
        }
    }

    override fun getItemCount(): Int {
        return mPostModel!!.size
    }
}