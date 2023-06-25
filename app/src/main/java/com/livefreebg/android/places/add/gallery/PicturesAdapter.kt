package com.livefreebg.android.places.add.gallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livefreebg.android.databinding.ItemPictureBinding

class PicturesAdapter : RecyclerView.Adapter<PicturesAdapter.ViewHolder>() {

    private var pictures: List<Uri> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = pictures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pictures[position])
    }

    fun setPictures(pictures: List<Uri>) {
        this.pictures = pictures
        notifyDataSetChanged()
    }

    data class ViewHolder(val binding: ItemPictureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri) {
            binding.picture.setImageURI(uri)
        }
    }
}
