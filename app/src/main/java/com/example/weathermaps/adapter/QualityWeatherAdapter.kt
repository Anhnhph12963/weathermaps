package com.example.weathermaps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermaps.databinding.ItemCommentQualityweatherBinding
import com.example.weathermaps.model.CommentQualityModel

class QualityWeatherAdapter(var mCommentQuality: List<CommentQualityModel>? = null) : RecyclerView.Adapter<QualityWeatherAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemCommentQualityweatherBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentQualityweatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(mCommentQuality!![position]) {
                binding.tvUsername.text = this.tv_username
                binding.tvComment.text = this.tv_comment
                binding.tvDateComment.text = this.tv_date_comment
            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return mCommentQuality!!.size
    }

}