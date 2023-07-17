package com.example.weathermaps.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermaps.databinding.ItemRycPostBinding
import com.example.weathermaps.model.PostModel
import com.squareup.picasso.Picasso


class PostsAdapter(private val mPostModel: ArrayList<PostModel>) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRycPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun bind(postModel: PostModel){
                binding.tvDate.text = "Ngày : ${postModel.date}"
                binding.tvTitle.text = postModel.title
                binding.tvLocation.text = "Địa điểm : ${postModel.location}"
                if (postModel.imagePost.isNotEmpty()){
                Picasso.get().load(postModel.imagePost).into(binding.imgImage)}
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ItemRycPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = mPostModel!!.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mPostModel[position])
    }


}